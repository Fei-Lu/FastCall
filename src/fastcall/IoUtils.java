/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fastcall;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Fei Lu
 */
public class IoUtils {
    
    public static BufferedReader getTextGzipReader (String infileS) {
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(infileS))));
            br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(infileS), 65536)), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
    
    public static BufferedReader getTextGzipReader (String infileS, int bufferSize) {
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(infileS))));
            br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(infileS), bufferSize)), bufferSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
    
    public static BufferedWriter getTextGzipWriter (String outfileS) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outfileS), 65536)), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }
    
    public static BufferedWriter getTextGzipWriter (String outfileS, int bufferSize) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outfileS), bufferSize)), bufferSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }
    
    public static BufferedWriter getTextWriter (String outfileS) {
        BufferedWriter bw = null;
        try {
             bw = new BufferedWriter (new FileWriter(outfileS), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }
    
    public static BufferedReader getTextReader (String infileS) {
        BufferedReader br = null;
        try {
            br = new BufferedReader (new FileReader(infileS), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
    
    public static DataOutputStream getBinaryWriter (String outfileS) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outfileS), 65536));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dos;
    }
    
    public static DataInputStream getBinaryReader (String infileS) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(infileS), 65536));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }
    
    public static ObjectOutputStream getObjectWriter (String outfileS) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outfileS), 65536));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return oos;
    }
    
    public static ObjectInputStream getObjectReader (String infileS) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(infileS), 65536));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ois;
    }
    
    public static File[] listFilesContains (File[] fAll, String containStr) {
        ArrayList<File> al = new ArrayList();
        for (int i = 0; i < fAll.length; i++) {
            if (fAll[i].getName().contains(containStr)) al.add(fAll[i]);
        }
        return al.toArray(new File[al.size()]);
    }
    
    public static File[] listFilesStartsWith (File[] fAll, String startStr) {
        ArrayList<File> al = new ArrayList();
        for (int i = 0; i < fAll.length; i++) {
            if (fAll[i].getName().startsWith(startStr)) al.add(fAll[i]);
        }
        return al.toArray(new File[al.size()]);
    }
    
    public static File[] listFilesEndsWith (File[] fAll, String endStr) {
        ArrayList<File> al = new ArrayList();
        for (int i = 0; i < fAll.length; i++) {
            if (fAll[i].getName().endsWith(endStr)) al.add(fAll[i]);
        }
        return al.toArray(new File[al.size()]);
    }
    
    /**
     * List all the files in a directory
     * @param dir
     * @return 
     */
    public static File[] listRecursiveFiles (File dir) {
        TreeSet<File> fSet = getRecursiveFiles (dir);
        return fSet.toArray(new File[fSet.size()]);
    }
    
    private static TreeSet<File> getRecursiveFiles (File dir) {
        TreeSet<File> fileTree = new TreeSet();
        for (File entry : dir.listFiles()) {
            if (entry.isFile()) fileTree.add(entry);
            else fileTree.addAll(getRecursiveFiles(entry));
        }
        return fileTree;
    }
}
