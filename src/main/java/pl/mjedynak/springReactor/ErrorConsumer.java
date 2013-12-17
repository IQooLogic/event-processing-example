package pl.mjedynak.springReactor;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;
import reactor.function.Consumer;

import java.io.File;
import java.io.IOException;

public class ErrorConsumer implements Consumer<Event<Exception>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void accept(Event<Exception> exceptionEvent) {
        try {
            FileUtils.write(new File(Bootstrap.EXCEPTIONS_FILE), exceptionEvent.getHeaders().get("item") + ", " + exceptionEvent.getData() + "\n", true);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}
