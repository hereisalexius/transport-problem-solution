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
import static com.hereisalexius.tpr.HTMLFormulaConstsants.MAX_I_E_IR;
import java.io.IOException;
import java.util.Arrays;
import static org.rendersnake.HtmlAttributesFactory.class_;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author hereisalexius
 */
public class TDCriteriaHW extends TDCriteria {

    private int c;

    public TDCriteriaHW(int c, int[][] values) {
        super(values);
        if (c < 0 || c > 1) {
            throw new IllegalArgumentException("c must be 0 < c < 1");
        }
        this.c = c;

    }

    @Override
    public int[][] getResult() {
        int[][] result = new int[values.length][2];
        int maxEir = 0;
        int maxEirId = 0;
        for (int i = 0; i < result.length; i++) {
            result[i][0] = eir(values[i]);

            result[i][1] = 0;
            if (result[i][0] > maxEir) {
                maxEir = result[i][0];
                maxEirId = i;
            }
        }
        result[maxEirId][1] = maxEir;

        return result;
    }

    private int eir(int... e) {
        Arrays.sort(e);
        return c * e[0] + (1 - c) * e[e.length - 1];
    }
    
    public HtmlCanvas generateTableHTML(HtmlCanvas html) throws IOException {

        html.table(class_("decision_table").add("border", "1", true));
        html.tr();
        html.th().content("");
        for (int i = 0; i < values[0].length; i++) {
            html.th().span().content("F").sub().content(i + 1)._th();
        }
        html.th().content(G_E_IR, false);
        html.th().content(MAX_I_E_IR, false);

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
                html.td().content(res[i][v+1]);
            }
            html._tr();
        }

        html.tr();
        html.th().content("");
//        for (int i = 0; i < q.length; i++) {
//            html.th().content("q<sub>" + (i + 1) + "</sub> = " + q[i], false);
//
//        }

        html._tr();
        html._table();

        return html;
    }
    

}
