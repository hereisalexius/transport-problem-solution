    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.utils;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxGraph;
import java.awt.Image;

/**
 *
 * @author hereisalexius
 */
public class MyGraphComponent extends mxGraphComponent {

    Image img;

    public MyGraphComponent(mxGraph g, Image img) {
        super(g);
        this.img = img;
    }

    @Override
    public mxInteractiveCanvas createCanvas() {
        return new MyInteractiveCanvas(this, img);
    }
}
