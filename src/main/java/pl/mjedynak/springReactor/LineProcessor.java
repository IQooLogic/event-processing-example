package pl.mjedynak.springReactor;

import org.springframework.beans.factory.annotation.Autowired;
import pl.mjedynak.PrimeFactorCounter;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.concurrent.Phaser;

import static pl.mjedynak.springReactor.Bootstrap.PROCESSED_EVENT;

public class LineProcessor implements Consumer<Event<String>> {

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
                eventLoopDispatcherReactor.notify(PROCESSED_EVENT, Event.wrap(line + "," + factor));
            } else {
                phaser.arrive();
            }
        } catch (Exception e) {
            phaser.arrive();
            eventLoopDispatcherReactor.notify(Exception.class, Event.wrap(e));
        }
    }
}
