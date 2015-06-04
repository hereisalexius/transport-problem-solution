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

import java.util.Arrays;

/**
 *
 * @author hereisalexius
 */
public class TDCriteriaS extends TDCriteria {

    public TDCriteriaS(int[][] values) {
        super(values);
    }

    @Override
    public int[][] getResult() {
        int[][] result = new int[values.length][2];
        int minEir = 0;
        int minEirId = 0;
        for (int i = 0; i < result.length; i++) {
            result[i][0] = eir(values, values[i]);

            result[i][1] = 0;
            if (result[i][0] < minEir) {
                minEir = result[i][0];
                minEirId = i;
            }
        }
        result[minEirId][1] = minEir;

        return result;
    }

    private int eir(int[][] values, int... eij) {
        int[] maxI = new int[eij.length];
        for (int j = 0; j < eij.length; j++) {
            maxI[j] = 0;
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < eij.length; j++) {
                if (maxI[j] < values[i][j]) {
                    maxI[j] = values[i][j];
                }
            }
        }

        for (int j = 0; j < eij.length; j++) {
            maxI[j] = eij[j];
        }

        Arrays.sort(maxI);

        return maxI[maxI.length - 1];
    }

}
