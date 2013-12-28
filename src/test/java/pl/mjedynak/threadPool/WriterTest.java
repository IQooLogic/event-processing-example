package pl.mjedynak.threadPool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.TempFileCreator;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

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
        File tempFile = TempFileCreator.createTempFile("anyContent");
        Writer writer = new Writer(queue, latch, tempFile.getAbsolutePath());
        given(latch.getCount()).willReturn(0L);
        given(queue.size()).willReturn(0);

        // when
        writer.write();

        // then
        verify(queue).size();
        verifyNoMoreInteractions(queue);
    }

}
