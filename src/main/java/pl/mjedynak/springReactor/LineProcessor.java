package pl.mjedynak.springReactor;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mjedynak.PrimeFactorCounter;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;

import java.util.concurrent.Phaser;

import static pl.mjedynak.springReactor.Bootstrap.PROCESSED_EVENT;

public class LineProcessor implements Consumer<Event<String>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private PrimeFactorCounter primeFactorCounter;
    @Autowired private Reactor eventLoopDispatcherReactor;
    @Autowired private Phaser phaser;

    @Override
    public void accept(Event<String> stringEvent) {
        try {
            String line = stringEvent.getData();
            long value = Long.valueOf(line);
            if (value > 0) {
                long factor = primeFactorCounter.primeFactors(value);
    //            logger.debug("processed " + line + "," + factor + ", " + Thread.currentThread());
                eventLoopDispatcherReactor.notify(PROCESSED_EVENT, Event.wrap(line + "," + factor));
            } else {
                phaser.arrive();
            }
        } catch (Exception e) {
            phaser.arrive();
            Event.Headers headers = new Event.Headers(ImmutableMap.of("item", stringEvent.getData()));
            Event<Exception> wrap = new Event<>(headers, e);
            eventLoopDispatcherReactor.notify(Exception.class, wrap);
        }
    }
}
