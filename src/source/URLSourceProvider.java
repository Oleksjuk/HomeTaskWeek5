package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementation for loading content from specified URL.<br/>
 * Valid paths to load are http://someurl.com, https://secureurl.com, ftp://frpurl.com etc.
 */
public class URLSourceProvider implements SourceProvider {

    public static final String GET_METHOD = "GET";
    public static final String FTP_PROTOCOL = "ftp";
    public static final int CODE_OK = 200;

    @Override
    public boolean isAllowed(String pathToSource) {
        try {
            URL url = new URL(pathToSource);
            if (FTP_PROTOCOL.equalsIgnoreCase(url.getProtocol())) {
                return true;
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_METHOD);
            connection.connect();
            if (CODE_OK == connection.getResponseCode()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

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
