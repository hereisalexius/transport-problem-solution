/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.utils;

import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

/**
 *
 * @author hereisalexius
 */
public class MyInteractiveCanvas extends mxInteractiveCanvas {

    Image img;

    public MyInteractiveCanvas(MyGraphComponent myGraphComponent, Image img) {
        super(myGraphComponent);
        this.img = img;
    }

    /*
     * (non-Javadoc)
     * @see com.mxgraph.canvas.mxICanvas#drawCell()
     */
    public Object drawCell(mxCellState state) {
        Map<String, Object> style = state.getStyle();
        mxIShape shape = getShape(style);

        if (g != null && shape != null) {
            // Creates a temporary graphics instance for drawing this shape
            float opacity = mxUtils.getFloat(style, mxConstants.STYLE_OPACITY,
                    100);
            Graphics2D previousGraphics = g;
            g = createTemporaryGraphics(style, opacity, state);

            // Paints the shape and restores the graphics object
            shape.paintShape(this, state);

            if (((mxCell) state.getCell()).isVertex()) {
                int x = (int) (state.getCenterX() - state.getWidth() / 2);
                int y = (int) (state.getCenterY());
                previousGraphics.drawImage(img, x, y, null);
            }

            g.dispose();
            g = previousGraphics;
        }

        return shape;
    }
}
