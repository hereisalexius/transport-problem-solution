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
import org.rendersnake.HtmlCanvas;
import java.util.*;

/**
 *
 * @author hereisalexius
 */
public abstract class AlternativesTree {

    private TransportationProblem tp;
    private int[][] basicNorthwestMatrix;
    private List<AlternativeLayer> layerList;

    public AlternativesTree(TransportationProblem tp) {
        this.tp = tp;
        this.basicNorthwestMatrix = SteppingStone.generateNorthWestSolution(tp);
    }

    public TransportationProblem getTp() {
        return tp;
    }

    public int[][] getBasicNorthwestMatrix() {
        return basicNorthwestMatrix;
    }

    public List<AlternativeLayer> getLayerList() {
        return layerList;
    }

    public void setTp(TransportationProblem tp) {
        this.tp = tp;
    }

    public void setBasicNorthwestMatrix(int[][] basicNorthwestMatrix) {
        this.basicNorthwestMatrix = basicNorthwestMatrix;
    }

    protected void setLayerList(List<AlternativeLayer> layerList) {
        this.layerList = layerList;
    }

    protected List<AlternativeLayer> generateAlternatives(TransportationProblem tp, int[][] basicNorthwestMatrix) {
        return null;
    }

    public HtmlCanvas generateReportHTML(HtmlCanvas html, String path) throws IOException {
        for (AlternativeLayer al : layerList) {
            al.generateSolutionTableHTML(html);
        }

        return html;
    }
}
