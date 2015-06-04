/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.trash;

import java.util.*;

/**
 *
 * @author hereisalexius
 */
public class Tag {

    private String tagName;
    private String tagValue;
    private Map<String, String> tagAttributeMap;

    public Tag(String tagName, String tagValue, Map<String, String> tagAttributeMap) {
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.tagAttributeMap = tagAttributeMap;
    }

    public Tag(String tagName, String tagValue) {

        this.tagName = tagName;
        this.tagValue = tagValue;
        this.tagAttributeMap = new HashMap<>();
        this.tagAttributeMap.put("", "");
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Map<String, String> getTagAttributeMap() {
        return tagAttributeMap;
    }

    public void setTagAttributeMap(Map<String, String> tagAttributeMap) {
        this.tagAttributeMap = tagAttributeMap;
    }

    public String getSource() {
        String src = "";
        if (!tagName.isEmpty()) {
            src = "<" + tagName + " ";

            for (Map.Entry<String, String> entrySet : tagAttributeMap.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                if (!key.isEmpty()) {
                    src += key + "=" + value + " ";
                }
            }
            src += ">" + tagValue + "</" + tagName + ">";
        }
        return src;
    }

    public static String getFromSourcesList(List<? extends Tag> cells) {
        String src = "";
        for (Tag cell : cells) {
            src += cell.getSource();
        }
        return src;
    }
}
