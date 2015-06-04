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
package com.hereisalexius.tpr.entities.alternatives.certain;

import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.algo.SteppingStone;
import com.hereisalexius.tpr.entities.alternatives.AlternativeLayer;
import com.hereisalexius.tpr.entities.alternatives.AlternativeNode;
import com.hereisalexius.tpr.entities.alternatives.AlternativesTree;
import com.hereisalexius.tpr.utils.AnnotationFactory;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.rendersnake.HtmlAttributesFactory.add;
import static org.rendersnake.HtmlAttributesFactory.class_;
import org.rendersnake.HtmlCanvas;
import com.hereisalexius.tpr.trash.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import static org.rendersnake.HtmlAttributesFactory.id;

/**
 *
 * @author hereisalexius
 */
public class CertainTree extends AlternativesTree {

    public CertainTree(TransportationProblem tp) {
        super(tp);
        super.setLayerList(this.generateAlternatives(super.getTp(), super.getBasicNorthwestMatrix()));
    }

    @Override
    protected List<AlternativeLayer> generateAlternatives(TransportationProblem tp, int[][] basicNorthwestMatrix) {
        List<AlternativeLayer> alternativeLayers = new ArrayList<>();

        boolean isComplete = false;
        int[][] basicMatrix = basicNorthwestMatrix;
        int[][] mapOfMin = null;
        int s, r = 0;
        int k = 1;
        do {
            int n = 1;
            List<CertainNode> alsternatveList = new ArrayList<>();
            int minimal = 0;
            s = 0;
            r = 0;
            boolean hasMin = false;
            for (int i = 0; i < basicMatrix.length; i++) {
                for (int j = 0; j < basicMatrix[0].length; j++) {
                    if (basicMatrix[i][j] == 0) {
                        int[][] map = SteppingStone.bindMap(basicMatrix, i, j);
                        int value = SteppingStone.getValue(tp, map);
                        alsternatveList.add(new CertainNode(tp, basicMatrix, map, false, k, n));
                        n++;
                        if (value < minimal) {
                            if (!hasMin) {
                                hasMin = true;
                            }
                            minimal = value;
                            s = i;
                            r = j;
                            mapOfMin = map;

                        }
                    }
                }
            }

            if (hasMin) {
                basicMatrix = SteppingStone.transformBasicMatrix(basicMatrix, mapOfMin, s, r);

                for (CertainNode alsternatve : alsternatveList) {
                    if (alsternatve.getValue() == minimal) {
                        alsternatve.setOptimal(true);
                    }
                }

            } else {
                isComplete = true;
            }

            alternativeLayers.add(new AlternativeLayer(alsternatveList, basicMatrix, CertainTree.class));
            k++;
        } while (!isComplete);

        return alternativeLayers;
    }

    @Override
    public HtmlCanvas generateReportHTML(HtmlCanvas html, String path) throws IOException {
        html.p().content("Рішення за правилом північно-західного кута:");
        html.p().content(AnnotationFactory.generateTableHeader("- Альтернативне рішення № 1"));
        this.generateBasicNWTableHTML(html);
        html.br().i().content(nwZ(), false);
        html.br();
        html.p().content("Додаткові альтернативні рішення:");

        alternativeSolutions(html, path);
        return html;
    }

    private HtmlCanvas generateBasicNWTableHTML(HtmlCanvas html) throws IOException {
        html.div(class_("result_table")).table(add("border", "2", true));
        for (int r[] : super.getBasicNorthwestMatrix()) {
            html.tr();
            for (int s : r) {
                html.td().content((s != 0) ? s + "" : " ");
            }
            html._tr();
        }
        html._table()._div();
        return html;
    }

