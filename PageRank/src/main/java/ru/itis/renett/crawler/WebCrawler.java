package ru.itis.renett.crawler;

import ru.itis.renett.exceptions.WebCrawlerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    private static final String LINK_REGEX = "<a(?:[\\s\\S]+?)href=\"([\\s\\S]+?)\"(?:[\\s\\S]+?)>";
    private Set<String> trash = new HashSet<>();
    private Set<String> linksThatPointNowhere = new HashSet<>();

    public String getHtmlByUrl(String link) throws WebCrawlerException {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {
                StringBuilder sb = new StringBuilder();

                String output;
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    if (connection.getContentType().contains("html")) {
                        while ((output = reader.readLine()) != null) {
                            sb.append(output);
                        }
                        return sb.toString();
                    } else {
                        linksThatPointNowhere.add(link);
                        return "";
                    }
                } else {
                    trash.add(link);
                    return "";
//                    throw new WebCrawlerException("ERROR visiting the link:" + url + ". CODE: " + connection.getResponseCode());
                }
            } catch (IOException e) {
                throw new IOException(e);
            }
        } catch (IOException e) {
            trash.add(link);
            return "";
//            throw new WebCrawlerException(e);
        }
    }

    public Set<String> getSitesByLink(String link) throws WebCrawlerException {
        System.out.println("\n ANALYZING link - " + link);

        String html = getHtmlByUrl(link);
        Pattern pattern = Pattern.compile(LINK_REGEX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        List<String> links = new ArrayList<>();

        while (matcher.find()) {
            links.add(matcher.group(1));
            System.out.println(links.get(links.size() - 1));
        }

        System.out.println("Links found = " + links.size());
        if (links.size() == 0) {
            linksThatPointNowhere.add(link);
        }
        return validate(links);
    }

    private Set<String> validate(List<String> links) {
        Set<String> newLinks = new HashSet<>();

        for (int i = 0; i < links.size(); i++) {
            String link = links.get(i).trim().toLowerCase(Locale.ROOT);
            if (link.startsWith("http")) {
                newLinks.add(link);
            }
        }

        System.out.println("final size: " + newLinks.size() + "\n----------------------");
        return newLinks;
    }

    public Set<String> getTrash() {
        return trash;
    }

    public Set<String> getLinksThatPointNowhere() {
        return linksThatPointNowhere;
    }
}
