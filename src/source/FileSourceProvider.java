package source;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for loading content from local file system.
 * This implementation supports absolute paths to local file system without specifying file:// protocol.
 * Examples c:/1.txt or d:/pathToFile/file.txt
 */
public class FileSourceProvider implements SourceProvider {

    public static final String WHITE_SPACE = " ";

    @Override
    public boolean isAllowed(String pathToSource) {
        Path path = Paths.get(pathToSource);
        if (Files.exists(path) && Files.isReadable(path)) {
            return true;
        }
        return false;
    }

    @Override
    public String load(String pathToSource) throws IOException {
        List<String> readLines = Files.readAllLines(Paths.get(pathToSource), Charset.defaultCharset());

        return readLines.stream()
                        .collect(Collectors.joining(WHITE_SPACE));
    }
}
