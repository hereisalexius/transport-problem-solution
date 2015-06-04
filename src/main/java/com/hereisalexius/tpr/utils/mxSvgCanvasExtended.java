/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.utils;

import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *
 * @author hereisalexius
 */
public class mxSvgCanvasExtended extends mxSvgCanvas {

    public mxSvgCanvasExtended(Document document) {
        super(document);
    }
    
    

    @Override
    public Element drawShape(int x, int y, int w, int h,
            Map<String, Object> style) {
         //... 

        // Draws the shape
        String shape = mxUtils.getString(style, mxConstants.STYLE_SHAPE, "");
        String cellid = mxUtils.getString(style, "cellid", "");
        Element elem = null;
         // ... 

        // e.g. if image, add the cell id 
        if (shape.equals(mxConstants.SHAPE_IMAGE)) {
            String img = getImageForStyle(style);
            if (img != null) {
                // Vertical and horizontal image flipping
                boolean flipH = mxUtils.isTrue(style,
                        mxConstants.STYLE_IMAGE_FLIPH, false);
                boolean flipV = mxUtils.isTrue(style,
                        mxConstants.STYLE_IMAGE_FLIPV, false);
                elem = createImageElement(x, y, w, h, img,
                        PRESERVE_IMAGE_ASPECT, flipH, flipV, isEmbedded());
                /* so here we are */
                // add the cell id atribute 
                if (!cellid.equals("")) {
                    elem.setAttribute("id", cellid);
                }
            }
        } else if (shape.equals(mxConstants.SHAPE_LINE)) // ... 
        {
            
        }
        return elem;
    }// end drawShape

}
