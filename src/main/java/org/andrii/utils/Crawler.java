package org.andrii.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private static final int timeout = 100000;
    private URL startPage;
    private Pattern validity;
    private TreeSet<URL> linksToBeProcessed = new TreeSet<>(new Comparator<URL>() {
        @Override
        public int compare(URL o1, URL o2) {
            return (o1.toString()).compareTo(o2.toString());
        }
    });
    private Set<URL> processedLinks = new HashSet<>();

    public Crawler(String startPageLink) throws MalformedURLException {
        this.startPage = new URL(startPageLink);
        this.validity = Pattern.compile(".*" + startPage.getHost().replace("www.", "") + ".*");
        linksToBeProcessed.add(startPage);
    }

    public void crawl() throws IOException {
        while (!linksToBeProcessed.isEmpty()) {
            URL currLink = linksToBeProcessed.first();
            Document doc = getDocument(currLink.toString());

            for (Element link : doc.select("a[href]")) {
                URL url = getFullLink(link.attr("href"));
                if (validLink(url.getHost())) {
                    if (!processedLinks.contains(url)) {
                        linksToBeProcessed.add(url);
                    }
                }
            }

            processedLinks.add(currLink);
            linksToBeProcessed.remove(currLink);
        }
    }

    private Document getDocument(String link) throws IOException {
        try {
            Connection connection = Jsoup.connect(link);
            connection.timeout(timeout);
            connection.userAgent("Mozilla/5.0");
            return connection.get();
        } catch (Exception e) {
            return getDocument(link);
        }
    }

    private URL getFullLink(String processingLink) throws MalformedURLException {
        if (!processingLink.startsWith("http")) {
            return new URL(startPage, processingLink);
        }

        return new URL(processingLink);
    }

    private boolean validLink(String processingLink) {
        if (processingLink.startsWith("/")) {
            return true;
        }

        if (processingLink.startsWith("mailto")) {
            return false;
        }

        Matcher m = validity.matcher(processingLink);
        return m.matches();
    }
}
