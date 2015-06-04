/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.trash;

import java.util.Map;

/**
 *
 * @author hereisalexius
 */
public class TdTag extends Tag {

    public TdTag(String tagValue, Map<String, String> tagAttributeMap) {
        super("td", tagValue, tagAttributeMap);
    }

    public TdTag(String tagValue) {
        super("td", tagValue);
    }

}
