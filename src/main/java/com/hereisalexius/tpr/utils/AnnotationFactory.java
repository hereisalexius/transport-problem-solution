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
package com.hereisalexius.tpr.utils;

/**
 *
 * @author hereisalexius
 */
public class AnnotationFactory {

    private static int tableCurrentEpoch = 1;
    private static int tableCurrentSubEpoch = 1;
    private static int tableCurrentSubSubEpoch = 1;

    private static int picCurrentEpoch = 1;
    private static int picCurrentSubEpoch = 1;
    private static int picCurrentSubSubEpoch = 1;

    public static void incrTabEpoh() {
        tableCurrentEpoch++;
        tableCurrentSubEpoch = 1;
        tableCurrentSubSubEpoch = 1;
    }

    public static void incrTabSubEpoh() {
        tableCurrentSubEpoch++;
        tableCurrentSubSubEpoch = 1;
    }

    public static void incrTabSubSubEpoh() {
        tableCurrentSubSubEpoch++;
    }

    public static void incrPicEpoh() {
        picCurrentEpoch++;
        picCurrentSubEpoch = 1;
        picCurrentSubSubEpoch = 1;
    }

    public static void incrPicSubEpoh() {
        picCurrentSubEpoch++;
        picCurrentSubSubEpoch = 1;
    }

    public static void incrPicSubSubEpoh() {
        picCurrentSubSubEpoch++;
    }

    public static String generateTableHeader(String text) {
        text = "Таблиця " + tableCurrentEpoch + "." + tableCurrentSubEpoch + "." + tableCurrentSubSubEpoch + " " + text;
        incrTabSubSubEpoh();
        return text;
    }

    public static String generateImageHeader(String text) {
        text = "Рисунок " + picCurrentEpoch + "." + picCurrentSubEpoch + "." + picCurrentSubSubEpoch + " " + text;
        incrPicSubSubEpoh();
        return text;
    }

}
