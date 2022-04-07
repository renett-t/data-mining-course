package ru.renett.services.test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

public class TestWebCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && (href.startsWith("https://") || href.startsWith("http://"));
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println(page.getWebURL().getParentUrl());
        System.out.println("URL: " + url);
        System.out.println("code: " + page.getStatusCode());
        System.out.println("domain: " + page.getWebURL().getDomain());
        System.out.println("sub-domain: " + page.getWebURL().getSubDomain());
        System.out.println("is Redirect: " + page.isRedirect());
        System.out.println("RedirectedToUrl: " + page.getRedirectedToUrl());

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
        }
    }

    public static void main(String[] args) throws Exception {
        File crawlStorage = new File("src/resources/crawler4j");

        String crawlStorageFolder = "/tmp/crawler4j/";
        int numberOfCrawlers = 1;

        CrawlConfig config = new CrawlConfig();
//        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setPolitenessDelay(1000);
        config.setMaxDepthOfCrawling(0);
        config.setIncludeBinaryContentInCrawling(false);
        config.setUserAgentString("Mozilla/5.0 (X11; Linux x86_64; rv:98.0) Gecko/20100101 Firefox/98.0");

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
//        controller.addSeed("https://www.ics.uci.edu/~lopes/");
//        controller.addSeed("https://www.ics.uci.edu/~welling/");
//        controller.addSeed("https://www.ics.uci.edu/");
//        controller.addSeed("http://surl.li/arbjy");
        controller.addSeed("https://alfa.me/xDYr9Q");
//        controller.addSeed("https://music.yandex.ru/");
//        controller.addSeed("https://www.youtube.com/watch?v=BYlSp4YE5rw");

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<TestWebCrawler> factory = TestWebCrawler::new;

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        System.out.println("STARTING!");
        controller.start(factory, numberOfCrawlers);
        System.out.println("END");

        System.out.println("NEW SESSION WITH NEW CONTROLLER AND SAVED CONFIG");
        CrawlController controller2 = new CrawlController(config, pageFetcher, robotstxtServer);
        controller2.addSeed("https://www.ics.uci.edu/");
        controller2.start(factory, numberOfCrawlers);
        System.out.println("VISITED NEW SITE - END");

        System.out.println("START NEW SESSION WITH PREV CONTROLLER");
        controller2.addSeed("https://music.yandex.ru/home");
        controller2.start(factory, numberOfCrawlers);
        System.out.println("VISITED NEW SITE");
    }
}
