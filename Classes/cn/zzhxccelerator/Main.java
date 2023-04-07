package cn.zzhxccelerator;

import cn.zzhxccelerator.face.Face;
import cn.zzhxccelerator.face.FaceModel;
import cn.zzhxccelerator.util.CompareResult;
import cn.zzhxccelerator.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cn.zzhxccelerator.face.Face.getFace;

public class Main {
    public static void main(String[] args) {
        // example:
        // create face model by giving three image paths
        FaceModel model = new FaceModel("path", "path", "path");
        boolean isTheSameFace = model.compare("pathToCompare");
        System.out.println(isTheSameFace);

        // uncomment the following code to test all images in the folder
        // testAllFace(model);
    }

    public static void testAllFace(FaceModel base) {
        File dir = new File("Images of Faces");
        // create error log
        File log = new File("error.txt");
        // clear log
        try (FileWriter fw = new FileWriter(log)) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get all images in the folder
        String[] files = dir.list();
        if (files == null) {
            throw new RuntimeException("No files in the folder");
        }
        // iterate all images
        for (int i = 0; i < files.length; i++) {
            System.out.println("index " + i + " of " + files.length + ": " + files[i]);
            // get face
            Face face = getFace(Utils.getPath("Images of Faces", files[i]));
            // if face is null, skip
            if (face == null) {
                continue;
            }
            // compare
            CompareResult result = base.getCompareResult(face);
            if (!result.isPassed()) {
                System.out.println("\u001B[31m" + face.file.getName() + " failed: " + result.diff + ", " + result.ratio + "\u001B[0m");
                try (FileWriter fw = new FileWriter(log, true)) {
                    fw.write(face.file.getName() + " failed: " + result.diff + ", " + result.ratio + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("\u001B[32m" + face.file.getName() + " passed: " + result.diff + ", " + result.ratio + "\u001B[0m");
            }
            // gc, release memory every 20 images
            if (i % 20 == 0) {
                System.gc();
            }
        }
    }
}


