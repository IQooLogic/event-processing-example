package pl.mjedynak.synchronous;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Writer {

    public void write(String fileName, String content) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), content);
    }
}
