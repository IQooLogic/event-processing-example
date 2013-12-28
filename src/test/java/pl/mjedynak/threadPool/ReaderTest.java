package pl.mjedynak.threadPool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.TempFileCreator;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReaderTest {

    @Mock private BlockingQueue<String> queue;
    @Mock private CountDownLatch latch;
    @InjectMocks private Reader reader;

    @Test
    public void shouldPopulateQueue() throws Exception {
        // given
        File tempFile = TempFileCreator.createTempFile("1\n2");

        // when
        reader.read(tempFile.getAbsolutePath());

        // then
        verify(queue).put("1");
        verify(queue).put("2");
        verify(latch).countDown();
    }
}
