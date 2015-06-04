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
public class TableTag extends Tag {

    private List<TrTag> rows;

    public TableTag(List<TrTag> rows, Map<String, String> tagAttributeMap) {
        super("table", Tag.getFromSourcesList(rows), tagAttributeMap);
        this.rows = rows;
    }
    
    public TableTag(List<TrTag> rows) {
        super("table", Tag.getFromSourcesList(rows));
        this.rows = rows;
    }

    public List<TrTag> getRows() {
        return rows;
    }

    public void setRows(List<TrTag> rows) {
        this.rows = rows;
    }
    
    

}
