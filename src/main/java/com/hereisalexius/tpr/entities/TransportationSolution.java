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

import com.hereisalexius.tpr.Run;
import com.hereisalexius.tpr.entities.alternatives.AlternativesTree;
import com.hereisalexius.tpr.entities.alternatives.AlternativesTreeFactory;
import com.hereisalexius.tpr.entities.alternatives.probable.ProbableTree;
import com.hereisalexius.tpr.utils.AnnotationFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class TransportationSolution {

    private TransportationProblem tp;
    private AlternativesTree altTree;
    private Class<? extends AlternativesTree> className;

    public TransportationSolution(TransportationProblem tp, Class<? extends AlternativesTree> className) {
        this.tp = tp;
        this.className = className;
        this.altTree = AlternativesTreeFactory.prepareTree(className, tp);
    }

    public TransportationProblem getTp() {
        return tp;
    }

    public AlternativesTree getAltTree() {
        return altTree;
    }

    public Class<? extends AlternativesTree> getClassName() {
        return className;
    }

    public void generateReportHTML(String path) {
        HtmlCanvas html = new HtmlCanvas();
        try {
            html.html().body();
            html.h1().content("Звіт з вирішення транспортної задачі");
            html.br();
            tp.generateRelationMap(html);
            html.p().content(AnnotationFactory.generateImageHeader("Структура взаємозв'язків транспортних сутностей"));
            AnnotationFactory.incrPicEpoh();

            html.br();

            html.p().content(AnnotationFactory.generateTableHeader("Матриця витрат на перевезення одиниці продукції"));
            AnnotationFactory.incrTabEpoh();
            tp.generateCostsMatrixTable(html);
            html.p().content("Цільова функція при цьому має вигляд:");
            html.i().content(tp.generateMinZFExpression(), false);
            html.p().content("тобто:");
            html.i().content(TransportationProblem.minZ, false);
            html.p().content("Для цієї задачі наступні рівняння будуть визначати обмеження:");
            html.i().content(tp.generateBordersExpressoins(), false);
            html.p().content("Разом з тим не всі вісім умов є незалежними, оскільки");
            html.p().content(tp.generateSumEquality(), false);
            html.br();
            html.br();
            html.br();
            html.br();

            altTree.generateReportHTML(html, path);

            html._body()._html();

            // System.out.println(html.toHtml());
            PrintWriter out = new PrintWriter(path + "report" + new Date().getTime() + ".html");
            out.println(html.toHtml());
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
