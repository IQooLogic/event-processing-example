package pl.mjedynak.synchronous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mjedynak.PrimeFactorCounter;

import java.io.IOException;

public class Bootstrap {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String PRIME_FACTOR_FILE = "primeFactorCounter-synchronous.txt";
    static final String EXCEPTIONS_FILE = "exceptions-synchronous.txt";
    static final String SUMMARY_FILE = "summary-synchronous.txt";

    private final PrimeFactorCounter primeFactorCounter = new PrimeFactorCounter();
    private final Reader reader = new Reader();
    private final Writer writer = new Writer();

    public void start() throws IOException {
        long startTime = System.currentTimeMillis();
        Iterable<String> numbers = reader.getNumbers();
        StringBuilder valuesWithPrimeFactorCounter = new StringBuilder();
        StringBuilder exceptions = new StringBuilder();
        for (String number : numbers) {
            try {
                long value = Long.valueOf(number);
                if (value > 0) {
                    long factor = primeFactorCounter.primeFactors(value);
                    valuesWithPrimeFactorCounter.append(value).append(",").append(factor).append("\n");
                }
            } catch (Exception e) {
                exceptions.append(number).append(", ").append(e).append("\n");
            }
        }
        writeResults(startTime, valuesWithPrimeFactorCounter, exceptions);
    }

    private void writeResults(long startTime, StringBuilder valuesWithPrimeFactorCounter, StringBuilder exceptions) throws IOException {
        writer.write(PRIME_FACTOR_FILE, valuesWithPrimeFactorCounter.toString());
        writer.write(EXCEPTIONS_FILE, exceptions.toString());
        String summary = "synchronous processing, time taken " + (System.currentTimeMillis() - startTime) + " ms";
        logger.debug(summary);
        writer.write(SUMMARY_FILE, summary);
    }


}
