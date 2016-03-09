/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fastcall;

import gnu.trove.list.array.TDoubleArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Fei Lu 
 */
public class FArrayUtils {
    
    /**
     * Return the indices of unique string from input string array,
     * @param input
     * @return 
     */
    public static int[] getUniqueStringIndices (String[] input) {
        String[] unique = getUniqueStringArray(input);
        int[] index = new int[unique.length];
        boolean[] ifMatch = new boolean[index.length];
        int cnt = 0;
        for (int i = 0; i < input.length; i++) {
            int hit = Arrays.binarySearch(unique, input[i]);
            if (ifMatch[hit]) continue;
            index[cnt] = i;
            ifMatch[hit] = true;
            cnt++;
        }
        return index;
    }
    
    /**
     * Return sorted unique array of string
     * @param input
     * @return 
     */
    public static String[] getUniqueStringArray (String[] input) {
        HashSet<String> t = new HashSet();
        for (int i = 0; i < input.length; i++) {
            t.add(input[i]);
        }
        String[] result = t.toArray(new String[t.size()]);
        Arrays.sort(result);
        return result;
    }
    
    /**
     * Return the start and end index of fixed-number subsets of an array, bound[i][1] is exclusive  
     * @param arraySize
     * @param subsetNumber
     * @return 
     */
    public static int[][] getSubsetsIndicesBySubsetNumber (int arraySize, int subsetNumber) {
        if (arraySize < subsetNumber) return null;
        int remainder = arraySize % subsetNumber;
        int size = (arraySize-remainder)/subsetNumber;
        int[][] bound = new int[subsetNumber][2];
        int cnt = 0;
        for (int i = 0; i < remainder; i++) {
            bound[i][0] = cnt;
            cnt=cnt+size+1;
            bound[i][1] = cnt;
        }
        for (int i = remainder; i < subsetNumber; i++) {
            bound[i][0] = cnt;
            cnt+=size;
            bound[i][1] = cnt;
        }
        return bound;
    }
    
    /**
     * Return the start and end index of fixed-size subsets of an array, bound[i][1] is exclusive
     * @param arraySize
     * @param subsetSize
     * @return 
     */
    public static int[][] getSubsetsIndicesBySubsetSize (int arraySize, int subsetSize) {
        int remainder = arraySize%subsetSize;
        int groupNumber;
        if (remainder == 0) groupNumber = arraySize/subsetSize;
        else groupNumber = arraySize/subsetSize + 1;
        int[][] bound = new int[groupNumber][2];
        for (int i = 0; i < groupNumber; i++) {
            bound[i][0] = i*subsetSize;
            bound[i][1] = i*subsetSize + subsetSize;
            if (bound[i][1] > arraySize) bound[i][1] = arraySize;
        }
        return bound;
    }
    
    /**
     * Return a random subset of an integer array, may have redundant value
     * @param ar
     * @param size
     * @return 
     */
    public static int[] getRandomSubset (int[] ar, int size) {
        if (size > ar.length) return null;
        int[] index = getRandomIntArray (ar.length, size);
        int[] nar = new int[size];
        for (int i = 0; i < nar.length; i++) nar[i] = ar[index[i]];
        return nar;
    }
    
    /**
     * Return a random subset of a double array, may have redundant value
     * @param ar
     * @param size
     * @return 
     */
    public static double[] getRandomSubset (double[] ar, int size) {
        if (size > ar.length) return null;
        int[] index = getRandomIntArray (ar.length, size);
        double[] nar = new double[size];
        for (int i = 0; i < nar.length; i++) nar[i] = ar[index[i]];
        return nar;
    }
    
    /**
     * Generate a random integer array, plus value, may have redundant value
     * Good when maxValue is large
     * Designed to generate subset indices
     * @param maxValue
     * @param size
     * @return 
     */
    public static int[] getRandomIntArray (int maxValue, int size) {
        int[] ar = new int[size];
        for (int i = 0; i < ar.length; i++) {
            ar[i] = (int)(maxValue*Math.random());
        }
        return ar;
    }
    
