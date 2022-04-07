package ru.renett.services;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.springframework.stereotype.Component;
import ru.renett.exceptions.WebCrawlerException;
import ru.renett.models.Link;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class WebCrawlerImpl extends edu.uci.ics.crawler4j.crawler.WebCrawler implements WebCrawler {
    private final List<String> domains = new ArrayList<>();
    private final List<String> visiting = new ArrayList<>();
    private final Map<Integer, String> linksMap = new HashMap<>();
    private int count = 0;

    private static final String FILE = "/home/renett/Repositories/java/4th-semester-programming/DM-BlogAdCompanies/src/main/resources/domain.txt";

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        System.out.println("handleUrlBeforeProcess");
        System.out.println(curURL.getURL());
        return super.handleUrlBeforeProcess(curURL);
    }

    @Override
    protected void onRedirectedStatusCode(Page page) {
        System.out.println("REDIRECT! " + page.getWebURL().getURL());
        super.onRedirectedStatusCode(page);
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        System.out.println("Should visit? " + href);
        visiting.add(href);
        return (href.startsWith("http"));
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        System.out.println("URL: " + url);
        System.out.println("domain: " + domain);
        domains.add(domain);
        linksMap.put(count++, domain);
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        System.out.println("Здесь должна быть запись в файл результата.");
        System.out.println(visiting);
        System.out.println(linksMap);

//        saveDomainToFile(domains);
    }

    private void saveDomainToFile(List<String> domains) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (String domain : domains) {
                writer.append(domain);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("SOS WASN'T SAVED, reason: " + e.getMessage());
        }
    }

    private List<String> getDomainsNames() {
        List<String> domains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String domain;
            while ((domain = reader.readLine()) != null) {
                domains.add(domain);
            }
        } catch (IOException e) {
            System.out.println("SOS CAN'T READ, reason: " + e.getMessage());
        }
        return domains;
    }

    @Override
    public void getDomainsByReferral(List<String> links) throws WebCrawlerException {
        try {
            String crawlStorageFolder = "/tmp/crawler4j/";
            int numberOfCrawlers = 1;

            CrawlConfig config = new CrawlConfig();
            config.setIncludeHttpsPages(true);
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setPolitenessDelay(200);
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
            for (String url : links) {
                controller.addSeed(url);
            }
            // The factory which creates instances of crawlers.
            CrawlController.WebCrawlerFactory<WebCrawlerImpl> factory = WebCrawlerImpl::new;

            // Start the crawl. This is a blocking operation, meaning that your code
            // will reach the line after this only when crawling is finished.
            System.out.println("STARTING WEB CRAWLER!");
            controller.start(factory, numberOfCrawlers);
        } catch (Exception e) {
            throw new WebCrawlerException(e);
        }
    }

    @Override
    public List<String> getCrawledLinks() {
        return getDomainsNames();
    }

//    public static void main(String[] args) {
//        WebCrawlerImpl webCrawler = new WebCrawlerImpl();
//        webCrawler.saveDomainToFile(List.of("domain1", "domain2", "domain3", "domain4"));
//        System.out.println(webCrawler.getDomainsNames());
//    }

    public static void main(String[] args) {
        WebCrawlerImpl webCrawler = new WebCrawlerImpl();
        List<String> list = List.of("https://alfa.link/scherbakov", "https://instagram.com/hold_auto", "https://youtu.be/luR1e-_LhrM",
                "https://tchk.me/zmvuSb", "https://clck.ru/S3z6g", "https://moscow.shop.megafon.ru", "https://clik.cc/8AF15",
                "https://bit.ly/3uG4O3u", "https://wtplay.link/usachev", "https://usachev.shop/", "https://alfa.me/dOfW7Q",
                "https://app.perekrestok.ru/AEfu/d4c025a4", "https://aviasales.ru", "https://netolo.gy/gVm");

        // {0=megafon.ru, 1=usachev.shop, 2=alfabank.ru, 3=alfabank.ru, 4=aviasales.ru, 5=netology.ru, 6=cian.ru, 7=perekrestok.ru, 8=playstation.com}
        webCrawler.getDomainsByReferral(list);
        System.out.println("_________________________");
    }
}

//https://alfa.link/scherbakov
//https://instagram.com/hold_auto
//https://youtu.be/luR1e-_LhrM
//https://tchk.me/zmvuSb
//https://clck.ru/S3z6g
//https://moscow.shop.megafon.ru
//https://clik.cc/8AF15
//https://bit.ly/3uG4O3u
//https://wtplay.link/usachev
//https://usachev.shop/
//https://alfa.me/dOfW7Q
//https://app.perekrestok.ru/AEfu/d4c025a4
//https://aviasales.ru
//https://netolo.gy/gVm