    @Deprecated
    public String nwZ() {
        int z = 0;
        int[] z3 = {0, 0, 0};
        String result = "Z = ";
        int[][] nwM = super.getBasicNorthwestMatrix();
        int[][][] costs = super.getTp().getCosts();

        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if (nwM[i][j] > 0) {
                    if (costs[i][j].length == 1) {

                        z += (nwM[i][j] * costs[i][j][0]);
                        result += costs[i][j][0] + " * " + nwM[i][j] + " + ";

                    } else if (costs[i][j].length == 1) {

                        z3[0] += (nwM[i][j] * costs[i][j][0]);
                        z3[1] += (nwM[i][j] * costs[i][j][1]);
                        z3[2] += (nwM[i][j] * costs[i][j][2]);
                        result += "(" + costs[i][j][0] + "," + costs[i][j][1] + "," + costs[i][j][2] + ")" + " * " + nwM[i][j] + " + ";

                    }
                }
            }

        }

        result = result.substring(0, result.length() - 2) + "= " + ((costs[0][0].length == 1) ? z + "" : "(" + z3[0] + "," + z3[1] + "," + z3[2] + ")");
        System.out.println(result);
        return result;
    }

    @Deprecated
    public String nwZEditional(int[][] nwM) {
        int z = 0;
        int[] z3 = {0, 0, 0};
        String result = "Z = ";
        int[][][] costs = costs = super.getTp().getCosts();

        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if (nwM[i][j] > 0) {
                    if (costs[i][j].length == 1) {

                        z += (nwM[i][j] * costs[i][j][0]);
                        result += costs[i][j][0] + " * " + nwM[i][j] + " + ";

                    } else if (costs[i][j].length == 1) {

                        z3[0] += (nwM[i][j] * costs[i][j][0]);
                        z3[1] += (nwM[i][j] * costs[i][j][1]);
                        z3[2] += (nwM[i][j] * costs[i][j][2]);
                        result += "(" + costs[i][j][0] + "," + costs[i][j][1] + "," + costs[i][j][2] + ")" + " * " + nwM[i][j] + " + ";

                    }
                }
            }

        }

        result = result.substring(0, result.length() - 2) + "= " + ((costs[0][0].length == 1) ? z + "" : "(" + z3[0] + "," + z3[1] + "," + z3[2] + ")");

        return result;
    }

    @Deprecated
    public HtmlCanvas alternativeSolutions(HtmlCanvas html, String path) throws IOException {
        int t = 2;

        List<TableTag> tables = new ArrayList<>();
        String val = nwZ().split("=")[2];
        Map<String, String> tabAttr = new HashMap<>();
        tabAttr.put("border", "2");

        String result = "";

        List<TrTag> rows = new ArrayList<>();
        Map<String, String> onlyColorAttr = new HashMap<>();
        onlyColorAttr.put("bgcolor", "lightgray");

        List<TdTag> header = new ArrayList<>();
        header.add(new TdTag("№"));
        header.add(new TdTag("Зміст рішення"));
        header.add(new TdTag("<i>δ<sup>k</sup><sub>ij</sub></i>"));
        header.add(new TdTag("Найкраще рішення"));
        rows.add(new TrTag(header, onlyColorAttr));

        List<AlternativeLayer> alts = super.getLayerList();

        int i = 0;

        for (AlternativeLayer ai : alts) {
            boolean isNeeded = false;
            List<TdTag> cells = new ArrayList<>();

            if (i > 0) {
                String e = "E<i><sup>" + (i + 1) + "</sup><sub>" + (i + 1) + "</sub></i>";
                String tab = "Таблиця " + t;
                result += "<br><p>" + tab + " - Альтернативне рішення " + e + "</p>";
                cells.add(new TdTag(e));
                cells.add(new TdTag(tab));
                cells.add(new TdTag("-"));
                cells.add(new TdTag(""));
                result += ai.getAlternativeNods().get(0).generatePathTableHTML(new HtmlCanvas()).toHtml();
                result += new Tag("p", "Величина загальних витрат в цьому випадку складатиме:").getSource();
                String nwze = nwZEditional(ai.getAlternativeNods().get(0).getNodeMatrix());
                val += "," + nwze.split("=")[2];
                result += nwze;
                rows.add(new TrTag(cells));
                cells = new ArrayList<>();
            }

            List<Integer> values = new ArrayList<>();
            List<Integer> prob = new ArrayList<>();
            int j = 1;
            for (AlternativeNode sca : ai.getAlternativeNods()) {

                String e = "E<i><sup>" + (i + 1) + "</sup><sub>" + (j + 1) + "</sub></i>";
                String tab = "Таблиця " + t + "." + (j + 1);
                result += "<p>" + tab + " - Альтернативне рішення " + e + "</p>";
                result += altExt(sca.getNodeMatrix(), sca.getNodeMap()).getSource();

                String riv = "";
                for (int k = 0; k < sca.getNodeMatrix().length; k++) {
                    for (int l = 0; l < sca.getNodeMatrix()[0].length; l++) {
                        if (sca.getNodeMap()[k][l] != 0 && sca.getNodeMatrix()[k][l] != 0) {

                            riv += "(" + (sca.getNodeMap()[k][l] * super.getTp().getCosts()[k][l][0]) + ") + ";
                        }
                    }
                }
                CertainNode cn = (CertainNode) sca;

                String sigma = "δ<sup>" + (i + 1) + "</sup><sub>ij</sub>(" + sscost(cn, super.getTp().getCosts()) + ")";
                riv = "<i> " + sigma + "= " + riv + riv.substring(0, riv.length() - 2) + "= " + cn.getValue();
                values.add(cn.getValue());
                prob.add(j);
                cells.add(new TdTag(e));
                cells.add(new TdTag(tab));
                cells.add(new TdTag(sigma));

                if (sca.isOptimal()) {
                    riv = "<b><u>" + riv + "</b></u>";
                    cells.add(new TdTag("***"));
                    if (!isNeeded) {
                        isNeeded = true;
                    }
                } else {
                    cells.add(new TdTag(""));
                }

                result += riv;

                rows.add(new TrTag(cells));
                j++;
                cells = new ArrayList<>();

            }

            // Create Chart
            Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(400).height(300).xAxisTitle("Альтернативи").yAxisTitle("Показники").build();
            chart.addSeries("δ", prob, values);

            // Customize Chart
            chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);

            BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D lGraphics2D = lBufferedImage.createGraphics();
            chart.paint(lGraphics2D);
            String imgPath = "img" + new Date().getTime() + ".png";
            ImageIO.write(lBufferedImage, "PNG", new File(imgPath));

            Map<String, String> imgAttr = new HashMap<>();
            imgAttr.put("src", path + imgPath);
            result += "<br><br>";
            result += new Tag("img", "", imgAttr).getSource();
            result += "<br>";
            result += new Tag("p", "Рис." + (i + 2) + ". Гістограма результатів" + (i + 1) + "етапу прийняття рішенню.").getSource();
            result += "<br>";

            if (isNeeded) {
                t++;
                String tab = "Таблиця " + t;
                result += "<br><p>" + tab + " - Підсумкова таблиця " + (i + 1) + "-го етапу прийняття рішенню." + "</p>";
                result += new TableTag(rows, tabAttr).getSource();
            }
            tables.add(new TableTag(rows, tabAttr));
            rows = new ArrayList<>();
            i++;
            t++;
        }
        result += new Tag("h3", "[TPSS-Log]:Оптимізацію транспортних перевезеннь успішно завершено!").getSource();
        result += "<br><br>";
        result += generateDecisionTree(new HtmlCanvas()).toHtml();
        result += new Tag("p", "Рис." + (i + 2) + ". Дерево рішень для чітких транспортних витрат.").getSource();
        html.div(id("megablock")).content(result, false);
        return html;
    }

    private int sscost(CertainNode cn, int[][][] costs) {
        int v = 0;

        for (int i = 0; i < cn.getNodeMap().length; i++) {
            for (int j = 0; j < cn.getNodeMap()[0].length; j++) {
                if (cn.getNodeMap()[i][j] == 1 && cn.getNodeMatrix()[i][j] == 0) {
                    v = costs[i][j][0];
                    break;
                }

            }
        }

        return v;
    }

    public HtmlCanvas generateDecisionTree(HtmlCanvas html) throws IOException {
        mxGraph graph = new mxGraph();

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

        try {
            Object next = null;
            int di = 1;
            for (AlternativeLayer al : super.getLayerList()) {
                int val = 0;
                for (AlternativeNode an : al.getAlternativeNods()) {
                    if (an.isOptimal()) {
                        CertainNode cn = (CertainNode) an;
                        val = cn.getValue();
                        break;
                    }
                }

                Object d = graph.insertVertex(parent, null, "D" + di + "(" + val + ")", 240, 150,
                        80, 30);
                if (next != null) {
                    graph.insertEdge(parent, null, "", next, d);
                }

                int pi = 1;
                for (AlternativeNode an : al.getAlternativeNods()) {
                    CertainNode cn = (CertainNode) an;
                    Object p = graph.insertVertex(parent, null, "P" + pi, 0, 0, 40,
                            40, "shape=ellipse");
                    graph.insertEdge(parent, null, "d" + di + "" + pi, d, p);
                    Object r = graph.insertVertex(parent, null, "Rij(" + cn.getValue() + ")", 0, 0,
                            40, 40);

                    if (cn.isOptimal()) {
                        next = r;
                    }
                    graph.insertEdge(parent, null, "", p, r);

                    pi++;
                }
                di++;
            }

        } finally {
            graph.getModel().endUpdate();
        }
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);

        layout.execute(graph.getDefaultParent());

        mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(
                graph, null, 1, null, new mxCellRenderer.CanvasFactory() {
                    @Override
                    public mxICanvas createCanvas(int width, int height) {
                        mxSvgCanvas canvas = new mxSvgCanvas(mxDomUtils.createSvgDocument(width, height));
                        canvas.setEmbedded(true);
                        return canvas;
                    }
                });
        html.div().content(mxXmlUtils.getXml(canvas.getDocument()));
        return html;
    }

    @Deprecated
    private TableTag altExt(int[][] solution, int[][] map) {
        Map<String, String> tabAttr = new HashMap<>();
        tabAttr.put("border", "2");

        List<TrTag> table = new ArrayList<>();

        int i = 0;
        for (int[] s : solution) {

            List<TdTag> inner = new ArrayList<>();

            int j = 0;
            for (int c1 : s) {
                Map<String, String> onlyColorAttr = new HashMap<>();
                if (map[i][j] != 0) {
                    onlyColorAttr.put("bgcolor", "red");
                } else {
                    onlyColorAttr.put("bgcolor", "white");
                }
                inner.add(new TdTag((map[i][j] != 0 || c1 != 0) ? c1 + ((map[i][j] != 0) ? "/" + map[i][j] : "") : " ", onlyColorAttr));

                j++;
            }
            i++;

            table.add(new TrTag(inner));

        }

        return new TableTag(table, tabAttr);
    }

}
