package com.ang.Util;

import java.io.*;

import com.ang.Hittable.Compound.Mesh;
import com.ang.Material.Material;

// allows for simple importing of models in .obj format
// loads verticies and vertex normals ONLY
public class OBJImporter { 
    private Vec3[]  vertexData;
    private Vec3[]  normalData;
    private int[][] vertexIndices;
    private int[][] normalIndices;

    // default position
    public Mesh loadOBJ(String path, Material mat) {
        return loadOBJ(new Vec3(0.0, 0.0, 0.0), path, mat);
    }

    // attempts to load obj
    public Mesh loadOBJ(Vec3 position, String path, Material mat) {
        try {
            extractData(path);
        } catch (Exception e) {
            System.out.println("Could not load OBJ from "+path);
            return null;
        }
        
        // allows for mesh to be centred at arbitrary position
        vertexData = applyOffset(vertexData, position);

        // passes data into mesh
        return new Mesh(vertexData, normalData, vertexIndices,
            normalIndices, mat);
    }

    // offsets all vertex positions
    private Vec3[] applyOffset(Vec3[] data, Vec3 offset) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].add(offset);
        }

        return data;
    }

    // reads data from obj file 
    private void extractData(String path) throws Exception{
        InputStream s;
        BufferedReader reader;

        s = this.getClass().getResourceAsStream(path);
        reader = new BufferedReader(new InputStreamReader(s));
        int vLineCount  = 0;
        int fLineCount  = 0;
        int vnLineCount = 0;
        
        // counts amount of data items of each type
        String line;
        while ((line = reader.readLine()) != null) {
            if ((line.charAt(0) == 'v') && (line.charAt(1) == ' ')) {
                vLineCount++;
            }
            if (line.charAt(0) == 'f') {
                fLineCount++;
            }
            if ((line.charAt(0) == 'v') && (line.charAt(1) == 'n')) {
                vnLineCount++;
            }
        }

        // initializes arrays with calculated data lengths
        vertexData      = new Vec3[vLineCount];
        normalData      = new Vec3[vnLineCount];
        vertexIndices   = new int[fLineCount][3];
        normalIndices   = new int[fLineCount][3];

        s = this.getClass().getResourceAsStream(path);
        reader = new BufferedReader(new InputStreamReader(s));

        int vIndex  = 0;
        int fIndex  = 0;
        int vnIndex = 0;
        
        // reads data from file based on prefix
        while ((line = reader.readLine()) != null) {
            // vertex positions
            // prefix v
            if ((line.charAt(0) == 'v') && (line.charAt(1) == ' ')) {
                String[] stSplit = line.split("\\s+");
                vertexData[vIndex] = new Vec3(
                    Double.valueOf(stSplit[1]), 
                    Double.valueOf(stSplit[2]), 
                    Double.valueOf(stSplit[3]));
                vIndex++;
            }
            // indices of linked verticies, indices of vertex normals
            // prefix f
            if (line.charAt(0) == 'f') {
                String[] stSplit = line.split("\\s+");
                int val1 = Integer.valueOf(stSplit[1].split("/")[0]);
                int val2 = Integer.valueOf(stSplit[2].split("/")[0]);
                int val3 = Integer.valueOf(stSplit[3].split("/")[0]);

                // obj file format indices start at 1, mine start at 0
                vertexIndices[fIndex] = new int[]{val1 - 1, val2 - 1, val3 - 1}; 
                
                int nor1 = Integer.valueOf(stSplit[1].split("/")[2]);
                int nor2 = Integer.valueOf(stSplit[2].split("/")[2]);
                int nor3 = Integer.valueOf(stSplit[3].split("/")[2]);

                // obj file format indices start at 1, mine start at 0
                normalIndices[fIndex] = new int[]{nor1 - 1, nor2 - 1, nor3 - 1};

                fIndex++;
            }  
            // vertex normals
            // prefix vn
            if ((line.charAt(0) == 'v') && (line.charAt(1) == 'n')) {
                String[] stSplit = line.split("\\s+");
                normalData[vnIndex] = new Vec3(
                    Double.valueOf(stSplit[1]), 
                    Double.valueOf(stSplit[2]), 
                    Double.valueOf(stSplit[3]));
                vnIndex++;
            }
        }

        reader.close();
    }
}