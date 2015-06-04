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
public final class TrTag extends Tag {

    private List<TdTag> cells;

    public TrTag(List<TdTag> cells, Map<String, String> tagAttributeMap) {
        super("tr", Tag.getFromSourcesList(cells), tagAttributeMap);
        this.cells = cells;
    }

    public TrTag(List<TdTag> cells) {
        super("tr", Tag.getFromSourcesList(cells));
        this.cells = cells;
    }
    
    public List<TdTag> getCells() {
        return cells;
    }

    public void setCells(List<TdTag> cells) {
        this.cells = cells;
    }

}
