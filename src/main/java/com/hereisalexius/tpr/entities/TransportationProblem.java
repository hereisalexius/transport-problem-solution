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
package com.hereisalexius.tpr.entities;

import com.hereisalexius.tpr.decisions.TDCriteria;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import java.io.IOException;
import java.util.*;
import static org.rendersnake.HtmlAttributesFactory.add;
import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.id;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class TransportationProblem {

    public static final int MIN_STOCK_COUNT = 3;
    public static final int MIN_REQUIREMENT_COUNT = 3;

    public static final String minZ
            = "<table cellspacing=\"0\">\n"
            + "<tbody><tr align=\"center\">\n"
            + "<td><i>Min Z = </i></td>\n"
            + "	<td><small>&nbsp;n&nbsp;</small><br>\n"
            + "		<big><big>∑</big></big><br>\n"
            + "		<small><i>i</i><small>&nbsp;</small>=<small>&nbsp;</small>1</small></td>\n"
            + "	<td><small>&nbsp;m&nbsp;</small><br>\n"
            + "		<big><big>∑</big></big><br>\n"
            + "		<small><i>j</i><small>&nbsp;</small>=<small>&nbsp;</small>1</small></td>\n"
            + "	<td><i>c<sub>ij</sub></i><big>&nbsp;</big></td>\n"
            + "	<td><i>x<sub>ij</sub></i><big>&nbsp;</big></td>\n"
            + "</tr>\n"
            + "</tbody>"
            + "</table>";

    private Map<String, Integer> stockMap;
    private Map<String, Integer> requirementMap;
    private int[][][] costs;
    private boolean hasProbabilities;

    private List<TDCriteria> criteriaStack;

    public TransportationProblem(Map<String, Integer> stockMap, Map<String, Integer> requrementMap, int[][] costs) {
        this.stockMap = stockMap;
        this.requirementMap = requrementMap;
        this.costs = converMatrix(costs);
        this.hasProbabilities = false;
        //checkInputValues();
    }

    public TransportationProblem(Map<String, Integer> stockMap, Map<String, Integer> requrementMap, int[][][] costs, List<TDCriteria> criteriaStack) {
        this.stockMap = stockMap;
        this.requirementMap = requrementMap;
        this.costs = costs;
        this.hasProbabilities = true;
        this.criteriaStack = criteriaStack;
        //checkInputValues();
    }

    public Map<String, Integer> getStockMap() {
        return stockMap;
    }

    public Map<String, Integer> getRequirementMap() {
        return requirementMap;
    }

    public int[][][] getCosts() {
        return costs;
    }

    public boolean hasProbabilities() {
        return hasProbabilities;
    }

    public List<TDCriteria> getCriteriaStack() {
        return criteriaStack;
    }

    public void setCriteriaStack(List<TDCriteria> criteriaStack) {
        this.criteriaStack = criteriaStack;
    }

    /**
     * Reporting
     */
    public HtmlCanvas generateRelationMap(HtmlCanvas html) throws IOException {
        mxGraph relationGraph = new mxGraph();
        Object parent = relationGraph.getDefaultParent();
        int cellWidth = 80;
        int cellHeight = 60;

        relationGraph.getModel().beginUpdate();

        try {
            ArrayList<Object> stockCells = new ArrayList<>();

            for (Map.Entry<String, Integer> entrySet : stockMap.entrySet()) {
                String key = entrySet.getKey();
                Integer value = entrySet.getValue();
                stockCells.add(relationGraph.insertVertex(parent, key, key + "\n" + value, 0, 0, cellWidth, cellHeight));
            }

            ArrayList<Object> requirementCells = new ArrayList<>();

            for (Map.Entry<String, Integer> entrySet : requirementMap.entrySet()) {
                String key = entrySet.getKey();
                Integer value = entrySet.getValue();
                requirementCells.add(relationGraph.insertVertex(parent, key, key + "\n" + value, 0, 0, cellWidth, cellHeight));
            }

            for (Object stockCell : stockCells) {
                for (Object requirementCell : requirementCells) {
                    relationGraph.insertEdge(parent, null, "", stockCell, requirementCell);
                }
            }
        } finally {
            relationGraph.getModel().endUpdate();
        }

        mxHierarchicalLayout layout = new mxHierarchicalLayout(relationGraph);

        layout.execute(relationGraph.getDefaultParent());

        mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(
                relationGraph, null, 1, null, new mxCellRenderer.CanvasFactory() {
                    @Override
                    public mxICanvas createCanvas(int width, int height) {
                        mxSvgCanvas canvas = new mxSvgCanvas(mxDomUtils.createSvgDocument(width, height));
                        canvas.setEmbedded(true);
                        return canvas;
                    }
                });

        html.div(id("relation_graph")).content(mxXmlUtils.getXml(canvas.getDocument()), false);

        return html;
    }

    public HtmlCanvas generateCostsMatrixTable(HtmlCanvas html) throws IOException {

        html.div(class_("costs_tab")).table(add("border", "2", true));
        html.tr()
                .th().span().content(" ")._th();
        for (Map.Entry<String, Integer> entrySet : this.getRequirementMap().entrySet()) {
            String key = entrySet.getKey();

            html.th().content(key);
        }
        html.th().span().content(" ")._th()
                ._tr();

        int costs[][][] = this.getCosts();
        int cell = 0;
        for (Map.Entry<String, Integer> entrySet : this.getStockMap().entrySet()) {
            html.tr();
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            html.th().content(key);

            for (int i = 0; i < costs[0].length; i++) {
                if (costs[0][0].length == 1) {
                    html.td().content(costs[cell][i][0]);
                } else {
                    html.td().content("(" + costs[cell][i][0] + "," + costs[cell][i][1] + "," + costs[cell][i][2] + ")");
                }
            }

            html.th().content(value);
            html._tr();
            cell++;
        }

        html.tr()
                .th().span().content(" ")._th();
        for (Map.Entry<String, Integer> entrySet : this.getRequirementMap().entrySet()) {
            Integer v = entrySet.getValue();

            html.th().content(v);
        }
        html.th().span().content(" ")._th()
                ._tr();

        html._table()._div();
        return html;
    }

    @Deprecated
    public String generateMinZFExpression() {
        String f = "Min Z = ";

        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if (costs[0][0].length == 1) {

                    f += costs[i][j][0] + "x<sub>" + (i + 1) + "" + (j + 1) + "</sub> + ";

                } else if (costs[0][0].length == 3) {
                    f += "(" + costs[i][j][0] + "," + costs[i][j][1] + "," + costs[i][j][2] + ")" + "x<sub>" + (i + 1) + "" + (j + 1) + "</sub> + ";
                }
            }
        }

        return f.substring(0, f.length() - 3) + ",";

    }

    @Deprecated
    public String generateBordersExpressoins() {
        String f = "";
        String ic = "";
        String jc = "";

        for (int i = 0; i < costs.length; i++) {
            ic += i + 1 + ",";
            for (int j = 0; j < costs[0].length; j++) {
                f += "x<sub>" + (i + 1) + "" + (j + 1) + "</sub> + ";
                if (i == 0) {
                    jc += j + 1 + ",";
                }
            }
            f = f.substring(0, f.length() - 3) + " = " + new ArrayList<Integer>(stockMap.values()).get(i) + "<br>";
        }

        for (int i = 0; i < costs[0].length; i++) {
            for (int j = 0; j < costs.length; j++) {
                f += "x<sub>" + (i + 1) + "" + (j + 1) + "</sub> + ";
            }
            f = f.substring(0, f.length() - 3) + " = " + new ArrayList<Integer>(requirementMap.values()).get(i) + "<br>";
        }
        f += "x<sub>ij</sub><u>></u>0;i = " + ic.substring(0, ic.length() - 1) + ";j = " + jc.substring(0, jc.length() - 1) + ";";

        return f;
    }

    @Deprecated
    public String generateSumEquality() {
        String sum
                = "<table cellspacing=\"0\">\n"
                + "<tbody><tr align=\"center\">\n"
                + "<td><i>Min Z = </i></td>\n"
                + "	<td><small>&nbsp;n&nbsp;</small><br>\n"
                + "		<big><big>∑</big></big><br>\n"
                + "		<small><i>i</i><small>&nbsp;</small>=<small>&nbsp;</small>1</small></td>\n"
                + "	<td><i>N<sub>i</sub></i>+<big>&nbsp;</big></td>\n"
                + "	<td><small>&nbsp;m&nbsp;</small><br>\n"
                + "		<big><big>∑</big></big><br>\n"
                + "		<small><i>j</i><small>&nbsp;</small>=<small>&nbsp;</small>1</small></td>\n"
                + "	<td><i>S<sub>j</sub></i>; <i>";

        for (Map.Entry<String, Integer> entrySet : stockMap.entrySet()) {
            Integer value = entrySet.getValue();
            sum += value + " + ";
        }
        sum = sum.substring(0, sum.length() - 2) + "= ";

        for (Map.Entry<String, Integer> entrySet : requirementMap.entrySet()) {
            Integer value = entrySet.getValue();
            sum += value + " + ";
        }
        sum = sum.substring(0, sum.length() - 3);
        sum += "</i><big>&nbsp;</big></td>\n"
                + "</tr>\n"
                + "</tbody>"
                + "</table> ";

        int r = requirementMap.size() + stockMap.size();

        sum += "Отже, існує тільки " + (r - 1)
                + " незалежних умов, "
                + "а тому базові рішення будуть містити " + r
                + " незалежних змінних.";

        return sum;

    }

    /**
     * Utils part
     */
    public static int[][][] converMatrix(int[][] mx) {
        int[][][] newMx = new int[mx.length][mx[0].length][1];

        for (int i = 0; i < newMx.length; i++) {
            for (int j = 0; j < newMx[0].length; j++) {
                newMx[i][j][0] = mx[i][j];
            }
        }

        return newMx;
    }

    /**
     * Check values part
     */
    private void checkInputValues() {

        if (stockMap.size() != MIN_STOCK_COUNT) {
            throw new IllegalArgumentException("Stock count mast be more then 3!");
        }

        if (requirementMap.size() != MIN_REQUIREMENT_COUNT) {
            throw new IllegalArgumentException("Requirement count mast be more then 3!");
        }

        if (!checkSummary()) {
            throw new IllegalArgumentException("Requirements sum mast be equal to Stocks sum!");
        }

        if (!checkCostsCount()) {
            throw new IllegalArgumentException("Costs outer length must be == to Stocks and Requirements count!");
        }

    }

    private boolean checkSummary() {
        int stockSum = 0;
        for (Map.Entry<String, Integer> entrySet : stockMap.entrySet()) {
            Integer value = entrySet.getValue();
            stockSum += value;
        }
        int requirementSum = 0;
        for (Map.Entry<String, Integer> entrySet : requirementMap.entrySet()) {
            Integer value = entrySet.getValue();
            requirementSum += value;
        }
        return stockSum == requirementSum;
    }

    private boolean checkCostsCount() {
        return costs.length == stockMap.size() && costs[0].length == requirementMap.size();
    }

}
