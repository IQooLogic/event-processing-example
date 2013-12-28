package pl.mjedynak.synchronous;

import org.junit.Test;
import pl.mjedynak.TempFileCreator;

import java.io.File;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReaderTest {

    private Reader reader = new Reader();

    @Test
    public void shouldReadNumbers() throws Exception {
        // given
        File tempFile = TempFileCreator.createTempFile("1\n2");

        // when
        Iterable<String> numbers = reader.getNumbers(tempFile.getAbsolutePath());

        // then
        Iterator<String> iterator = numbers.iterator();
        assertThat(iterator.next(), is("1"));
        assertThat(iterator.next(), is("2"));
        assertThat(iterator.hasNext(), is(false));
    }
}
