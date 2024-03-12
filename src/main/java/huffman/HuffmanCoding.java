package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import huffman.CharFreq;
import huffman.StdOut;
import huffman.TreeNode;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Venora Furtado
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    private CharFreq getCharacterFrequency(Character a) {
        CharFreq heeseung = null;  

        for (int i = 0; i < sortedCharFreqList.size(); i++) {
            if (sortedCharFreqList.get(i).getCharacter().equals(a)) {
                heeseung = sortedCharFreqList.get(i); 
            }
        }

        if (heeseung == null) {
            heeseung = new CharFreq();
            heeseung.setCharacter(a);
            heeseung.setProbOcc(0);
            sortedCharFreqList.add(heeseung);
        }

        return heeseung;
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
       StdIn.setFile(fileName);
       sortedCharFreqList = new ArrayList<CharFreq>();
       int count = 0;

       while (StdIn.hasNextChar()) {
           Character sunootemp;
           sunootemp = StdIn.readChar();
           CharFreq jungwon = getCharacterFrequency(sunootemp);
           jungwon.setProbOcc(jungwon.getProbOcc() + 1);
           count++;
       }

       for (int i = 0; i < sortedCharFreqList.size(); i++) {
           CharFreq jungwon = sortedCharFreqList.get(i); 
           jungwon.setProbOcc(jungwon.getProbOcc()/count);
       }

       if (sortedCharFreqList.size() == 1){
            CharFreq jungwon = sortedCharFreqList.get(0);
            int i = (int)jungwon.getCharacter();
            i++;
            jungwon = new CharFreq((char)i, 0);
            sortedCharFreqList.add(jungwon);
        }
        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
        ArrayList<TreeNode> jay = new ArrayList<TreeNode>();   
        TreeNode dequeone; 
        TreeNode dequetwo; 

        while (sortedCharFreqList.size() > 0 || jay.size() > 1) {
            if (jay.size() == 0) {
                dequeone = new TreeNode();
                dequeone.setData(sortedCharFreqList.get(0));
                sortedCharFreqList.remove(dequeone.getData());
            }
            else {
                if (sortedCharFreqList.size() > 0){
                    if (jay.get(0).getData().getProbOcc() < sortedCharFreqList.get(0).getProbOcc()) {
                        dequeone = jay.get(0);
                        jay.remove(dequeone); 
                    } else {
                        dequeone = new TreeNode();
                        dequeone.setData(sortedCharFreqList.get(0));
                        sortedCharFreqList.remove(dequeone.getData());
                    }
                } else {
                    dequeone = jay.get(0);
                    jay.remove(dequeone); 
                } 
            }
            
            if (sortedCharFreqList.size() == 0 && jay.size() == 0){
                dequetwo = null;
            } else if (jay.size() == 0) {
                dequetwo = new TreeNode();
                dequetwo.setData(sortedCharFreqList.get(0));
                sortedCharFreqList.remove(dequetwo.getData()); 
            } else {
                if (sortedCharFreqList.size() > 0) {
                    if (jay.get(0).getData().getProbOcc() < sortedCharFreqList.get(0).getProbOcc()) {
                        dequetwo = jay.get(0);
                        jay.remove(dequetwo); 
                    } else {
                        dequetwo = new TreeNode();
                        dequetwo.setData(sortedCharFreqList.get(0));
                        sortedCharFreqList.remove(dequetwo.getData());
                    }
                } else {
                    dequetwo = jay.get(0);
                    jay.remove(dequetwo);
                }
            } 
            if (dequetwo != null){
                TreeNode tree = new TreeNode();
                CharFreq enhypen = new CharFreq(); 
                enhypen.setCharacter(null); 
                enhypen.setProbOcc(dequeone.getData().getProbOcc() + dequetwo.getData().getProbOcc()); 
                tree.setLeft(dequeone);
                tree.setRight(dequetwo);
                tree.setData(enhypen);
                jay.add(tree);
            } else {
                jay.add(dequeone);
            }
        } //while
        huffmanRoot = jay.get(0);
    }

    private String RecursiveFunction(Character c, TreeNode leftnode, TreeNode rightnode, String codeString) {
        String rStr = "";
        if (leftnode != null){
            if (leftnode.getLeft() == null && leftnode.getRight() == null) {
                if (leftnode.getData().getCharacter().equals(c)) {
                    return codeString + "0";
                } 
            }
            else {
                rStr = RecursiveFunction(c, leftnode.getLeft(), leftnode.getRight(), codeString + "0");
            }
        }
        if (rStr.length() > 0 ) return rStr;
        if (rightnode != null){
            if (rightnode.getLeft() == null && rightnode.getRight() == null) {
                if (rightnode.getData().getCharacter().equals(c)) {
                    return codeString + "1";
                }
            }
            else {
                rStr = RecursiveFunction(c, rightnode.getLeft(), rightnode.getRight(), codeString + "1");
            }
        }
        
        return rStr;    
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        
        encodings = new String[128];
        
        for (int i = 0; i < 128; i++) {
            Character sunoo = (char) i;
            String s = "";
            if (huffmanRoot.getLeft() == null && huffmanRoot.getRight() == null){
                if (huffmanRoot.getData().getCharacter().equals((char)i)) s = "";
            } else {
                s = RecursiveFunction(sunoo, huffmanRoot.getLeft(), huffmanRoot.getRight(), "");
            }
            if (s.length() > 0) {
                encodings[i] = s; 
            }
        }
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        StdOut.setFile(encodedFile);
        String wonie = "";

    while (StdIn.hasNextChar()) {
        Character sunootemp;
        sunootemp = StdIn.readChar(); 
        int jungwon = (int) sunootemp;
        wonie = wonie + encodings[jungwon];
    }
    writeBitString(encodedFile, wonie);
    StdOut.close();
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    private int findEncodings(String encodedFile) {
        for (int i = 0; i < encodings.length; i++) {
            if (encodings[i] != null && encodedFile.startsWith(encodings[i])) {
                return i; 
            }
        }
        return -1;
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        
        String sunoo = readBitString(encodedFile);
        StdOut.setFile(decodedFile);
        
        while (sunoo.length() > 0) {
            int jungwon = findEncodings(sunoo);
            
            if (jungwon > -1) {
                StdOut.print((char) jungwon);
                sunoo = sunoo.substring(encodings[jungwon].length());
            }
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
