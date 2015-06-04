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
package com.hereisalexius.tpr.decisions;

import static com.hereisalexius.tpr.HTMLFormulaConstsants.G_E_IR;
import static com.hereisalexius.tpr.HTMLFormulaConstsants.HL_E_IR;
import static com.hereisalexius.tpr.HTMLFormulaConstsants.MAX_I_E_IR;
import java.io.IOException;
import java.util.Arrays;
import static org.rendersnake.HtmlAttributesFactory.class_;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class TDCriteriaHL extends TDCriteriaBL {

    public TDCriteriaHL(double... q) {
        super(q);

    }

    public TDCriteriaHL(int[][] values, double... q) {
        super(values, q);

    }

    @Override
    public int[][] getResult() {

        int[][] result = new int[values.length][10];

        for (int v = 0; v < 10; v += 2) {

            int maxEir = Integer.MIN_VALUE;
            int maxEirId = 0;
            for (int i = 0; i < result.length; i++) {
                result[i][v] = (int) ((v * 0.1) * super.getResult()[i][0] + (1 - (v * 0.1)) * minEij(values[i]));

                result[i][v + 1] = 0;
                if (result[i][v] > maxEir) {
                    maxEir = result[i][v];
                    maxEirId = i;
                }
            }
            result[maxEirId][v + 1] = maxEir;
        }
        return result;

    }

    private int minEij(int... row) {
        Arrays.sort(row);
        return row[0];
    }

    public HtmlCanvas generateTableHTML(HtmlCanvas html) throws IOException {

        html.table(class_("decision_table").add("border", "1", true));
        html.tr();
        html.th().content("");
        for (int i = 0; i < values[0].length; i++) {
            html.th().span().content("F").sub().content(i + 1)._th();
        }
        
        for (int v = 0; v < 10; v += 2) {
            String s1 = String.format(HL_E_IR + "\nv=%.1f", (v * 0.1));
            html.th().content(s1, false);
            String s2 = String.format(MAX_I_E_IR + "\nv=%.1f", (v * 0.1));
            html.th().content(s2, false);
        }
        html._tr();

        for (int i = 0; i < values.length; i++) {
            html.tr();
            html.th().content("E<sub>" + (i + 1) + "</sub>", false);
            for (int j = 0; j < values[0].length; j++) {
                html.td().content(values[i][j]);
            }

            int[][] res = getResult();

            for (int v = 0; v < 10; v += 2) {
                html.td().content(res[i][v]);
                html.td().content(res[i][v + 1]);
            }
            html._tr();
        }

        html.tr();
        html.th().content("");
        for (int i = 0; i < q.length; i++) {
            html.th().content("q<sub>" + (i + 1) + "</sub> = " + q[i], false);

        }

        html._tr();
        html._table();

        return html;
    }

}
