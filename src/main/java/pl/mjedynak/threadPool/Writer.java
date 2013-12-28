package pl.mjedynak.threadPool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Writer implements Runnable {

    private final String fileName;

    private final BlockingQueue<String> queue;
    private final CountDownLatch latch;

    public Writer(BlockingQueue<String> queue, CountDownLatch latch, String fileName) {
        this.queue = queue;
        this.fileName = fileName;
        this.latch = latch;
    }

    @Override
    public void run() {
        write();
    }

    void write() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            while (true) {
                if (latch.getCount() == 0 && queue.size() == 0) {
                    return;
                }
                String line = queue.poll(1, TimeUnit.SECONDS);
                if (line != null) {
                    bufferedWriter.write(line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
