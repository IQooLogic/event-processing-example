package pl.mjedynak.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mjedynak.akka.message.Calculate;
import pl.mjedynak.akka.message.Result;
import scala.collection.mutable.ArraySeq;

import java.io.File;
import java.io.IOException;

import static pl.mjedynak.akka.message.MessageType.NO_RESULT;
import static pl.mjedynak.akka.message.MessageType.PROCESSING_EXCEPTION;
import static pl.mjedynak.akka.message.MessageType.PROCESSING_FINISHED;
import static pl.mjedynak.akka.message.MessageType.READING_FINISHED;
import static pl.mjedynak.akka.message.MessageType.WRITING_FINISHED;

public class Master extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ActorRef lineProcessorRouter;
    private ActorRef writer;
    private int calculationCounter;
    private int responseCounter;
    private boolean readingFinished = false;
    private long startTime = System.currentTimeMillis();

    public Master() {
        lineProcessorRouter = this.getContext().actorOf(LineProcessor.createLineProcessor().withRouter(new RoundRobinRouter(5)), "lineProcessorRouter");
        writer = this.getContext().actorOf(Writer.createWriter());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Calculate) {
            calculationCounter++;
            lineProcessorRouter.tell(message, getSelf());
        } else if (message instanceof Result) {
            responseCounter++;
            writer.tell(message, getSelf());
            logProgress();
            if (responseCounter == calculationCounter && readingFinished) {
                writer.tell(PROCESSING_FINISHED, getSelf());
            }
        } else if (message == PROCESSING_EXCEPTION || message == NO_RESULT) {
            responseCounter++;
        } else if (message == READING_FINISHED) {
            readingFinished = true;
        } else if (message == WRITING_FINISHED) {
            writeSummary();
            getContext().system().shutdown();
        } else {
            unhandled(message);
        }
    }

    private void writeSummary() throws IOException {
        String summary = "akka processing, time taken " + (System.currentTimeMillis() - startTime) + " ms";
        logger.debug(summary);
        FileUtils.writeStringToFile(new File(Bootstrap.SUMMARY_FILE), summary);
    }

    private void logProgress() {
        if (responseCounter % 1000 == 0) {
            logger.debug("responseCounter=" + responseCounter + ", calculationCounter=" + calculationCounter);
        }
    }

    public static Props createMaster() {
        return Props.create(Master.class, new ArraySeq<>(0));
    }
}
