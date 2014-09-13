package pl.mjedynak.springReactor;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SpringReactorSystemTest {

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
        List<String> lines = FileUtils.readLines(new File(Bootstrap.PRIME_FACTOR_FILE));
        assertThat(lines.size(), is(9900));
        assertThat(lines.contains("5556634922133,5"), is(true));
        File exceptionsFile = new File(Bootstrap.EXCEPTIONS_FILE);
        assertThat(exceptionsFile.exists(), is(true));
        String exceptionsFileContent = FileUtils.readFileToString(exceptionsFile);
        assertThat(exceptionsFileContent, containsString("badData1"));
        assertThat(exceptionsFileContent, containsString("badData2"));
        assertThat(new File(Bootstrap.SUMMARY_FILE).exists(), is(true));
    }

}
