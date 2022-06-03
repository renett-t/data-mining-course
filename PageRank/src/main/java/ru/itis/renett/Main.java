package ru.itis.renett;

import ru.itis.renett.crawler.WebCrawler;
import ru.itis.renett.csv.CSVReader;
import ru.itis.renett.csv.CSVWriter;

import java.io.IOException;
import java.util.*;

import static ru.itis.renett.csv.CSVWriter.*;

public class Main {
    public static String FIRST_SITE = "https://greenpeace.ru/";
    public static String SECOND_SITE = "https://study.istamendil.info/";

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        Map<String, Set<String>> linksMap = new HashMap<>();

        String url = FIRST_SITE;
        Set<String> setLinks = crawler.getSitesByLink(url);
        linksMap.put(url, setLinks);

        List<String> listLink = new ArrayList<>(setLinks);

        for (int k = 0; k < listLink.size(); k++) {
            String newPage = listLink.get(k);
            setLinks = crawler.getSitesByLink(newPage);
            if (linksMap.containsKey(newPage)) {
                linksMap.get(newPage).addAll(setLinks);
            } else {
                linksMap.put(newPage, setLinks);
            }

            List<String> internalList = new ArrayList<>(setLinks);
            for (int l = 0; l < internalList.size(); l++) {
                newPage = internalList.get(l);
                setLinks = crawler.getSitesByLink(newPage);
                if (linksMap.containsKey(newPage)) {
                    linksMap.get(newPage).addAll(setLinks);
                } else {
                    linksMap.put(newPage, setLinks);
                }
            }
        }

        // saving data
        System.out.println("Library size: " + linksMap.size());
        try {
            CSVWriter.writeLibrary(linksMap.keySet(), LIBRARY);
            CSVWriter.writeLibrary(crawler.getTrash(), TRASH);
            CSVWriter.writeLibrary(crawler.getLinksThatPointNowhere(), EMPTY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reading data
        Map<String, Integer> library = CSVReader.readLibrary(LIBRARY);

        // todo: creating transition matrix
        double[][] matrix = new double[library.size()][library.size()];
        for (String fromLink : linksMap.keySet()) {
            int indexFrom = library.get(fromLink);
            Set<String> linksToSet = linksMap.get(fromLink);
            double val = 1 / linksToSet.size();

            for (String toLink : linksToSet) {
                int indexTo = library.get(toLink);
                matrix[indexFrom][indexTo] = val;
            }
        }

        // todo: writing transition matrix m to csv file
        try {
            CSVWriter.writeMatrix(matrix, MATRIX);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating v0 matrix
        double[][] v0 = new double[library.size()][1];
        double init = 1 / library.size();
        for (int i = 0; i < v0.length; i++) {
            v0[i][0] = init;
        }

        // todo: multiplying v0 * m multiple times to calculate pagerank

        // todo: write pagerank to site

    }
}
