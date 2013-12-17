package pl.mjedynak.springReactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.Phaser;

import static pl.mjedynak.springReactor.Bootstrap.READ_EVENT;

@Component
public class Reader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private Reactor threadPoolDispatcherReactor;
    @Autowired private Phaser phaser;

    public void read() throws Exception {
        File file = new File("numbers.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            phaser.register();
            threadPoolDispatcherReactor.notify(READ_EVENT, Event.wrap(line));
        }
        br.close();
        logger.debug("finished reading");
    }

}
