/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package fastcall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Holding FastA format sequence, providing functions of sorting, searching and collecting statistics
 * @author Fei Lu 
 */
public class Fasta {
    
    public FastaRecord[] records = null;
    /**0 means sorting by ID, 1 means by name, 2 means sorting by length based on ascending order, 3 means sorting by length based on descending order*/
    /**4 means sorting by name value, this is useful when chromosomes are not ordered by number. For example ( 1,10,2,3...)*/
    private int sortType = -1;
    
    public Fasta (String[] names, String[] seqs, int[] ids) {
        records = new FastaRecord[names.length];
        for (int i = 0; i < names.length; i++) {
            records[i] = new FastaRecord(names[i], seqs[i], ids[i]);
        }
        sortType = 0;
    }
    
    public Fasta (String infileS) {
        this.readFasta(infileS);
    }
    
    public Fasta (FastaRecord[] records) {
        this.records = records;
    }
    
    public Fasta () {}
    
    public Fasta getMergedFasta (Fasta another) {
        if (this.records == null) {
            return another;
        }
        FastaRecord[] nr = new FastaRecord[this.getSeqNumber()+another.getSeqNumber()];
        for (int i = 0; i < this.getSeqNumber(); i++) {
            nr[i] = this.records[i];
        }
        for (int i = 0; i < another.getSeqNumber(); i++) {
            nr[i+this.getSeqNumber()] = another.records[i];
        }
        return new Fasta(nr);
    }
    
