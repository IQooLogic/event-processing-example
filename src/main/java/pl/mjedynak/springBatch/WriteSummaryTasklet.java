package pl.mjedynak.springBatch;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;

public class WriteSummaryTasklet implements Tasklet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Long startTime = (Long) chunkContext.getStepContext().getJobParameters().get(Bootstrap.START_TIME_PARAMETER);
        String summary =  "spring batch processing, time taken " + (System.currentTimeMillis() - startTime) + " ms";
        logger.debug(summary);
        FileUtils.writeStringToFile(new File(Bootstrap.SUMMARY_FILE), summary);
        return RepeatStatus.FINISHED;
    }
}
