import source.SourceLoader;
import source.URLSourceProvider;

import java.io.IOException;
import java.util.Scanner;

public class TranslatorController {

    public static void main(String[] args) {
        //initialization
        SourceLoader sourceLoader = new SourceLoader();
        Translator translator = new Translator(new URLSourceProvider());

        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        while(!"exit".equals(command)) {

            try {
                String source = sourceLoader.loadSource(command);
                System.out.println("Original: " + source);

                String translation = translator.translate(source);
                System.out.println("Translation: " + translation);

            } catch (IOException e) {
                System.out.println("Occur error: please check path to source and try again");
            }

            command = scanner.next();
        }
    }
}