    /**
     * Return N50 statistic
     * @return 
     */
    public int getN50 () {
        if (sortType != 3) this.sortRecordByLengthDescending();
        long sum = this.getTotalSeqLength();
        long halfSum = sum/2;
        int current = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            current+=this.getSeqLength(i);
            if (current > halfSum) return i+1;
        }
        return -1;
    }
    
    /**
     * Return L50 statistic
     * @return 
     */
    public int getL50 () {
        if (sortType != 3) this.sortRecordByLengthDescending();
        long sum = this.getTotalSeqLength();
        long halfSum = sum/2;
        int current = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            current+=this.getSeqLength(i);
            if (current > halfSum) return this.getSeqLength(i);
        }
        return -1;
    }
    
    /**
     * Return total sequence length in bp
     * @return 
     */
    public long getTotalSeqLength () {
        long sum = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            sum+=this.getSeqLength(i);
        }
        return sum;
    }
    
    /**
     * Return number of sequences
     * @return 
     */
    public int getSeqNumber () {
        return records.length;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public int getIndex (String name) {
        if (this.sortType!=1) {
            System.out.println("Please sort fasta by name first, program stop");
            System.exit(0);
        }
        return Arrays.binarySearch(records, new FastaRecord(name,null,-1));
    }
    
    /**
     * Return sequence length in bp
     * @param index
     * @return 
     */
    public int getSeqLength (int index) {
        return records[index].seq.length();
    }
    
    /**
     * Return all of the sequence names
     * @return 
     */
    public String[] getNames () {
        String[] names = new String[this.getSeqNumber()];
        for (int i = 0; i < names.length; i++) names[i] = this.getName(i);
        return names;
    }
    
    /**
     * Return sequence name
     * @param index
     * @return 
     */
    public String getName (int index) {
        return records[index].name;
    }
    
    /**
     * Return sequence
     * @param index
     * @return 
     */
    public String getSeq (int index) {
        return records[index].seq;
    }
    
    /**
     * Set sequence name
     * @param newName
     * @param index 
     */
    public void setName (String newName, int index) {
        records[index].name = newName;
    }
    
    /**
     * Read fasta file without sorting
     * @param infileS 
     */
    public void readFasta(String infileS) {
        ArrayList<FastaRecord> al = new ArrayList();
        try {
            BufferedReader br = null;
            if (infileS.endsWith("gz")) br = IoUtils.getTextGzipReader(infileS);
            else br = new BufferedReader(new FileReader(infileS), 65536);
            String temp = null, name = null, seq = null;
            StringBuilder sb = new StringBuilder();
            FastaRecord fr;
            boolean first = true;
            int cnt = 1;
            while ((temp = br.readLine()) != null) {
                if (temp.startsWith(">")) {
                    if (first == false) {
                        seq = sb.toString();
                        fr = new FastaRecord(name, seq, cnt);
                        al.add(fr);
                        sb = new StringBuilder();
                        if (cnt%1000000 == 0) {
                            System.out.println("Read "+String.valueOf(cnt)+" sequences");
                        }
                        cnt++;
                    }
                    name = temp.substring(1, temp.length());
                    first = false;
                }
                else {
                    sb.append(temp);
                }
            }
            if (!name.equals("")) {
                seq = sb.toString();
                fr = new FastaRecord(name, seq, cnt);
                al.add(fr);
            }
            records = al.toArray(new FastaRecord[al.size()]);
            sortType = 0;
            System.out.println(records.length + " sequences in the file " + infileS);
        }
        catch (Exception e) {
            System.out.println("Error while reading " + infileS);
        }
    }
    
    /**
     * Write fasta file from selected sequences 
     * @param outfileS
     * @param out 
     */
    public void writeFasta (String outfileS, boolean[] out) {
        int cnt = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfileS), 65536);
            for (int i = 0; i < records.length; i++) {
                if (!out[i]) continue;
                bw.write(">"+records[i].name);
                bw.newLine();
                bw.write(FStringUtils.getMultiplelineString(60, records[i].seq));
                bw.newLine();
                cnt++;
            }
            bw.flush();
            bw.close();
            System.out.println(cnt+ " sequences are written in " + outfileS);
        }
        catch (Exception e) {
            System.out.println("Error while writing "+ outfileS);
        }
    }
    
    /**
     * Write fasta file
     * @param outfileS 
     */
    public void writeFasta (String outfileS) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfileS), 65536);
            for (int i = 0; i < records.length; i++) {
                bw.write(">"+records[i].name);
                bw.newLine();
                bw.write(FStringUtils.getMultiplelineString(60, records[i].seq));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            System.out.println(records.length+ " sequences are written in " + outfileS);
        }
        catch (Exception e) {
            System.out.println("Error while writing "+ outfileS);
        }
    }
    
    public void writeFastq (String outfileS) {
        String defaultQualityS = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        try {
            BufferedWriter bw = new BufferedWriter (new FileWriter(outfileS), 65536);
            for (int i = 0; i < this.getSeqNumber(); i++) {
                bw.write("@"+this.getName(i));
                bw.newLine();
                bw.write(this.getSeq(i));
                bw.newLine();
                bw.write("+");
                bw.newLine();
                bw.write(defaultQualityS.substring(0, this.getSeqLength(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Write length of each sequence
     * @param outfileS 
     */
    public void writeLength (String outfileS) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfileS), 65536);
            bw.write("Name\tLength");
            bw.newLine();
            for (int i = 0; i < this.getSeqNumber(); i++) {
                bw.write(this.getName(i)+"\t"+String.valueOf(this.getSeqLength(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sortRecordByNameValue () {
        Arrays.sort (records, new sortByNameValue());
        sortType = 4;
    }
    
    public void sortRecordByName () {
        Arrays.sort (records, new sortByName());
        sortType = 1;
    }
    
    public void sortRecordByID () {
        Arrays.sort (records, new sortByID());
        sortType = 0;
    }
    
    public void sortRecordByLengthAscending () {
        Arrays.sort(records, new sortByLengthAscending());
        sortType = 2;
    }
    
    public void sortRecordByLengthDescending () {
        Arrays.sort(records, new sortByLengthDescending());
        sortType = 3;
    }
    
    private class FastaRecord implements Comparable <FastaRecord> {
        int id;
        String name;
        String seq;
        FastaRecord (String name, String seq, int id) {
            this.name = name;
            this.seq = seq;
            this.id = id;
        }
        
        @Override
        public int compareTo(FastaRecord o) {
            if (sortType == 0) {
                return id - o.id;
            }
            else if (sortType == 1) {
                return name.compareTo(o.name);
            }
            else if (sortType == 2) {
                return seq.length()-o.seq.length();
            }
            else if (sortType == 3) {
                return o.seq.length() - seq.length();
            }
            else if (sortType == 4) {
                return Integer.valueOf(name) - Integer.valueOf(o.name);
            }
            else {
                System.out.println("Unvalided sortType value in fasta. Program stops. Please debug");
                System.exit(1); 
            }
            return -1;
	}
    }
    
    private class sortByID implements Comparator <FastaRecord> {
        @Override
        public int compare(FastaRecord o1, FastaRecord o2) {
            return o1.id - o2.id;
        }
    }
    
    private class sortByNameValue implements Comparator <FastaRecord> {
        @Override
        public int compare (FastaRecord o1, FastaRecord o2) {
            int v1 = Integer.valueOf(o1.name);
            int v2 = Integer.valueOf(o2.name);
            if (v1< v2) return -1;
            else if (v1 == v2) return 0;
            return 1;
        }
    }
    
    private class sortByName implements Comparator <FastaRecord> {
        @Override
        public int compare (FastaRecord o1, FastaRecord o2) {
            return o1.name.compareTo(o2.name);
        }
    }
    
    private class sortByLengthAscending implements Comparator <FastaRecord> {
        @Override
        public int compare (FastaRecord o1, FastaRecord o2) {
            return o1.seq.length()-o2.seq.length();
        }
    }
    
    private class sortByLengthDescending implements Comparator <FastaRecord> {
        @Override
        public int compare (FastaRecord o1, FastaRecord o2) {
            return o2.seq.length()-o1.seq.length();
        }
    }
}
