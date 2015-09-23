package com.vartanian.testing.cuncurrent.impl;

import com.vartanian.testing.cuncurrent.Parser;
import com.vartanian.testing.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by super on 9/23/15.
 */
public class MultiThreadParserFastImpl implements Parser {

    private final int MIN_DELAY = 100;
    private final int MAX_DELAY = 300;

    private final String site;
    private final String hrefQuery;
    private final int maxDeep;

    private ConcurrentHashMap<String, Object> concurrentHashMap;
    private Map<String, Object> parameters;

    public MultiThreadParserFastImpl(ConcurrentHashMap<String, Object> concurrentHashMap, String site, int maxDeep, String hrefQuery) {
        this.concurrentHashMap = concurrentHashMap;
        this.site = site;
        this.hrefQuery = hrefQuery;
        this.maxDeep = maxDeep;
    }

    @Override
    public void init() {

    }

    @Override
    public void run() {
        startParsing();
    }

    public void startParsing() {

        int currentLevel = 0;

        Elements links = getLinks(site);

        if (links == null && currentLevel > 0){
            currentLevel--;
        }

        for (Element element : links) {
            URL url = null;
            try {
                url = new URL(element.attr("abs:href"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String newURI = Utils.toFullForm(url, false);
            if (newURI == null){
                continue;
            }
            getRef(newURI, currentLevel);
        }

    }

    private void getRef(String currentURL, int currentLevel) {

        if (currentLevel > maxDeep) {
            if (currentLevel > 0) {
                currentLevel--;
            }
            return;
        }

        if (concurrentHashMap.contains(currentURL)) {
            if (currentLevel > 0) {
                currentLevel--;
            }
            return;
        }

        concurrentHashMap.put(currentURL, currentURL);

        String ch = "";
        for (int i = 0; i < currentLevel; i++) {
            ch = ch + "-";
        }
        System.out.println(ch + currentURL + " ||| Thread: " + Thread.currentThread().getName() + "|||| level: " + currentLevel);

        randomPause(MIN_DELAY, MAX_DELAY);


    }

    private Elements getLinks(String currentURL) {

        Document doc;
        try {
            doc = Jsoup.connect(currentURL).get();
        } catch (IOException e) {
            return null;
        }

        //get all ref on a page
        return doc.select(hrefQuery);

    }

    public void randomPause(int MIN_DELAY, int MAX_DELAY) {
        Random rnd = new Random();
        long delay = MIN_DELAY + rnd.nextInt(MAX_DELAY - MIN_DELAY);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
