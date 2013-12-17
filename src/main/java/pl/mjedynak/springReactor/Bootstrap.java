package pl.mjedynak.springReactor;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.core.Reactor;

import java.io.File;
import java.util.concurrent.Phaser;

import static reactor.event.selector.Selectors.$;
import static reactor.event.selector.Selectors.T;

public class Bootstrap {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String PRIME_FACTOR_FILE = "primeFactorCounter-springReactor.txt";
    static final String SUMMARY_FILE = "summary-springReactor.txt";
    static final String EXCEPTIONS_FILE = "exceptions-springReactor.txt";

    static final String READ_EVENT = "readEvent";
    static final String PROCESSED_EVENT = "processedEvent";

    public void start() throws Exception {
        long startTime = System.currentTimeMillis();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReactorConfiguration.class);
        Reactor threadPoolDispatcherReactor = (Reactor) context.getBean("threadPoolDispatcherReactor");
        Reactor eventLoopDispatcherReactor = (Reactor) context.getBean("eventLoopDispatcherReactor");
        LineProcessor lineProcessor = (LineProcessor) context.getBean("lineProcessor");
        Reader reader = (Reader) context.getBean("reader");
        Writer writer = (Writer) context.getBean("writer");
        Phaser phaser = (Phaser) context.getBean("phaser");
        ErrorConsumer errorConsumer = (ErrorConsumer) context.getBean("errorConsumer");

        threadPoolDispatcherReactor.on($(READ_EVENT), lineProcessor);
        eventLoopDispatcherReactor.on($(PROCESSED_EVENT), writer);
        eventLoopDispatcherReactor.on(T(Exception.class), errorConsumer);

        reader.read();
        phaser.awaitAdvance(0);
        writer.closeStream();

        String summary = String.format("spring reactor processing, time taken %d ms", System.currentTimeMillis() - startTime);
        logger.debug(summary);
        FileUtils.writeStringToFile(new File(SUMMARY_FILE), summary);

    }
}
