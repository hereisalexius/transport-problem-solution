/*
 * Copyright (C) 2015 Olexiy Polishchuk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.hereisalexius.tpr.entities.alternatives;

import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.algo.SteppingStone;
import java.io.IOException;
import static org.rendersnake.HtmlAttributesFactory.*;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public abstract class AlternativeNode {

    private TransportationProblem tp;
    private int[][] nodeMatrix;
    private int[][] nodeMap;
    private boolean isOptimal;
    private int k;
    private int n;
    private int locI;
    private int locj;

    public AlternativeNode(TransportationProblem tp, int[][] nodeMatrix, int[][] nodeMap, boolean isOptimal, int k, int n) {
        this.tp = tp;
        this.nodeMatrix = nodeMatrix;
        this.nodeMap = nodeMap;
        this.isOptimal = isOptimal;
        this.k = k;
        this.n = n;
        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[0].length; j++) {
                if (nodeMatrix[i][j] == 0 && nodeMap[i][j] != 0) {
                    locI = i + 1;
                    locj = j + 1;
                }
            }
        }

    }

    public TransportationProblem getTp() {
        return tp;
    }

    public int[][] getNodeMatrix() {
        return nodeMatrix;
    }

    public int[][] getNodeMap() {
        return nodeMap;
    }

    public boolean isOptimal() {
        return isOptimal;
    }

    public void setOptimal(boolean isOptimal) {
        this.isOptimal = isOptimal;
    }

    public int getK() {
        return k;
    }

    public int getN() {
        return n;
    }

    public int getLocI() {
        return locI;
    }

    public int getLocj() {
        return locj;
    }

    public HtmlCanvas generatePathTableHTML(HtmlCanvas html) throws IOException {
        html.div(class_("apath_table")).table(add("border", "2", true));

        for (int i = 0; i < nodeMatrix.length; i++) {
            html.tr();
            for (int j = 0; j < nodeMatrix[0].length; j++) {

                if (nodeMatrix[i][j] != 0) {
                    if (nodeMap[i][j] != 0) {
                        html.td(add("bgcolor", "red", true)).content(nodeMatrix[i][j] + "/" + ((nodeMap[i][j] > 0) ? "+" : "-"));
                    } else {
                        html.td().content(nodeMatrix[i][j]);
                    }
                } else if (nodeMap[i][j] != 0) {
                    html.td(add("bgcolor", "red", true)).content("/" + ((nodeMap[i][j] > 0) ? "+" : "-"));
                } else {
                    html.td().content("");
                }
            }
            html._tr();
        }

        html._table()._div();
        return html;
    }

    protected HtmlCanvas generateExpressionHTML(HtmlCanvas html) throws IOException {
        String exp = " = ";

        for (Integer v : SteppingStone.getContainedValues(tp, nodeMap)) {
            exp += "(" + v + ") + ";
        }

        exp = exp.substring(0, exp.length() - 3);
        if (isOptimal) {
            html.b();
        }
        html.p().i().span().content("δ").sup().content(k).sub().content(locI + "," + locj);
        html.span().content(exp + " = " + SteppingStone.getValue(tp, nodeMap));
        html._i()._p();
        if (isOptimal) {
            html._b();
        }
        return html;
    }

    protected HtmlCanvas generateTableTitleHTML(HtmlCanvas html) throws IOException {
        html.p().span().content("Альтернативне рішення E").sup().content(k).sub().content(n);
        html.span().content(" (На основі δ").sup().content(k).sub().content(locI + "," + locj);
        html.span().content(")");
        return html._p();
    }

    public HtmlCanvas generateFullBlockHTML(HtmlCanvas html) throws IOException {
        generateTableTitleHTML(html);
        generatePathTableHTML(html);
        generateExpressionHTML(html);
        return html;
    }
}
