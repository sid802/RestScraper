import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sid on 5/17/2016.
 */
public  class URLParser {


    /**
     *
     * @param urlString URL of string we want to get its source from
     * @return
     */
    public static String parseToString(String urlString) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader buffered = new BufferedReader(inReader);
            if (buffered != null) {
                int cp;
                while ((cp = buffered.read()) != -1) {
                    sb.append((char)cp);
                }
                buffered.close();
            }
            in.close();
            inReader.close();
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed");
        }
        catch (IOException e) {
            System.out.println("IOException");
        }

        return sb.toString();
    }

    /**
     *
     * @param urlString URL of string we want to get its source from
     * @return
     */
    public static Document parseToDocument(String urlString) {
        InputStream in = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            in = urlConnection.getInputStream();
            //doc = Jsoup.parse(in);
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed");
        }
        catch (IOException e) {
            System.out.println("IOException");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
