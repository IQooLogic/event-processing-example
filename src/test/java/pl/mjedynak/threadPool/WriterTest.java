package pl.mjedynak.threadPool;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.TempFileCreator;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class WriterTest {

    @Mock private BlockingQueue<String> queue;
    @Mock private CountDownLatch latch;

    @Test
    public void shouldDoNothingIfLatchCountIsZeroAndQueueIsEmpty() throws Exception {
        // given
        String initialContent = "initialContent";
        File tempFile = TempFileCreator.createTempFile(initialContent);
        Writer writer = new Writer(queue, latch, tempFile.getAbsolutePath());
        given(latch.getCount()).willReturn(0L);
        given(queue.size()).willReturn(0);

        // when
        writer.write();

        // then
        verify(queue).size();
        verifyNoMoreInteractions(queue);
        String content = FileUtils.readFileToString(tempFile);
        assertThat(content, is(initialContent));
    }

    @Test
    public void shouldWriteToFile() throws Exception {
        // given
        File tempFile = TempFileCreator.createTempFile("");
        Writer writer = new Writer(queue, latch, tempFile.getAbsolutePath());
        String line = "line";
        given(latch.getCount()).willReturn(1L).willReturn(0L);
        given(queue.size()).willReturn(1).willReturn(0);
        given(queue.poll(1, TimeUnit.SECONDS)).willReturn(line).willReturn(null);

        // when
        writer.write();

        // then
        String content = FileUtils.readFileToString(tempFile);
        assertThat(content, is(line));
    }

}
