package pl.mjedynak.threadPool;

import pl.mjedynak.PrimeFactorCounter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LineProcessor implements Runnable {

    private final PrimeFactorCounter primeFactorCounter = new PrimeFactorCounter();

    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<String> successQueue;
    private final BlockingQueue<String> exceptionsQueue;
    private final CountDownLatch inputLatch;
    private final CountDownLatch outputLatch;

    public LineProcessor(BlockingQueue<String> inputQueue, BlockingQueue<String> successQueue, BlockingQueue<String> exceptionsQueue, CountDownLatch inputLatch, CountDownLatch outputLatch) {
        this.inputQueue = inputQueue;
        this.successQueue = successQueue;
        this.exceptionsQueue = exceptionsQueue;
        this.inputLatch = inputLatch;
        this.outputLatch = outputLatch;
    }

    @Override
    public void run() {
        while (true) {
            String number = null;
            try {
                if (inputLatch.getCount() == 0 && inputQueue.size() == 0) {
                    outputLatch.countDown();
                    return;
                }
                number = inputQueue.poll(1, TimeUnit.SECONDS);
                long value = Long.valueOf(number);
                if (value > 0) {
                    long factor = primeFactorCounter.primeFactors(value);
                    successQueue.put(value + "," + factor + "\n");
                }
            } catch (Exception e) {
                exceptionsQueue.add(number + ", " + e + "\n");
            }
        }
    }

}
