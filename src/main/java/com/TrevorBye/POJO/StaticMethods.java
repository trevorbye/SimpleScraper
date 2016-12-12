package com.TrevorBye.POJO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaticMethods {

    //private constructor to prevent instantiation
    private StaticMethods() {
    }

    //returns list of elements for a class name that starts with a specific string
    public static List<Element> getClassListStarting(String classBeginning, String url) throws IOException{
        Document doc = Jsoup.connect(url).get();
        List<Element> elements = doc.select("[class^=" + classBeginning + "]");
        return elements;
    }

    public static Element getById(String url, String id) throws IOException{
        Document doc = Jsoup.connect(url).get();
        Element element = doc.getElementById(id);
        return doc.getElementById(id);
    }

    //pass in list of elements for the page
    public static List<String> getAllPageLinks(String scrapeUrl, Boolean abs) throws IOException{
        List<String> hrefList = new ArrayList<>();
        Document doc = Jsoup.connect(scrapeUrl).get();

        if (abs) {
            //gets full url path
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                hrefList.add(link.attr("abs:href"));
            }

        } else {
            //gets relative url path
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                hrefList.add(link.attr("href"));
            }
        }

        return hrefList;
    }

    //recursive method to convert raw Jsoup Element to a parent-child structure DomNode nested object to be
    //returned as JSON.
    public static DomNode buildDomReturn(Element parentElement) {
        DomNode domNode = new DomNode();

        if (!parentElement.className().isEmpty()) {
            domNode.setClassName(parentElement.className());
        }

        if (!parentElement.id().isEmpty()) {
            domNode.setId(parentElement.id());
        }

        if (!parentElement.ownText().isEmpty()) {
            domNode.setOwnText(parentElement.ownText());
        }

        if (!parentElement.tagName().isEmpty()) {
            domNode.setTagType(parentElement.tagName());
        }

        if (!parentElement.data().isEmpty()) {
            domNode.setScriptData(parentElement.data());
        }

        if (!parentElement.dataset().isEmpty()) {
            domNode.setDataSet(parentElement.dataset());
        }


        if (parentElement.children().size() > 0) {
            List<Element> childList = parentElement.children();
            List<DomNode> domNodeList = new ArrayList<>();

            for (Element element : childList) {
                domNodeList.add(buildDomReturn(element));
            }

            domNode.setChildren(domNodeList);
        }

        return domNode;
    }

    //simply checks if URL can be processed by Jsoup
    public static void validJsoupUrl(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();
    }

    public static List<Element> getAllElements(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.getAllElements();
    }
}
