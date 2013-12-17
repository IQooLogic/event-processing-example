package pl.mjedynak.akka;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AkkaSystemTest {
    @Before
    public void setUp() {
        new File(Bootstrap.PRIME_FACTOR_FILE).delete();
        new File(Bootstrap.EXCEPTIONS_FILE).delete();
        new File(Bootstrap.SUMMARY_FILE).delete();
    }

    @Test
    public void shouldProcessFile() throws Exception {
        // when
        new Bootstrap().start();

        // then
        Awaitility.await().atMost(Duration.TWO_MINUTES).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                File file = new File(Bootstrap.PRIME_FACTOR_FILE);
                if (file.exists()) {
                    List<String> lines = FileUtils.readLines(file);
                    return lines.size() == 9900 && lines.contains("5556634922133,5");
                }
                return false;
            }
        });
        File exceptionsFile = new File(Bootstrap.EXCEPTIONS_FILE);
        assertThat(exceptionsFile.exists(), is(true));
        String exceptionsFileContent = FileUtils.readFileToString(exceptionsFile);
        assertThat(exceptionsFileContent, containsString("badData1"));
        assertThat(exceptionsFileContent, containsString("badData2"));
        assertThat(new File(Bootstrap.SUMMARY_FILE).exists(), is(true));
    }
}
