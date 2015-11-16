package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Implementation for loading content from specified URL.<br/>
 * Valid paths to load are http://someurl.com, https://secureurl.com, ftp://frpurl.com etc.
 */
public class URLSourceProvider implements SourceProvider {

    @Override
    public boolean isAllowed(String pathToSource) {
        try {
            URL url = new URL(pathToSource);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public String load(String pathToSource) throws IOException {
        URL url = new URL(pathToSource);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder loadedText = new StringBuilder();
        String readLine = "";
        while ((readLine = br.readLine())!=null) {
            loadedText.append(readLine);
        }
        br.close();

        return loadedText.toString();
    }
}
