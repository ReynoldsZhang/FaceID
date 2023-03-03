package cn.zzhxccelerator;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetFaceMarks {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        runFaceID();

        //cn.zzhxccelerator.Facemark.getFacemarks("D:\\Projects\\FaceID\\Photos\\Base Photos\\Base_Photo_1.png", "drawFacemarks");
    }

    public static double form(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1 + x2, 2) + Math.pow(y1 + y2, 2));
    }

    public static void processImage(String FileNameAndLocation, String fileName) throws IOException, URISyntaxException, InterruptedException {
        Facemark.getFacemarks(FileNameAndLocation, "drawFacemarks", fileName);
    }

    public static void runFaceID() throws IOException, URISyntaxException, InterruptedException {
        Camera_GUI_System camerSystem = new Camera_GUI_System();
        do{
            if(camerSystem.BaseFaceSet){
                keepTakingPhotos(camerSystem);
            }
        }while(true);
    }

    public static void keepTakingPhotos(Camera_GUI_System cameraSystem) throws IOException, URISyntaxException, InterruptedException {
        String FacemarkSetting = null;
        int numberOfPhoto = 0;
        int[][] Facemarks = null;
        do {
                cameraSystem.takePhoto("Photo_taken_" + numberOfPhoto);

                Facemarks = Facemark.getFacemarks(
                        "Photos\\Camera Photos" + "Photo_ taken_" + numberOfPhoto + ".png",
                        FacemarkSetting, "Photo_ taken_" + numberOfPhoto);

                if (Facemarks == null) {
                    cameraSystem.addProcessingText("Error with taken photo");
                } else {
                    processImage("Photos\\Camera Photos" + "Photo_ taken_" + numberOfPhoto + ".png", "Photo_ taken_" + numberOfPhoto);
                }
        } while (true);
    }
}


