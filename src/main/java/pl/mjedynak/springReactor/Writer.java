package pl.mjedynak.springReactor;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.event.Event;
import reactor.function.Consumer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Phaser;

public class Writer implements Consumer<Event<String>> {

    @Autowired private Phaser phaser;
    private final BufferedWriter bufferedWriter;

    public Writer() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(Bootstrap.PRIME_FACTOR_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(Event<String> stringEvent) {
        try {
            bufferedWriter.write(stringEvent.getData() + "\n");
            phaser.arrive();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeStream() throws IOException {
        bufferedWriter.close();
    }
}
