package pl.mjedynak.synchronous;

import com.google.common.base.Splitter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Reader {

    public Iterable<String> getNumbers(String fileName) throws IOException {
        String fileContent = FileUtils.readFileToString(new File(fileName));
        return Splitter.on("\n").omitEmptyStrings().split(fileContent);
    }
}
