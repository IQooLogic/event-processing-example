package pl.mjedynak.springReactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.mjedynak.PrimeFactorCounter;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.dispatch.SynchronousDispatcher;

import java.util.concurrent.Phaser;

@Configuration
@ComponentScan("pl.mjedynak.springReactor")
public class ReactorConfiguration {

    @Bean
    Environment env() {
        return new Environment();
    }

    @Bean
    Reactor threadPoolDispatcherReactor(Environment env) {
        return Reactors.reactor()
                .env(env)
                .dispatcher(Environment.THREAD_POOL)
                .get();
    }

    @Bean
    Reactor eventLoopDispatcherReactor(Environment env) {
        return Reactors.reactor()
                .env(env)
                .dispatcher(Environment.EVENT_LOOP)
                .get();
    }

    @Bean
    Reader reader() {
        return new Reader();
    }

    @Bean
    LineProcessor lineProcessor() {
        return new LineProcessor();
    }

    @Bean
    Writer writer() {
        return new Writer();
    }

    @Bean
    ErrorConsumer errorConsumer() {
        return new ErrorConsumer();
    }

    @Bean
    PrimeFactorCounter primeFactorCounter() {
        return new PrimeFactorCounter();
    }

    @Bean
    Phaser phaser() {
        return new Phaser();
    }
}
