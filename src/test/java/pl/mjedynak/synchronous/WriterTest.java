package pl.mjedynak.synchronous;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import pl.mjedynak.TempFileCreator;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WriterTest {

    private Writer writer = new Writer();

    @Test
    public void shouldWriteStringToFile() throws Exception {
        // given
        File file = TempFileCreator.createTempFile("anyContent");
        String fileContent = "fileContent";

        // when
        writer.write(file.getAbsolutePath(), fileContent);

        // then
        String content = FileUtils.readFileToString(file);
        assertThat(content, is(fileContent));
    }
}
