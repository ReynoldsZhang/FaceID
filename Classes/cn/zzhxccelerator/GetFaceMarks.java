package cn.zzhxccelerator;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetFaceMarks {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        new Camera_GUI_System();

        //cn.zzhxccelerator.Facemark.getFacemarks("D:\\Projects\\FaceID\\Photos\\Base Photos\\Base_Photo_1.png", "drawFacemarks");
    }

    public static double form(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1 + x2, 2) + Math.pow(y1 + y2, 2));
    }

    public static void processImage(String FileNameAndLocation) throws IOException, URISyntaxException, InterruptedException {
        Facemark.getFacemarks(FileNameAndLocation, "drawFacemarks");
    }

    public static void Non(){
//        for (int i = 1; i < 4; i++) {
//            try {
//                cn.zzhxccelerator.GetFaceMarks Image_Processing = null;
//                cn.zzhxccelerator.GetFaceMarks.processImage("Photos\\Base Photos\\" + "Base_Photo_" + i + ".png");
//            } catch (IOException | URISyntaxException | InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//
//        int count = 0;
//        int[][] Facemarks;
//        String FacemarkSetting = null;
//        do{
//            try {
//                takePhoto("checkPhotos" + count);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            try {
//                Facemarks = cn.zzhxccelerator.Facemark.getFacemarks(
//                        "Photos\\Camera Photos\\" + "checkPhotos" + count + ".png",
//                        FacemarkSetting);
//            } catch (IOException | URISyntaxException | InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            if (Facemarks == null) {
//                addProcessingText("Error with Taking Photo");
//            }
//        } while (Facemarks == null);
    }
}


