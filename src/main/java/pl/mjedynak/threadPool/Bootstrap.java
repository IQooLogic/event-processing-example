package pl.mjedynak.threadPool;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bootstrap {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int THREAD_POOL_SIZE = 4;

    static final String PRIME_FACTOR_FILE = "primeFactorCounter-threadPool.txt";
    static final String EXCEPTIONS_FILE = "exceptions-threadPool.txt";
    static final String SUMMARY_FILE = "summary-threadPool.txt";

    private BlockingQueue<String> inputQueue = new ArrayBlockingQueue<>(10000);
    private BlockingQueue<String> successQueue = new ArrayBlockingQueue<>(10000);
    private BlockingQueue<String> exceptionsQueue = new ArrayBlockingQueue<>(10000);
    private CountDownLatch inputLatch = new CountDownLatch(1);
    private CountDownLatch outputLatch = new CountDownLatch(THREAD_POOL_SIZE);


    public void start() throws Exception {
        long startTime = System.currentTimeMillis();
        Reader reader = new Reader(inputQueue, inputLatch);
        Writer successWriter = new Writer(successQueue, outputLatch, PRIME_FACTOR_FILE);
        Writer exceptionsWriter = new Writer(exceptionsQueue, outputLatch, EXCEPTIONS_FILE);

        Collection<LineProcessor> lineProcessors = new ArrayList<>();
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            lineProcessors.add(new LineProcessor(inputQueue, successQueue, exceptionsQueue, inputLatch, outputLatch));
        }
        ExecutorService lineProcessorExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (LineProcessor lineProcessor : lineProcessors) {
            lineProcessorExecutor.execute(lineProcessor);
        }

        new Thread(reader).start();
        Thread successWriterThread = new Thread(successWriter);
        successWriterThread.start();
        Thread exceptionsWriterThread = new Thread(exceptionsWriter);
        exceptionsWriterThread.start();

        lineProcessorExecutor.shutdown();
        successWriterThread.join();
        exceptionsWriterThread.join();

        String summary = String.format("thread pool processing, time taken %d ms", System.currentTimeMillis() - startTime);
        logger.debug(summary);
        FileUtils.writeStringToFile(new File(SUMMARY_FILE), summary);
    }
}
