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

import java.io.IOException;
import java.util.Arrays;
import static org.rendersnake.HtmlAttributesFactory.class_;
import org.rendersnake.HtmlCanvas;
import static com.hereisalexius.tpr.HTMLFormulaConstsants.*;

/**
 *
 * @author hereisalexius
 */
public class TDCriteria {

    public static final int DEFAULT_POSITION = 0;
    public static final int OPTIMISTIC_POSITION = 1;
    public static final int NATURAL_POSITION = 2;
    public static final int PESSIMISTIC_POSITION = 3;

    protected String eirHeader = "e<sub>ir</sub>";
    protected String cHeader = "default";

    protected int[][] values;
    private int position;

    public TDCriteria() {
        this.values = new int[][]{{0}};
        this.position = OPTIMISTIC_POSITION;
    }

    public TDCriteria(int[][] values) {
        this.values = values;
        this.position = OPTIMISTIC_POSITION;
    }

    public TDCriteria(int[][] values, int position) {
        this.values = values;
        this.position = position;
    }

    public void setValues(int[][] values) {
        this.values = values;
    }

    public int[][] getValues() {
        return values;
    }

    public int[][] getResult() {
        int[][] result = new int[this.values.length][2];
        int maxEir = 0;
        int maxEirId = 0;
        for (int i = 0; i < result.length; i++) {

            switch (position) {
                case OPTIMISTIC_POSITION:
                    result[i][0] = findEriOptimistic(values[i]);
                    break;
                case NATURAL_POSITION:
                    result[i][0] = findEriNatural(values[i]);
                    break;
                case PESSIMISTIC_POSITION:
                    result[i][0] = findEriPissimistic(values[i]);
                    break;
                default:
                    result[i][0] = findEriDefault(values[i]);
            }
            result[i][1] = 0;
            if (result[i][0] > maxEir) {
                maxEir = result[i][0];
                maxEirId = i;
            }
        }
        result[maxEirId][1] = maxEir;

        return result;
    }

    private int findEriDefault(int... row) {
        Arrays.sort(row);
        return row[row.length - 1] + row[0];
    }

    private int findEriOptimistic(int... row) {
        Arrays.sort(row);
        return row[row.length - 1];
    }

    private int findEriNatural(int... row) {
        int sum = 0;
        for (int i : row) {
            sum += i;
        }
        return sum / row.length;
    }

    private int findEriPissimistic(int... row) {
        Arrays.sort(row);
        return row[0];
    }

    public String getEirHeader() {
        return eirHeader;
    }

    public void setEirHeader(String eirHeader) {
        this.eirHeader = eirHeader;
    }

    public String getcHeader() {
        return cHeader;
    }

    public void setcHeader(String cHeader) {
        this.cHeader = cHeader;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HtmlCanvas generateTableHTML(HtmlCanvas html) throws IOException {
        html.table(class_("decision_table"));
        generateTableHeaderHTML(html);

        for (int i = 0; i < values[0].length; i++) {
            html.tr();
            html.th().content("E<sub>" + (i + 1) + "</sub>", false);
            for (int j = 0; j < values.length; j++) {
                html.td().content(values[j][i]);
            }

            int[][] res = getResult();

            html.td().content(res[i][0]);
            html.td().content(res[i][1]);

            html._tr();
        }

        html._table();
        return html;
    }

    protected HtmlCanvas generateTableHeaderHTML(HtmlCanvas html) throws IOException {

        html.tr();
        html.th().content("");
        for (int i = 0; i < values.length; i++) {
            html.th().span().content("F").sub().content(i + 1)._th();
        }

        switch (position) {
            case OPTIMISTIC_POSITION:
                html.th().content(OPTIMISTIC_E_IR, false);

                break;
            case NATURAL_POSITION:
                html.th().content(NATURAL_E_IR, false);
                break;
            case PESSIMISTIC_POSITION:
                html.th().content(PESSIMISTIC_E_IR, false);
                break;
            default:
                html.th().content(DEFAULT_E_IR, false);

        }
        html.th().content(MAX_I_E_IR, false);

        html._tr();
        return html;
    }

    public int getBestIndex() {
        int bestIndex = 0;
        int i = 0;
        for (int[] r : getResult()) {
            if (r[r.length - 1]!=0) {
                bestIndex = i;    
            }
            i++;
        }
        return bestIndex;
    }

}
