package com.ang.Utils;

import java.io.*;

import com.ang.Materials.Material;
import com.ang.World.Mesh;

public class OBJImporter { 
    public Mesh loadOBJ(Vector3 position, String path, Material mat) {
        double[][][] data;
        try {
            data = extractData(path);
        } catch (Exception e) {
            System.out.println("Could not load OBJ from "+path);
            return null;
        }
        
        Vector3[] vertexData = toVectorArray(data[0]);
        Vector3[] normalData = toVectorArray(data[1]);
        int[][] faceIndices = to2dIntArray(data[2]);
        int[][] normalIndices = to2dIntArray(data[3]);

        vertexData = applyOffset(vertexData, position);

        return new Mesh(vertexData, normalData, faceIndices, normalIndices, mat);
    }

    private Vector3[] applyOffset(Vector3[] data, Vector3 offset) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].add(offset);
        }

        return data;
    }

    private Vector3[] toVectorArray(double[][] data) {
        Vector3[] output = new Vector3[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = new Vector3(data[i][0], data[i][1], data[i][2]);
        }

        return output;
    }

    private int[][] to2dIntArray(double[][] data) {
        int[][] output = new int[data.length][3];
        for (int i = 0; i < data.length; i++) {
            output[i][0] = (int)data[i][0];
            output[i][1] = (int)data[i][1];
            output[i][2] = (int)data[i][2];
        }

        return output;
    }

    private double[][][] extractData(String path) throws Exception{
        BufferedReader lineCounter = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(path)));
        int vLineCount = 0; // lines storing vertex coords
        int fLineCount = 0; // lines storing verticies that make up tris
        int vnLineCount = 0; // lines storing vertex vectors to be accessed by index
        
        String st;
        while ((st = lineCounter.readLine()) != null) {
            if (st.charAt(0) == 'v' && st.charAt(1) == ' ') {
                vLineCount++;
            }
            if (st.charAt(0) == 'f') {
                fLineCount++;
            }
            if (st.charAt(0) == 'v' && st.charAt(1) == 'n') {
                vnLineCount++;
            }
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(path)));
        double[][] vLines = new double[vLineCount][3];
        double[][] vnLines = new double[vnLineCount][3];
        double[][] fLines = new double[fLineCount][3];
        double[][] nLines = new double[fLineCount][3];

        int vIndex = 0;
        int fIndex = 0;
        int vnIndex = 0;
        
        while ((st = reader.readLine()) != null) {
            if (st.charAt(0) == 'v' && st.charAt(1) == ' ') {
                String[] stSplit = st.split("\\s+");
                vLines[vIndex] = new double[]{Double.valueOf(stSplit[1]), Double.valueOf(stSplit[2]), Double.valueOf(stSplit[3])};
                vIndex++;
            }
            if (st.charAt(0) == 'f') {
                String[] stSplit = st.split("\\s+");
                int val1 = Integer.valueOf(stSplit[1].split("/")[0]);
                int val2 = Integer.valueOf(stSplit[2].split("/")[0]);
                int val3 = Integer.valueOf(stSplit[3].split("/")[0]);

                fLines[fIndex] = new double[]{val1-1, val2-1, val3-1}; // f data starts at index 1
                
                int norm1 = Integer.valueOf(stSplit[1].split("/")[2]);
                int norm2 = Integer.valueOf(stSplit[2].split("/")[2]);
                int norm3 = Integer.valueOf(stSplit[3].split("/")[2]);

                nLines[fIndex] = new double[]{norm1-1, norm2-1, norm3-1};

                fIndex++;
            }  
            if (st.charAt(0) == 'v' && st.charAt(1) == 'n') {
                String[] stSplit = st.split("\\s+");
                vnLines[vnIndex] = new double[]{Double.valueOf(stSplit[1]), Double.valueOf(stSplit[2]), Double.valueOf(stSplit[3])};
                vnIndex++;
            }
        }

        lineCounter.close();
        reader.close();

        return new double[][][]{vLines, vnLines, fLines, nLines};
    }
}