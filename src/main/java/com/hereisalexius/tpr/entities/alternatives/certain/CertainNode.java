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
import com.hereisalexius.tpr.entities.alternatives.AlternativeNode;
import java.io.IOException;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class CertainNode extends AlternativeNode {

    public CertainNode(TransportationProblem tp, int[][] nodeMatrix, int[][] nodeMap, boolean isOptimal, int k, int n) {
        super(tp, nodeMatrix, nodeMap, isOptimal, k, n);
    }

    public int getValue() {
        return SteppingStone.getValue(super.getTp(), super.getNodeMap());
    }

    @Override
    public HtmlCanvas generateExpressionHTML(HtmlCanvas html) throws IOException {
        return super.generateExpressionHTML(html); //To change body of generated methods, choose Tools | Templates.
    }

}
