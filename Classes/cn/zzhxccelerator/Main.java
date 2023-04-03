package cn.zzhxccelerator;

import cn.zzhxccelerator.face.Face;
import cn.zzhxccelerator.face.FaceComparator;
import cn.zzhxccelerator.face.FaceModel;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

import static cn.zzhxccelerator.face.Face.getFace;

public class Main {
    public static void main(String[] args) {
//        Weightable.checkWeight(Face.class);
        Face f1 = getFace("Photos/Base Photos/Base_Photo_1.png");
        Face f2 = getFace("Photos/Base Photos/Base_Photo_2.png");
//        Face f3 = getFace("Photos/Base Photos/Base_Photo_3.png");
//        Face f4 = getFace("Photos/Tests/test1.jpg");
//        Face f5 = getFace("Photos/Tests/test2.jpg");
        Face f6 = getFace("Photos/Tests/test3.jpg");
//        Face fake1 = getFace("Images of Faces/1_0_0_20170110212654271.jpg");
//        Face fake2 = getFace("Images of Faces/6_1_4_20161221193229605.jpg");
//        Face fake3 = getFace("Images of Faces/13_1_1_20170109204443201.jpg");
//        Face fake4 = getFace("Images of Faces/18_1_0_20170109213413212.jpg");
//        Face fake5 = getFace("Images of Faces/29_1_0_20170103183824867.jpg");
//        Face fake6 = getFace("Images of Faces/59_0_0_20170111203940975.jpg");
        FaceModel model = new FaceModel(f1, f2, f6);
//        Face fake7 = getFace("Images of Faces/16_1_0_20170109203419299.jpg");
        doAll(model);
//        System.out.println(model.compare(fake7));
    }

    public static void doAll(FaceModel base) {
        File dir = new File("Images of Faces");
        String[] files = dir.list();
        for (int i = 300; i < files.length; i++) {
            System.out.println("index " + i + " of " + files.length + ": " + files[i]);
            Face face = getFace("Images of Faces/" + files[i]);
            if (face == null) {
                return;
            }
            double d = base.compare(face);
            if (Math.abs(1 - d) < 0.1) {
                System.out.println("\u001B[32m" + face.file.getName() + " passed." + "\u001B[0m");
                System.console().readLine();
            } else {
                System.out.println("\u001B[31m" + face.file.getName() + " failed." + "\u001B[0m");
            }
        }
    }

    public static void storeFace(Face face) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File("Faces", face.file.getName().split("\\.")[0]))
        )) {
            oos.writeObject(face);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Face loadFace(String name) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Faces", name)))) {
            return (Face) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void test(Function<Face, Double> fun, Face... faces) {
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == null) {
                System.out.println("index " + i + " is null");
                continue;
            }
            System.out.println("index " + i + ": " + fun.apply(faces[i]));
        }
    }

    public static void test2(Function<Face, double[]> fun, Face... faces) {
        for (Face face : faces) {
            System.out.println(Arrays.toString(fun.apply(face)));
        }
    }
}


