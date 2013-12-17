package pl.mjedynak.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import pl.mjedynak.akka.message.Result;
import scala.collection.mutable.ArraySeq;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static pl.mjedynak.akka.message.MessageType.PROCESSING_FINISHED;
import static pl.mjedynak.akka.message.MessageType.WRITING_FINISHED;

public class Writer extends UntypedActor {

    private final BufferedWriter bufferedWriter;

    public Writer() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(Bootstrap.PRIME_FACTOR_FILE, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Result) {
            bufferedWriter.write(((Result) message).getValue() + "\n");
        } else if (message == PROCESSING_FINISHED) {
            bufferedWriter.close();
            getSender().tell(WRITING_FINISHED, getSelf());
        } else {
            unhandled(message);
        }
    }

    public static Props createWriter() {
        return Props.create(Writer.class, new ArraySeq<>(0));
    }
}
