/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hereisalexius.tpr.entities.algo;

import com.hereisalexius.tpr.entities.TransportationProblem;
import java.util.*;

/**
 *
 * @author hereisalexius
 */
public class SteppingStone {

//    public static TransportationSolution solve(TransportationProblem problem) {
//        return new Algorithm().solve(problem);
//    }
    public static int[][] generateNorthWestSolution(TransportationProblem problem) {
        List<Integer> stockList = new ArrayList<>(problem.getStockMap().values());
        List<Integer> requirementList = new ArrayList<>(problem.getRequirementMap().values());
        int[][] nwMatrix = new int[stockList.size()][requirementList.size()];
        //fill matrix by 0s
        for (int i = 0; i < nwMatrix.length; i++) {
            for (int j = 0; j < nwMatrix[0].length; j++) {
                nwMatrix[i][j] = 0;
            }
        }

        int leftValue = 0;
        int outPosition = 0;

        for (int i = 0; i < nwMatrix.length; i++) {
            int currentStock = stockList.get(i);

            if (leftValue > 0) {
                nwMatrix[i][outPosition] = leftValue;
                currentStock -= leftValue;
                leftValue = 0;
            }

            if (i > 0) {
                outPosition++;
            }

            for (int j = outPosition; j < nwMatrix[0].length; j++) {
                if (currentStock > 0) {
                    if (currentStock >= requirementList.get(j)) {
                        nwMatrix[i][j] = requirementList.get(j);
                        currentStock -= requirementList.get(j);
                    } else {
                        leftValue = Math.abs(currentStock - requirementList.get(j));
                        currentStock = 0;
                        nwMatrix[i][j] = Math.abs(requirementList.get(j) - leftValue);
                        outPosition = j;
                    }

                }
            }
        }

        return nwMatrix;
    }

