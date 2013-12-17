package pl.mjedynak.threadPool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Reader implements Runnable {

    private final BlockingQueue<String> queue;
    private final CountDownLatch latch;

    public Reader(BlockingQueue<String> queue, CountDownLatch latch) {
        this.queue = queue;
        this.latch = latch;
    }

    public void read() throws Exception {
        File file = new File("numbers.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            queue.put(line);
        }
        br.close();
        latch.countDown();
    }

    @Override
    public void run() {
        try {
            read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
