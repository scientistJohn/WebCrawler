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
    public static void main(String[] args) throws Exception {
        System.out.println("____________________________________________");
        System.out.println("___________HERE IS IT BEGINS________________");
        System.out.println("____________________________________________");

        Crawler crawler = new Crawler("https://www.tight.media/");
        crawler.crawl();

        System.out.println("____________________________________________");
    }
}