    public static int fillArray(int[][] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = value;
            }
        }
        return value;
    }

    public static int[][] bindMap(int[][] basicSolution, Integer voidStock, Integer voidRequire) {
        int[][] bindedMap = new int[basicSolution.length][basicSolution[0].length];
        fillArray(bindedMap, 0);

        bindedMap[voidStock][voidRequire] = 1;
        //fill all by +/-
        for (int i = 0; i < bindedMap.length; i++) {
            for (int j = 0; j < bindedMap[0].length; j++) {
                if (basicSolution[i][j] > 0) {
                    if (i == voidStock || j == voidRequire) {
                        bindedMap[i][j] = -1;
                    } else {
                        bindedMap[i][j] = 1;
                    }
                }
            }

        }

        findCycle(bindedMap, voidStock, voidRequire);

        return bindedMap;
    }

    private static void findCycle(int[][] map, Integer voidStock, Integer voidRequire) {
        for (int clear = 0; clear < 3; clear++) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {

                    boolean oneMoreTime = true;

                    if (map[i][j] != 0) {

                        while (oneMoreTime) {

                            boolean isRowOposite = hasOpositeInRow(map, i, j);
                            boolean isColumnOposite = hasOpositeInColumn(map, i, j);
                            boolean isRowSame = hasSameInRow(map, i, j);
                            boolean isColumnSame = hasSameInColumn(map, i, j);

                            if (!isColumnSame && !isColumnOposite) {
                                map[i][j] = 0;
                            }

                            if (!isRowSame && !isRowOposite) {
                                map[i][j] = 0;
                            }

                            oneMoreTime = false;

                            if (isColumnSame && isRowSame && !isColumnOposite && !isRowOposite) {
                                oneMoreTime = true;
                                map[i][j] *= -1;
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean hasOpositeInRow(int[][] map, int i, int j) {
        boolean result = false;
        for (int l = 0; l < map[0].length; l++) {
            if (l != j) {
                result = map[i][l] != 0 && map[i][l] == map[i][j] * -1;
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    private static boolean hasOpositeInColumn(int[][] map, int i, int j) {
        boolean result = false;
        for (int l = 0; l < map.length; l++) {
            if (l != i) {
                result = map[l][j] != 0 && map[l][j] == map[i][j] * -1;
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    private static boolean hasSameInRow(int[][] map, int i, int j) {
        boolean result = false;
        for (int l = 0; l < map[0].length; l++) {
            if (l != j) {
                result = map[i][l] != 0 && map[i][l] == map[i][j];
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    private static boolean hasSameInColumn(int[][] map, int i, int j) {
        boolean result = false;
        for (int l = 0; l < map.length; l++) {
            if (l != i) {
                result = map[l][j] != 0 && map[l][j] == map[i][j];
            }
            if (result) {
                break;
            }
        }
        return result;
    }

//    private static List<List<SingleCostAlternative>> getSingleCostAlternatives(Integer[][] nwMatrix, TransportationProblem problem) {
//        List<List<SingleCostAlternative>> epochList = new ArrayList<>();
//
//        boolean isComplete = false;
//        Integer[][] basicMatrix = nwMatrix;
//        Integer[][] mapOfMin = null;
//        int s, r = 0;
//        int sran = 0;
//        do {
//
//            List<SingleCostAlternative> alsternatveList = new ArrayList<>();
//            int minimal = 0;
//            s = 0;
//            r = 0;
//            boolean hasMin = false;
//            for (int i = 0; i < nwMatrix.length; i++) {
//                for (int j = 0; j < nwMatrix[0].length; j++) {
//                    if (basicMatrix[i][j] == 0) {
//                        Integer[][] map = bindMap(basicMatrix, i, j);
//                        int value = getValue(problem, map);
//                        alsternatveList.add(new SingleCostAlternative(value, getContainedValues(problem, map), basicMatrix, map));
//                        if (value < minimal) {
//                            if (!hasMin) {
//                                hasMin = true;
//                            }
//                            minimal = value;
//                            s = i;
//                            r = j;
//                            mapOfMin = map;
//
//                        }
//                    }
//                }
//            }
//
//            if (hasMin) {
//                basicMatrix = transformBasicMatrix(basicMatrix, mapOfMin, s, r);
//
//                for (SingleCostAlternative alsternatve : alsternatveList) {
//                    if (alsternatve.getValue() == minimal) {
//                        alsternatve.setMinimal(true);
//                    }
//                }
//
//            } else {
//                isComplete = true;
//            }
//
//            epochList.add(alsternatveList);
//            sran++;
//        } while (!isComplete);
//
//        return epochList;
//    }

//    private static List<ProbEpoch> getProbabCostAlternatives(Integer[][] nwMatrix, TransportationProblem problem) {
//        List<ProbEpoch> epochList = new ArrayList<>();
//
//        int phaseCounter = 0;
//
//        return epochList;
//    }

    public static int getValue(TransportationProblem problem, int[][] map) {
        int value = 0;
        List<Integer> values = getContainedValues(problem, map);
        for (Integer value1 : values) {
            value += value1;
        }
        return value;
    }

    public static List<Integer> getContainedValues(TransportationProblem problem, int[][] map) {

        List<Integer> values = new ArrayList<>();
        int[][][] c = problem.getCosts();
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                if (c[i][j][0] * map[i][j] != 0) {
                    values.add(c[i][j][0] * map[i][j]);
                }
            }
        }
        return values;
    }

    public static int[][] transformBasicMatrix(int[][] matrix, int[][] map, int s, int r) {
        int[][] newMatrix = new int[matrix.length][matrix[0].length];
        fillArray(newMatrix, 0);

        int min = Integer.MAX_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] < min && map[i][j] < 0) {
                    min = matrix[i][j];
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                newMatrix[i][j] = matrix[i][j] + min * map[i][j];
            }
        }

        return newMatrix;
    }

//    protected static class Algorithm implements Solveable {
//
//        @Override
//        public TransportationSolution solve(TransportationProblem problem) {
//            Integer[][] nwMatrix = generateNorthWestSolution(problem);
//            //Run.printArray(nwMatrix);
//            List<List<SingleCostAlternative>> scaList = null;
//
//            if (problem.getCosts()[0][0].length < 2) {
//                scaList = getSingleCostAlternatives(nwMatrix, problem);
//            } else {
//
//            }
//
//            return new TransportationSolution(problem, nwMatrix, scaList);
//        }
//
//    }
}
