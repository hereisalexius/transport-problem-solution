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
public class HtmlTag extends Tag {

    private List<Tag> srcs;

    public HtmlTag(List<Tag> srcs, Map<String, String> tagAttributeMap) {
        super("html", Tag.getFromSourcesList(srcs), tagAttributeMap);
    }

    public HtmlTag(List<Tag> srcs) {
        super("html", Tag.getFromSourcesList(srcs));

    }

    public List<Tag> getSrcs() {
        return srcs;
    }

    public void setSrcs(List<Tag> srcs) {
        this.srcs = srcs;
    }

    @Override
    public String getSource() {
        String src = "";
        src = "<" + super.getTagName() + " ";

        for (Map.Entry<String, String> entrySet : super.getTagAttributeMap().entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if (!key.isEmpty()) {
                src += key + "=" + value + " ";
            }
        }
        src += "><meta charset=\"UTF-8\">" + super.getTagValue() + "</" + super.getTagName() + ">";

        return src;

    }

}
