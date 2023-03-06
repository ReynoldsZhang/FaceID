package cn.zzhxccelerator;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetFaceMarks {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        runFaceID();

        //cn.zzhxccelerator.Facemark.getFacemarks("D:\\Projects\\FaceID\\Photos\\Base Photos\\Base_Photo_1.png", "drawFacemarks");
    }

    /**
     * the method used to calculate the distance with two points.
     * @param x1 point1 xRay
     * @param x2 point2 xRay
     * @param y1 point1 yRay
     * @param y2 point2 yRay
     * @return
     */
    public static double form(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1 + x2, 2) + Math.pow(y1 + y2, 2));
    }

    /**
     * the function used to drawFacemarks
     * @param FileNameAndLocation
     * @param fileName
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public static void processImage(String FileNameAndLocation, String fileName) throws IOException, URISyntaxException, InterruptedException {
        Facemark.getFacemarks(FileNameAndLocation, "drawFacemarks", fileName);
    }

    /**
     * the function used to run the camera Gui and when the user finished taking base photos, the system will start to compare face.
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public static void runFaceID() throws IOException, URISyntaxException, InterruptedException {
        Camera_GUI_System camerSystem = new Camera_GUI_System();
        do{
            if(camerSystem.BaseFaceSet){
                keepTakingPhotos(camerSystem);
            }
        }while(true);
    }

    /**
     * the method after user taking base photos, it will keep taking photos and then compare each face with the base photos
     * @param cameraSystem
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public static void keepTakingPhotos(Camera_GUI_System cameraSystem) throws IOException, URISyntaxException, InterruptedException {
        String FacemarkSetting = null;
        int numberOfPhoto = 0;
        int[][] Facemarks = null;
        do {
                cameraSystem.takePhoto("Photo_taken_" + numberOfPhoto);

                Facemarks = Facemark.getFacemarks(
                        "Photos\\Camera Photos\\Photo_taken_" + numberOfPhoto + ".png",
                        FacemarkSetting, "Photo_taken_" + numberOfPhoto);

                if (Facemarks == null) {
                    cameraSystem.addProcessingText("Error with taken photo");
                } else {
                    processImage("Photos\\Camera Photos\\Photo_taken_" + numberOfPhoto + ".png", "Photo_taken_" + numberOfPhoto);
                    cameraSystem.addProcessingText("Complete taking photo No." + numberOfPhoto);
                }
             //TODO
            //call the function tha compare each picture with the bas photos
            {

            }

            numberOfPhoto++;
        } while (true);
    }
}


