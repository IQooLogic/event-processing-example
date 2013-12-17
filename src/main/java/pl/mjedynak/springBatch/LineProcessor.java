package pl.mjedynak.springBatch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mjedynak.PrimeFactorCounter;

public class LineProcessor implements ItemProcessor<String, String> {

    @Autowired private PrimeFactorCounter primeFactorCounter;

    @Override
    public String process(String number) throws Exception {
        long value = Long.valueOf(number);
        if (value > 0) {
            long factor = primeFactorCounter.primeFactors(value);
            return number + "," + factor;
        }
        return null;

    }
}
