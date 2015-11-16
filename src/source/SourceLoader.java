package source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SourceLoader should contains all implementations of SourceProviders to be able to load different sources.
 */
public class SourceLoader {
    private List<SourceProvider> sourceProviders = new ArrayList<SourceProvider>();

    public SourceLoader() {
        Collections.addAll(sourceProviders, new FileSourceProvider(), new URLSourceProvider());
    }

    public String loadSource(String pathToSource) throws IOException {
        for (SourceProvider sourceProvider : sourceProviders) {

            if (sourceProvider.isAllowed(pathToSource)) {

                return sourceProvider.load(pathToSource);
            }
        }

        throw new IOException();
    }
}
