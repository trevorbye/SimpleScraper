package com.TrevorBye.POJO;

import java.util.List;
import java.util.Map;

public class DomNode {

    //jsoup tagName()
    private String tagType;

    //jsoup ownText()
    private String ownText;

    //jsoup id()
    private String id;

    //jsoup className()
    private String className;

    //jsoup data()
    private String scriptData;

    //jsoup dataset() HTML5 "data-"
    private Map<String,String> dataSet;

    private List<DomNode> children;

    public DomNode() {
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getOwnText() {
        return ownText;
    }

    public void setOwnText(String ownText) {
        this.ownText = ownText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScriptData() {
        return scriptData;
    }

    public void setScriptData(String scriptData) {
        this.scriptData = scriptData;
    }

    public Map<String, String> getDataSet() {
        return dataSet;
    }

    public void setDataSet(Map<String, String> dataSet) {
        this.dataSet = dataSet;
    }

    public List<DomNode> getChildren() {
        return children;
    }

    public void setChildren(List<DomNode> children) {
        this.children = children;
    }
}
