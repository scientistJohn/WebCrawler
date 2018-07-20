package org.andrii.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    private static URL startPage;
    private static Pattern validity = Pattern.compile(".*nv.ua/ukr.*");

    static {
        try {
            startPage = new URL("https://nv.ua/ukr/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("____________________________________________");
        System.out.println("___________HERE IS IT BEGINS________________");
        System.out.println("____________________________________________");

        Deque<URL> linksToBeProcessed = new LinkedList<>();
        Set<URL> processedLinks = new HashSet<>();

        linksToBeProcessed.push(startPage);

        while (!linksToBeProcessed.isEmpty() && processedLinks.size() < 10) {
            URL currLink = linksToBeProcessed.pop();
            Document doc = getDocument(currLink.toString());

            for (Element link : doc.select("a[href]")) {
                String processingLink = link.attr("href");
                if (validLink(processingLink)) {
                    URL url = getFullLink(processingLink);
                    if (!processedLinks.contains(url)) {
                        linksToBeProcessed.add(url);
                    }
                }
            }

            processedLinks.add(currLink);
        }

        System.out.println("____________________________________________");
    }

    private static Document getDocument(String link) throws IOException {
        try {
            Connection connection = Jsoup.connect(link);
            connection.timeout(10*10000);
            connection.userAgent("Mozilla/5.0");
            return connection.get();
        } catch (Exception e) {
            return getDocument(link);
        }
    }

    private static URL getFullLink(String processingLink) throws MalformedURLException {
        if (!processingLink.startsWith("http")) {
            return new URL(startPage, processingLink);
        }

        return new URL(processingLink);
    }

    private static boolean validLink(String processingLink) {
        if (processingLink.startsWith("/")) {
            return true;
        }

        Matcher m = validity.matcher(processingLink);
        return m.matches();
    }
}
