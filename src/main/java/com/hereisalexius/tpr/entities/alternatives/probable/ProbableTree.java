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

import com.hereisalexius.tpr.decisions.TDCriteria;
import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.algo.SteppingStone;
import com.hereisalexius.tpr.entities.alternatives.AlternativeLayer;
import com.hereisalexius.tpr.entities.alternatives.AlternativeNode;
import com.hereisalexius.tpr.entities.alternatives.AlternativesTree;
import com.hereisalexius.tpr.entities.alternatives.certain.CertainNode;
import com.hereisalexius.tpr.entities.alternatives.certain.CertainTree;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class ProbableTree extends AlternativesTree {

    public ProbableTree(TransportationProblem tp) {
        super(tp);
        super.setLayerList(generateAlternatives(super.getTp(), super.getBasicNorthwestMatrix()));
    }

    @Override
    protected List<AlternativeLayer> generateAlternatives(TransportationProblem tp, int[][] basicNorthwestMatrix) {
        List<AlternativeLayer> alternativeLayers = new ArrayList<>();
        int[][] basicMatrix = basicNorthwestMatrix;

        Iterator<TDCriteria> criterias = tp.getCriteriaStack().iterator();
        TDCriteria currentCriteria = null;

        ProbableNode oldOptimalNode = null;
        ProbableNode newOptimalNode = null;
        int k = 1;
        do {
            if (criterias.hasNext()) {
                currentCriteria = criterias.next();
            }
            oldOptimalNode = newOptimalNode;
            List<ProbableNode> alternativeNodes = getProbableNodes(tp, basicMatrix, k);
            //System.out.println(alternativeNodes.get(0).getProbableDifferance());
            newOptimalNode = getOptimalNode(alternativeNodes, currentCriteria);
            AlternativeLayer al = new AlternativeLayer(alternativeNodes, basicMatrix, ProbableTree.class);
            al.setCriteria(currentCriteria);
            alternativeLayers.add(al);
            if (newOptimalNode != null) {
                basicMatrix = newOptimalNode.getResultMatrix();
            }
            k++;
        } while (!isNewBetter(alternativeLayers));
        return alternativeLayers;
    }

    private List<ProbableNode> getProbableNodes(TransportationProblem tp, int[][] basicMatrix, int k) {
        List<ProbableNode> probableNodes = new ArrayList<>();
        int n = 2;
        for (int i = 0; i < basicMatrix.length; i++) {
            for (int j = 0; j < basicMatrix[0].length; j++) {
                if (basicMatrix[i][j] == 0) {
                    probableNodes.add(new ProbableNode(tp, basicMatrix, SteppingStone.bindMap(basicMatrix, i, j), false, k, n));
                    n++;
                }
            }
        }

        return probableNodes;
    }

    private ProbableNode getOptimalNode(List<ProbableNode> probableNodes, TDCriteria criteria) {
        ProbableNode pn = null;

        int[][] values = new int[probableNodes.size()][probableNodes.get(0).getResultValues().size()];

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j]
                        = probableNodes.get(i)
                        .getNegativeResultValues().get(j);

            }
        }

        criteria.setValues(values);

        int[][] analized = criteria.getResult();

        for (int i = 0; i < analized.length; i++) {
            if (analized[i][1] != 0) {
                pn = probableNodes.get(i);
                pn.setOptimal(true);
            }
        }

        return pn;
    }

//    private boolean isNewBetter(ProbableNode newNode, ProbableNode oldNode) {
//        return (oldNode == null) || (newNode != null && (newNode.getOptimizedValue() < oldNode.getOptimizedValue()));
//    }
    private boolean isNewBetter(List<AlternativeLayer> alternativeLayers) {
        boolean result = false;
        if (alternativeLayers.size() > 1) {
            AlternativeLayer last = alternativeLayers.get(alternativeLayers.size() - 1);
            AlternativeLayer prelast = alternativeLayers.get(alternativeLayers.size() - 2);
            System.out.println(last.getCriteria().getBestIndex()+":"+prelast.getCriteria().getBestIndex());
            result = last.getCriteria().getBestIndex()==prelast.getCriteria().getBestIndex();
        }
        return result;
    }

}
