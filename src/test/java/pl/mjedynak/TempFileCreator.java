package pl.mjedynak;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TempFileCreator {

    public static File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("tempFile", ".tmp");
        FileUtils.writeStringToFile(tempFile, content);
        tempFile.deleteOnExit();
        return tempFile;
    }
}
