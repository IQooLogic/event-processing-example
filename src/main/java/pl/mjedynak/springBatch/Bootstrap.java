package pl.mjedynak.springBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ExecutorService;

import static com.google.common.collect.ImmutableMap.of;

public class Bootstrap {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String PRIME_FACTOR_FILE = "primeFactorCounter-springBatch.txt";
    static final String SUMMARY_FILE = "summary-springBatch.txt";
    static final String EXCEPTIONS_FILE = "exceptions-springBatch.txt";

    static final String START_TIME_PARAMETER = "START_TIME";


    public void start() throws Exception {
        long startTime = System.currentTimeMillis();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("processNumbersJob");
        ConcurrentTaskExecutor taskExecutor = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService executor = (ExecutorService) taskExecutor.getConcurrentExecutor();

        JobExecution execution = jobLauncher.run(job, new JobParameters(of(START_TIME_PARAMETER, new JobParameter(startTime))));
        executor.shutdown();

        logger.debug("Exit Status : " + execution.getStatus());
    }

}
