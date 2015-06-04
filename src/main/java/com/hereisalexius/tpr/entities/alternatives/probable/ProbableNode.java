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
package com.hereisalexius.tpr.entities.alternatives.probable;

import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.algo.SteppingStone;
import com.hereisalexius.tpr.entities.alternatives.AlternativeNode;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import static org.rendersnake.HtmlAttributesFactory.add;
import static org.rendersnake.HtmlAttributesFactory.class_;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class ProbableNode extends AlternativeNode {

    private int optimizedValue = Integer.MAX_VALUE;

    public ProbableNode(TransportationProblem tp, int[][] nodeMatrix, int[][] nodeMap, boolean isOptimal, int k, int n) {
        super(tp, nodeMatrix, nodeMap, isOptimal, k, n);

    }

    public int getOptimizedValue() {
        return optimizedValue;
    }

    public void setOptimizedValue(int optimizedValue) {
        this.optimizedValue = optimizedValue;
    }

    public int[][] getResultMatrix() {
        return SteppingStone.transformBasicMatrix(super.getNodeMatrix(), super.getNodeMap(), super.getLocI(), super.getLocj());
    }

    public List<Integer> getResultValues() {
        List<Integer> result = new ArrayList<>();
        List<List<Integer>> values = getExspressionValues();

        for (int i = 0; i < values.get(0).size(); i++) {
            int currentValue = 0;
            for (int j = 0; j < values.size(); j++) {
                currentValue += values.get(j).get(i);
            }
            result.add(currentValue);
        }

        return result;
    }

    public List<Integer> getNegativeResultValues() {
        List<Integer> result = new ArrayList<>();
        List<List<Integer>> values = getExspressionValues();

        for (int i = 0; i < values.get(0).size(); i++) {
            int currentValue = 0;
            for (int j = 0; j < values.size(); j++) {
                currentValue += values.get(j).get(i);
            }
            result.add(currentValue * -1);
        }

        return result;
    }

    public List<Integer> getOwnerResultValues() {
        List<Integer> result = new ArrayList<>();
        List<List<Integer>> values = getOwnerExspressionValues();

        for (int i = 0; i < values.get(0).size(); i++) {
            int currentValue = 0;
            for (int j = 0; j < values.size(); j++) {
                currentValue += values.get(j).get(i);
            }
            result.add(currentValue);
        }

        return result;
    }

    public List<List<Integer>> getExspressionValues() {
        int[][][] costs = super.getTp().getCosts();
        int[][] resultMatrix = this.getResultMatrix();

        List<List<Integer>> expValList = new ArrayList<>();

        for (int i = 0; i < costs.length; i++) {
            List<Integer> values = null;
            for (int j = 0; j < costs[0].length; j++) {

                if (resultMatrix[i][j] != 0) {
                    values = new ArrayList<>();
                    for (int k = 0; k < costs[0][0].length; k++) {
                        values.add(resultMatrix[i][j] * costs[i][j][k]);
                    }
                    expValList.add(values);
                }

            }

        }

        return expValList;
    }

    public List<List<Integer>> getOwnerExspressionValues() {
        int[][][] costs = super.getTp().getCosts();
        int[][] resultMatrix = this.getNodeMatrix();

        List<List<Integer>> expValList = new ArrayList<>();

        for (int i = 0; i < costs.length; i++) {
            List<Integer> values = null;
            for (int j = 0; j < costs[0].length; j++) {

                if (resultMatrix[i][j] != 0) {
                    values = new ArrayList<>();
                    for (int k = 0; k < costs[0][0].length; k++) {
                        values.add(resultMatrix[i][j] * costs[i][j][k]);
                    }
                    expValList.add(values);
                }

            }

        }

        return expValList;
    }

    public int getProbableDifferance() {
        return (getResultValues().get(0) + (2 * getResultValues().get(1)) + getResultValues().get(2)) / 2;
    }

    @Override
    public HtmlCanvas generateFullBlockHTML(HtmlCanvas html) throws IOException {
        super.generateTableTitleHTML(html);
        generatePathTableHTML(html);
        generateResultsHeaderHTML(html);
        generateResultTableHTML(html);
        generateExpressionHTML(html);
        generateChartHTML(html);
        return html;
    }

    @Override
    public HtmlCanvas generatePathTableHTML(HtmlCanvas html) throws IOException {
        html.div(class_("apath_table")).table(add("border", "2", true));

        for (int i = 0; i < super.getNodeMatrix().length; i++) {
            html.tr();
            for (int j = 0; j < super.getNodeMatrix()[0].length; j++) {

                if (super.getNodeMatrix()[i][j] != 0) {
                    if (super.getNodeMap()[i][j] != 0) {
                        html.td(add("bgcolor", "red", true)).content(super.getNodeMatrix()[i][j] + "/" + ((super.getNodeMap()[i][j] > 0) ? "+" : "-"));
                    } else {
                        html.td().content(super.getNodeMatrix()[i][j]);
                    }
                } else if (super.getNodeMap()[i][j] != 0) {
                    html.td(add("bgcolor", "red", true)).content("/" + ((super.getNodeMap()[i][j] > 0) ? "+" : "-"));
                } else {
                    html.td().content("");
                }
            }
            html._tr();
        }

        html._table()._div();
        return html;
    }

    private HtmlCanvas generateResultTableHTML(HtmlCanvas html) throws IOException {
        html.div(class_("result_table")).table(add("border", "2", true));
        for (int r[] : getResultMatrix()) {
            html.tr();
            for (int s : r) {
                html.td().content((s != 0) ? s + "" : " ");
            }
            html._tr();
        }
        html._table()._div();
        return html;
    }

    @Override
    protected HtmlCanvas generateExpressionHTML(HtmlCanvas html) throws IOException {
        String exp1 = "";
        String exp2 = "";
        String result = "";

        int[][][] costs = super.getTp().getCosts();
        int[][] basicMatrix = getResultMatrix();
        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if (basicMatrix[i][j] != 0) {
                    exp1 += "(" + basicMatrix[i][j] + ")(" + costs[i][j][0] + "," + costs[i][j][1] + "," + costs[i][j][2] + ")(+)";
                }
            }
        }
        exp1 = exp1.substring(0, exp1.length() - 3);

        List<List<Integer>> values = getExspressionValues();

        for (int i = 0; i < values.size(); i++) {
            exp2 += "(";
            for (int j = 0; j < values.get(0).size(); j++) {
                exp2 += values.get(i).get(j) + ",";
            }
            exp2 = exp2.substring(0, exp2.length() - 1);
            exp2 += ")(+)";
        }
        exp2 = exp2.substring(0, exp2.length() - 3);

        result = "(" + getResultValues().get(0) + "," + getResultValues().get(1) + "," + getResultValues().get(2) + ")";

        html.p().i();
        html.span().content("Z").sup().content(super.getK()).sub().content(super.getLocI() + "," + super.getLocj());
        html.span().content(" = " + exp1);
        html.span().content(" = " + exp2);
        html.span().content(" = " + result);
        html._i()._p();
        return html;
    }

    private HtmlCanvas generateResultsHeaderHTML(HtmlCanvas html) throws IOException {
        html.p().span().content("Альтернативне рішення E").sup().content(super.getK()).sub().content(super.getN());
        html.span().content(" (Для розрахунку Z").sup().content(super.getK()).sub().content(super.getLocI() + "," + super.getLocj());
        html.span().content(")");
        return html._p();
    }

    private HtmlCanvas generateChartHTML(HtmlCanvas html) throws IOException {

        Chart chart = new ChartBuilder().chartType(ChartType.Area).width(600).height(400).title("Нечіткі множини").xAxisTitle("X").yAxisTitle("Y").build();
        List<Integer> own = this.getOwnerResultValues();
        List<Integer> res = this.getResultValues();

        chart.addSeries("Z(1,1)", new double[]{own.get(0), own.get(1), own.get(2)}, new double[]{0, 1, 0});
        chart.addSeries("Z(" + super.getK() + "/" + super.getLocI() + "," + super.getLocj() + ")", new double[]{res.get(0), res.get(1), res.get(2)}, new double[]{0, 1, 0});

        chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
        chart.getStyleManager().setAxisTitlesVisible(false);

        BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D lGraphics2D = lBufferedImage.createGraphics();
        chart.paint(lGraphics2D);
        String imgPath = "img" + new Date().getTime() + ".png";
        ImageIO.write(lBufferedImage, "PNG", new File("C:\\Users\\hereisalexius\\Desktop\\test\\"+imgPath));

        html.img(add("src", imgPath, true));

        return html;
    }

    public HtmlCanvas generateAltNameHTML(HtmlCanvas html) throws IOException {
        html.p().span().content("E").sup().content(super.getK()).sub().content(super.getN());
        return html._p();
    }

    public HtmlCanvas generateOwnerAlternativeMatrixHTML(HtmlCanvas html) throws IOException {
        html.div(class_("first_table")).table(add("border", "2", true));
        for (int r[] : super.getNodeMatrix()) {
            html.tr();
            for (int s : r) {
                html.td().content((s != 0) ? s + "" : " ");
            }
            html._tr();
        }
        html._table()._div();

        return html;
    }

    protected HtmlCanvas generateOwnerExpressionHTML(HtmlCanvas html) throws IOException {
        String exp1 = "";
        String exp2 = "";
        String result = "";
        html.p().content("Витрати на перевезення:");
        int[][][] costs = super.getTp().getCosts();
        int[][] basicMatrix = super.getNodeMatrix();
        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if (basicMatrix[i][j] != 0) {
                    exp1 += "(" + basicMatrix[i][j] + ")(" + costs[i][j][0] + "," + costs[i][j][1] + "," + costs[i][j][2] + ")(+)";
                }
            }
        }
        exp1 = exp1.substring(0, exp1.length() - 3);

        List<List<Integer>> values = getExspressionValues();

        for (int i = 0; i < values.size(); i++) {
            exp2 += "(";
            for (int j = 0; j < values.get(0).size(); j++) {
                exp2 += values.get(i).get(j) + ",";
            }
            exp2 = exp2.substring(0, exp2.length() - 1);
            exp2 += ")(+)";
        }
        exp2 = exp2.substring(0, exp2.length() - 3);

        result = "(" + getResultValues().get(0) + "," + getResultValues().get(1) + "," + getResultValues().get(2) + ")";

        html.p().i();
        html.span().content("Z").sup().content(super.getK()).sub().content(super.getLocI() + "," + super.getLocj());
        html.span().content(" = " + exp1);
        html.span().content(" = " + exp2);
        html.span().content(" = " + result);
        html._i()._p();
        return html;
    }
}
