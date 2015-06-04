/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr;

/**
 *
 * @author hereisalexius
 */
public class HTMLFormulaConstsants {

    public static final String SUM = "&sum;";
    public static final String MULT = "П";

    public static final String E_IJ = "e<sub>ij</sub>";
    public static final String E_IR = "e<sub>ir</sub>";
    public static final String MAX_I = "max<sub>i</sub>";
    public static final String MAX_J = "max<sub>j</sub>";
    public static final String MIN_I = "min<sub>i</sub>";
    public static final String MIN_J = "min<sub>j</sub>";
    public static final String Q_J = "q<sub>j</sub>";

    public static final String MAX_I_E_IR = MAX_I + E_IR;
    public static final String MIN_I_E_IR = MIN_I + E_IR;

    public static final String DEFAULT_E_IR = E_IR + " = " + MIN_J + E_IJ + "+" + MAX_J + E_IJ;
    public static final String OPTIMISTIC_E_IR = E_IR + " = " + MAX_J + E_IJ;
    public static final String PESSIMISTIC_E_IR = E_IR + " = " + MAX_J + "(" + MIN_I + E_IJ + "+" + E_IJ + ")";
    public static final String NATURAL_E_IR = E_IR + " = 1/n " + SUM + E_IJ;

    public static final String BL_E_IR = E_IR + " = " + SUM + E_IJ + Q_J;
    public static final String G_E_IR = E_IR + " = " + MIN_J + E_IJ + Q_J;
    public static final String HL_E_IR = E_IR + " = v" + SUM + E_IJ + Q_J + "(1 - v)" + MIN_J + E_IJ;
    public static final String MULT_E_IR = E_IR + " = " + MULT + E_IJ;
}
