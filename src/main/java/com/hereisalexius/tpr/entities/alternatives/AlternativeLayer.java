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

import com.hereisalexius.tpr.decisions.TDCriteria;
import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.algo.SteppingStone;
import com.hereisalexius.tpr.entities.alternatives.certain.CertainNode;
import com.hereisalexius.tpr.entities.alternatives.certain.CertainTree;
import com.hereisalexius.tpr.entities.alternatives.probable.ProbableNode;
import com.hereisalexius.tpr.entities.alternatives.probable.ProbableTree;
import java.io.IOException;
import java.util.*;
import org.rendersnake.HtmlCanvas;
import static org.rendersnake.HtmlAttributesFactory.*;

/**
 *
 * @author hereisalexius
 */
public class AlternativeLayer {

    private List<? extends AlternativeNode> alternativeNods;
    private int[][] layerMatrix;
    private Class<? extends AlternativesTree> className;

    public AlternativeLayer(List<? extends AlternativeNode> alternativeNods, int[][] layerMatrix, Class<? extends AlternativesTree> className) {
        this.alternativeNods = alternativeNods;
        this.layerMatrix = layerMatrix;
        this.className = className;
    }

    public List<? extends AlternativeNode> getAlternativeNods() {
        return alternativeNods;
    }

    public int[][] getLayerMatrix() {
        return layerMatrix;
    }

    public HtmlCanvas generateSolutionTableHTML(HtmlCanvas html) throws IOException {
        for (AlternativeNode an : alternativeNods) {
            an.generateFullBlockHTML(html);
        }

        if (className.isAssignableFrom(ProbableTree.class)) {
            generateProbLooseMatrixHTML(html);
            if (c != null) {
                c.generateTableHTML(html);
            }
        } else {
            html.div(class_("sol_tab")).table(add("border", "2", true));
            html.tr()
                    .th().content("Таблиця рішеннь")
                    .th().content("Значення")
                    .th().content("Оптимальне рішення")
                    ._tr();

            for (AlternativeNode alternativeNode : alternativeNods) {
                CertainNode ct = (CertainNode) alternativeNode;
                html.tr();

                html.td()
                        .p().i().span().content("E")
                        .sup().content(ct.getK()).sub().content(ct.getN())._i()._p()
                        ._td();

                html.td();
                html.p().i().span().content("δ").sup().content(ct.getK()).sub().content(ct.getLocI() + "," + ct.getLocj());
                html.span().content(" = " + SteppingStone.getValue(ct.getTp(), ct.getNodeMap()));
                html._i()._p()._td();

                if (ct.isOptimal()) {
                    html.td().content("******");
                } else {
                    html.td().content(" ");
                }

                html._tr();
            }
            html._table()._div();
        }

        return html;
    }

    private HtmlCanvas generateProbLooseMatrixHTML(HtmlCanvas html) throws IOException {
        html.p().content("Матриця витрат");
        html.div(class_("loose_tab")).table(add("border", "2", true));
        html.tr()
                .th().span().content(" ")._th()
                .th().span().content("F").sub().content(1)._th()
                .th().span().content("F").sub().content(2)._th()
                .th().span().content("F").sub().content(3)._th()
                ._tr();
        html.tr();
        html.td().b().span().content("E").sup().content(1).sub().content(1)._b()._td();
        ProbableNode pn = (ProbableNode) alternativeNods.get(0);
        html.td().content(pn.getOwnerResultValues().get(0));
        html.td().content(pn.getOwnerResultValues().get(1));
        html.td().content(pn.getOwnerResultValues().get(2));
        html._tr();

        for (AlternativeNode alternativeNode : alternativeNods) {
            ProbableNode ct = (ProbableNode) alternativeNode;
            html.tr();
            html.td();
            html.b();
            ct.generateAltNameHTML(html);
            html._b();
            html._td();
            for (int v : ct.getResultValues()) {
                html.td().content(v);
            }
            html._tr();

        }

        html._table()._div();

        return html;
    }

    TDCriteria c = new TDCriteria();

    public void setCriteria(TDCriteria c) {
        this.c = c;
    }

    public TDCriteria getCriteria() {
        return c;
    }
    
    

}