    /**
     * Return a random subset of an integer array, no redundant value
     * @param ar
     * @param size
     * @return 
     */
    public static int[] getNonredundantRandomSubset (int[] ar, int size) {
        if (size > ar.length) return null;
        int[] index = getNonredundantRandomIntArray (ar.length, size);
        int[] nar = new int[size];
        for (int i = 0; i < nar.length; i++) nar[i] = ar[index[i]];
        return nar;
    }
    
    /**
     * Return a random subset of a double array, no redundant value
     * @param ar
     * @param size
     * @return 
     */
    public static double[] getNonredundantRandomSubset (double[] ar, int size) {
        if (size > ar.length) return null;
        int[] index = getNonredundantRandomIntArray (ar.length, size);
        double[] nar = new double[size];
        for (int i = 0; i < nar.length; i++) nar[i] = ar[index[i]];
        return nar;
    }
    
    /**
     * Generate a random integer array, plus value
     * Good when maxValue is small
     * Designed to generate subset indices
     * @param maxValue
     * @param size
     * @return 
     */
    public static int[] getNonredundantRandomIntArray (int maxValue, int size) {
        int[] ar = new int[maxValue];
        for (int i = 0; i < maxValue; i++) ar[i] = i;
        shuffleArray(ar);
        int[] nar = new int[size];
        for (int i = 0; i < size; i++) {
            nar[i] = ar[i];
        }
        return nar;
    }
    
    /**
     * Shuffle an int array
     * Implementing Fisher–Yates shuffle
     * @param ar 
     */
    public static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
    /**
     * Shuffle an double array
     * Implementing Fisher–Yates shuffle
     * @param ar 
     */
    public static void shuffleArray(double[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            double a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
    /**
     * Remove NaN from a double array
     * @param value
     * @return 
     */
    public static double[] removeNaN (double[] value) {
        TDoubleArrayList vList = new TDoubleArrayList();
        for (int i = 0; i < value.length; i++) {
            if (Double.isNaN(value[i])) continue;
            vList.add(value[i]);
        }
        return vList.toArray();
    }
    
    /**
     * Remove NaN from a two dimension array
     * @param value
     * @return 
     */
    public static double[][] removeNaN (double[][] value) {
        TDoubleArrayList[] vList = new TDoubleArrayList[value.length];
        for (int i = 0; i < vList.length; i++) vList[i] = new TDoubleArrayList();
        for (int i = 0; i < value[0].length; i++) {
            boolean flag = false;
            for (int j = 0; j < value.length; j++) {
                if (Double.isNaN(value[j][i])) {
                    flag = true;
                    break;
                }
            }
            if (flag) continue;
            for (int j = 0; j < vList.length; j++) {
                vList[j].add(value[j][i]);
            }
        }
        double[][] v = new double[vList.length][];
        for (int i = 0; i < v.length; i++) v[i] = vList[i].toArray();
        return v;
    }
    
    /**
     * Return an index of an array by descending order of value
     * @param array
     * @return 
     */
    public static int[] getIndexByDescendingValue (int[] array) {
        int[] index = getIndexByAscendingValue(array);
        ArrayUtils.reverse(index);
        return index;
    }
    
    /**
     * Return an index of an array by ascending order of value
     * @param array
     * @return 
     */
    public static int[] getIndexByAscendingValue (int[] array) {
        int[] inputArray = new int[array.length];
        System.arraycopy(array, 0, inputArray, 0, array.length);
        Integer[] idx = new Integer[inputArray.length];
        for( int i = 0 ; i < idx.length; i++ ) idx[i] = i;
        Arrays.sort(idx, new Comparator<Integer>() {
            public int compare(Integer i1, Integer i2) {
                return Integer.compare(inputArray[i1], inputArray[i2]);
            }
        });
        int[] index = new int[idx.length];
        for (int i = 0; i < index.length; i++) index[i] = idx[i];
        
        return index;
    }
}
