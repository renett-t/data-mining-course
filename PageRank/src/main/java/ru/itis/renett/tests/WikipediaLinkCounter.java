package ru.itis.renett.tests;

import ru.itis.renett.crawler.WebCrawler;

import java.util.*;

public class WikipediaLinkCounter {
    public static String SITE = "https://ru.wikipedia.org/wiki/%D0%90%D1%80%D1%81%D0%BB%D0%B0%D0%BD%D0%BE%D0%B2,_%D0%9C%D0%B0%D1%80%D0%B0%D1%82_%D0%9C%D0%B8%D1%80%D0%B7%D0%B0%D0%B5%D0%B2%D0%B8%D1%87";

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        Set<String> setLinks = crawler.getSitesByLink(SITE);
        System.out.println(setLinks);
    }
}
