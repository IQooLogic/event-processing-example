package pl.mjedynak.threadPool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.PrimeFactorCounter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.valueOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class LineProcessorTest {

    @Mock private PrimeFactorCounter primeFactorCounter;
    @Mock private BlockingQueue<String> inputQueue;
    @Mock private BlockingQueue<String> successQueue;
    @Mock private BlockingQueue<String> exceptionsQueue;
    @Mock private CountDownLatch inputLatch;
    @Mock private CountDownLatch outputLatch;

    private LineProcessor lineProcessor;

    @Before
    public void setUp() {
        lineProcessor = new LineProcessor(inputQueue, successQueue, exceptionsQueue, inputLatch, outputLatch);
        setField(lineProcessor, "primeFactorCounter", primeFactorCounter);
    }

    @Test
    public void shouldCountDownOnOutputLatch() {
        // given
        given(inputLatch.getCount()).willReturn(0L);
        given(inputQueue.size()).willReturn(0);

        // when
        lineProcessor.process();

        // then
        verify(outputLatch).countDown();
        verifyZeroInteractions(primeFactorCounter, successQueue, exceptionsQueue);
    }

    @Test
    public void shouldPutMessageInSuccessQueue() throws Exception {
        // given
        String number = "1";
        Long computationResult = 2L;
        given(inputLatch.getCount()).willReturn(1L, 0L);
        given(inputQueue.size()).willReturn(1, 0);
        given(inputQueue.poll(1, TimeUnit.SECONDS)).willReturn(number);
        given(primeFactorCounter.primeFactors(valueOf(number))).willReturn(computationResult);

        // when
        lineProcessor.process();

        // then
        verify(successQueue).put(valueOf(number) + "," + computationResult + "\n");
    }

    @Test
    public void shouldNotPutValueInSuccessQueueIfItIsLowerThanZero() throws Exception  {
        // given
        String number = "-1";
        given(inputLatch.getCount()).willReturn(1L, 0L);
        given(inputQueue.size()).willReturn(1, 0);
        given(inputQueue.poll(1, TimeUnit.SECONDS)).willReturn(number);

        // when
        lineProcessor.process();

        // then
        verify(inputQueue).poll(1, TimeUnit.SECONDS);
        verifyZeroInteractions(primeFactorCounter, successQueue);
    }

    @Test
    public void shouldPutMessageInExceptionsQueueIfExceptionOccurs() throws Exception {
        // given
        String number = "1";
        given(inputLatch.getCount()).willReturn(1L, 0L);
        given(inputQueue.size()).willReturn(1, 0);
        RuntimeException exception = new RuntimeException("exception message");
        given(inputQueue.poll(1, TimeUnit.SECONDS)).willReturn(number);
        given(primeFactorCounter.primeFactors(valueOf(number))).willThrow(exception);

        // when
        lineProcessor.process();

        // then
        verify(exceptionsQueue).add(valueOf(number) + ", " + exception + "\n");
    }

}
