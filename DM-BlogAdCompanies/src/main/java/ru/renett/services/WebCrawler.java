package ru.renett.services;

import ru.renett.exceptions.WebCrawlerException;

import java.util.List;

public interface WebCrawler {
    void getDomainsByReferral(List<String> links) throws WebCrawlerException;
    List<String> getCrawledLinks();
}
