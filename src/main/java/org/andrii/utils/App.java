package org.andrii.utils;

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
