package pl.mjedynak.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import pl.mjedynak.akka.message.Calculate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static pl.mjedynak.akka.message.MessageType.READING_FINISHED;

public class Bootstrap {

    static final String PRIME_FACTOR_FILE = "primeFactorCounter-akka.txt";
    static final String SUMMARY_FILE = "summary-akka.txt";
    static final String EXCEPTIONS_FILE = "exceptions-akka.txt";

    public void start() throws IOException {
        Config config = ConfigFactory.parseString("akka.loglevel = DEBUG \n");
        ActorSystem system = ActorSystem.create("PrimeFactors", config);
        ActorRef master = system.actorOf(Master.createMaster(), "master");
        File file = new File("numbers.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            master.tell(new Calculate(line), ActorRef.noSender());
        }
        br.close();
        master.tell(READING_FINISHED, ActorRef.noSender());
    }


}
