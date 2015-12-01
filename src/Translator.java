import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import source.URLSourceProvider;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Provides utilities for translating texts to russian language.<br/>
 * Uses Yandex Translate API, more information at <a href="http://api.yandex.ru/translate/">http://api.yandex.ru/translate/</a><br/>
 * Depends on {@link URLSourceProvider} for accessing Yandex Translator API service
 */
public class Translator {
    private URLSourceProvider urlSourceProvider;
    /**
     * Yandex Translate API key could be obtained at <a href="http://api.yandex.ru/key/form.xml?service=trnsl">http://api.yandex.ru/key/form.xml?service=trnsl</a>
     * to do that you have to be authorized.
     */
    private static final String YANDEX_API_KEY = "trnsl.1.1.20151116T110931Z.edba483e0a13cf7e.a3c732e577690ab3f3885745dc907f7f18720c5d";
    private static final String TRANSLATION_DIRECTION = "ru";

    public Translator(URLSourceProvider urlSourceProvider) {
        this.urlSourceProvider = urlSourceProvider;
    }

    /**
     * Translates text to russian language
     * @param original text to translate
     * @return translated text
     * @throws IOException
     */
    public String translate(String original) throws IOException {
        String translatedText = "";
        String prepareURL = prepareURL(original);
        if (urlSourceProvider.isAllowed(prepareURL)) {
            String content = urlSourceProvider.load(prepareURL);
            translatedText = parseContent(content);
        }
        return translatedText;
    }

    /**
     * Prepares URL to invoke Yandex Translate API service for specified text
     * @param text to translate
     * @return url for translation specified text
     */
    private String prepareURL(String text) {
        return "https://translate.yandex.net/api/v1.5/tr/translate?key=" + YANDEX_API_KEY + "&text=" + encodeText(text) + "&lang=" + TRANSLATION_DIRECTION;
    }

    /**
     * Parses content returned by Yandex Translate API service. Removes all tags and system texts. Keeps only translated text.
     * @param content that was received from Yandex Translate API by invoking prepared URL
     * @return translated text
     */
    private String parseContent(String content) {
        try {
            DOMParser parser = new DOMParser();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(content));
            parser.parse(is);
            Document xmlDoc = parser.getDocument();
            NodeList root = xmlDoc.getChildNodes();

            Node translation = getNode("translation", root);
            Node text = getNode("text", translation.getChildNodes());
            content = getNodeValue(text);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private Node getNode(String tagName, NodeList nodes){
        for (int i=0; i<nodes.getLength(); i++){
            Node node = nodes.item(i);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }
        return null;
    }

    private String getNodeValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node data = childNodes.item(i);
            if (data.getNodeType() == Node.TEXT_NODE) {
                return data.getTextContent();
            }
        }
        return "";
    }

    /**
     * Encodes text that need to be translated to put it as URL parameter
     * @param text to be translated
     * @return encoded text
     */
    private String encodeText(String text) {
        try {
            return URLEncoder.encode(text,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }
}
