package ru.renett.services.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCrawler {
    public static void main(String[] args) {
        List<String> links = new ArrayList<>();
        links.add("https://instagram.com/renett_t");
        links.add("https://www.youtube.com/watch?v=BYlSp4YE5rw");
        links.add("https://alfabank.ru");
        links.add("https://music.yandex.ru/");
        links.add("https://greenpeace.ru/");
        links.add("https://vk.com/fintech.tinkoff");
        links.add("https://alfa.link/scherbakov");
        links.add("https://trk.mail.ru/c/p55r28");
        links.add("http://surl.li/arbjy");
        links.add("https://bit.ly/30xnzcp");
        links.add("https://ya.cc/t/8y3cqTRnDNLM5");
        links.add("https://alfa.me/xDYr9Q");
        links.add("https://bit.ly/2YHBqff");

        for (String link : links) {
            try {
                System.out.println("------------" + link);
                boolean approved = false;
                int count = 0;

                while (!approved) {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:98.0) Gecko/20100101 Firefox/98.0");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setConnectTimeout(10000);
                    connection.connect();
                    System.out.println("connected!");
                    count++;

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Map<String, List<String>> headers = connection.getHeaderFields();
                        System.out.println(headers);
                        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                            if (entry.getKey().equals("X-Host") || entry.getKey().equals("Host") || entry.getKey().equals("Cookie")) {
                                System.out.println(entry.getValue());
                            }
                        }
                        approved = true;
                    } else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                        link = connection.getHeaderField("Location");
                        System.out.println("RETRY - " + link);
                        if (count > 4) {
                            break;
                        }
                    } else {
                        System.out.println("ERROR FOR : " + link + "; " + connection.getResponseCode() + connection.getResponseMessage());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
