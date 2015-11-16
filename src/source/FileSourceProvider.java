package source;

import java.io.*;

/**
 * Implementation for loading content from local file system.
 * This implementation supports absolute paths to local file system without specifying file:// protocol.
 * Examples c:/1.txt or d:/pathToFile/file.txt
 */
public class FileSourceProvider implements SourceProvider {

    @Override
    public boolean isAllowed(String pathToSource) {
        File file = new File(pathToSource);
        if (file.exists() && file.canRead()) {
            return true;
        }
        return false;
    }

    @Override
    public String load(String pathToSource) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(pathToSource));
        StringBuilder loadedText = new StringBuilder();
        String readLine = "";
        while ((readLine = bf.readLine()) != null){
            loadedText.append(readLine);
        }
        bf.close();

        return loadedText.toString();
    }
}
