package pl.mjedynak.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import org.apache.commons.io.FileUtils;
import pl.mjedynak.PrimeFactorCounter;
import pl.mjedynak.akka.message.Calculate;
import pl.mjedynak.akka.message.Result;
import scala.Option;
import scala.collection.mutable.ArraySeq;

import java.io.File;

import static pl.mjedynak.akka.message.MessageType.NO_RESULT;
import static pl.mjedynak.akka.message.MessageType.PROCESSING_EXCEPTION;

public class LineProcessor extends UntypedActor {

    private final PrimeFactorCounter primeFactorCounter = new PrimeFactorCounter();

    // compile error
//    private static SupervisorStrategy strategy = new OneForOneStrategy(-1,
//            Duration.Inf(), new Function<Throwable, SupervisorStrategy.Directive>() {
//        @Override
//        public SupervisorStrategy.Directive apply(Throwable t) {
//            return restart();
//        }
//    });
//
//    @Override
//    public SupervisorStrategy supervisorStrategy() {
//        return strategy;
//    }

    @Override
    public void preRestart(Throwable cause, Option<Object> message) throws Exception {
        super.preRestart(cause, message);
        if (message.isDefined() && message.get() instanceof Calculate) {
            getContext().actorSelection("/user/master").tell(PROCESSING_EXCEPTION, getSelf());
            Calculate calculateObject = (Calculate) message.get();
            FileUtils.write(new File(Bootstrap.EXCEPTIONS_FILE), calculateObject.getValue() + ", " + cause + "\n", true);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Calculate) {
            String number = ((Calculate) message).getValue();
            long value = Long.valueOf(number);
            if (value > 0) {
                long factor = primeFactorCounter.primeFactors(value);
                getSender().tell(new Result(number + "," + factor), getSelf());
            } else {
                getSender().tell(NO_RESULT, getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    public static Props createLineProcessor() {
        return Props.create(LineProcessor.class, new ArraySeq<>(0));
    }
}
