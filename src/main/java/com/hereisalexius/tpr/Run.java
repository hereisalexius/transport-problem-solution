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
package com.hereisalexius.tpr;

import com.hereisalexius.tpr.decisions.TDCriteria;
import com.hereisalexius.tpr.decisions.TDCriteriaBL;
import com.hereisalexius.tpr.decisions.TDCriteriaG;
import com.hereisalexius.tpr.decisions.TDCriteriaHL;
import com.hereisalexius.tpr.decisions.TDCriteriaMM;
import com.hereisalexius.tpr.decisions.TDCriteriaMult;
import com.hereisalexius.tpr.entities.TransportationProblem;
import com.hereisalexius.tpr.entities.TransportationSolution;
import com.hereisalexius.tpr.entities.alternatives.certain.CertainTree;
import com.hereisalexius.tpr.entities.alternatives.probable.ProbableNode;

import com.hereisalexius.tpr.entities.alternatives.probable.ProbableTree;
import static com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility.getBytes;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class Run {

    public static void main(String[] args) {

        //        System.out.println();
//
        Map<String, Integer> stockMap = new TreeMap<>();
        stockMap.put("Enterprise 1", 650);
        stockMap.put("Enterprise 2", 1000);
        stockMap.put("Enterprise 3", 950);

        Map<String, Integer> requirementMap = new TreeMap<>();
        requirementMap.put("Customer 1", 450);
        requirementMap.put("Customer 2", 400);
        requirementMap.put("Customer 3", 850);
        requirementMap.put("Customer 4", 650);
        requirementMap.put("Customer 5", 250);
//
        int[][] costValues1 = {
            {4, 1, 2, 7, 8},
            {7, 5, 3, 4, 6},
            {8, 4, 6, 2, 5}
        };
//
        int[][][] costValues = {
            {{3, 4, 6}, {1, 1, 3}, {1, 2, 4}, {4, 7, 10}, {7, 8, 10}},
            {{4, 7, 8}, {2, 5, 7}, {2, 3, 6}, {3, 4, 5}, {3, 6, 8}},
            {{5, 8, 12}, {2, 4, 5}, {4, 6, 9}, {1, 2, 4}, {4, 5, 7}}
        };
//
        List<TDCriteria> criterias = new ArrayList<>();
        criterias.add(new TDCriteriaMult());
        criterias.add(new TDCriteriaHL(0.1, 0.85, 0.05));

        TransportationProblem tp = new TransportationProblem(stockMap, requirementMap, costValues, criterias);
        
//         TransportationProblem tp = new TransportationProblem(stockMap, requirementMap, costValues1);
        
        TransportationSolution ts = new TransportationSolution(tp, ProbableTree.class);
        
//        TransportationSolution ts = new TransportationSolution(tp, CertainTree.class);


       ts.generateReportHTML("C:\\Users\\hereisalexius\\Desktop\\test\\");
//        try {
//
//            int[][] yo = {{1490, 2010, 3130},
//            {1560, 2150, 3375},
//            {1590, 2050, 3210},
//            {1130, 1890, 3130},
//            {1700, 2220, 3410},
//            {1850, 2460, 3580},
//            {1470, 2250, 3250},
//            {1670, 2640, 3940}};
//            
//            TDCriteriaHL c = new TDCriteriaHL(yo, new double[]{0.1, 0.85, 0.05});
//
//            HtmlCanvas html = new HtmlCanvas();
//            html.html().body();
//            c.generateTableHTML(html);
//            html._body()._html();
//            System.out.println(html.toHtml());
//        } catch (IOException ex) {
//            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}
