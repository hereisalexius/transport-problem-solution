/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.utils;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 *
 * @author hereisalexius
 */
public class mxGraphExtended extends mxGraph {

    public mxGraphExtended() {
        super();
    }

    @Override
    public void drawCell(mxICanvas canvas, Object cell) {
        // add the cell's id as a style attribute
        // cause canvas only get the style and geometry
        mxCellState state = this.getView().getState(cell);
        state.getStyle().put("cellid", ((mxCell) cell).getId());

        super.drawCell(canvas, cell);
    }

}
