package cn.zzhxccelerator;

import cn.zzhxccelerator.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class GetFaceMarks {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        runFaceID();
    }

    /**
     * draw facemarks
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
                        Utils.getPath("Photos", "Camera Photos" , "Photo_taken_" + numberOfPhoto + ".png"),
                        FacemarkSetting, "Photo_taken_" + numberOfPhoto);

                if (Facemarks == null) {
                    cameraSystem.addProcessingText("Error with taken photo");
                    continue;
                } else {
                    processImage(Utils.getPath("Photos", "Camera Photos", "Photo_taken_" + numberOfPhoto + ".png"), "Photo_taken_" + numberOfPhoto);
                    cameraSystem.addProcessingText("Complete taking photo No." + numberOfPhoto);
                    cameraSystem.updateIdentifiedImagePhoto(Utils.getPath("Photos", "Camera Photos", "Photo_taken_" + numberOfPhoto + ".png"));
                }
             //TODO
            //call the function tha compare each picture with the bas photos
            Date startTime = new Date();
            {
                //if TRUE
                cameraSystem.updateLockedStatus(true);
                //if FALSE
                cameraSystem.updateLockedStatus(false);
            }
            Date endTime = new Date();

            cameraSystem.addProcessingTime(String.valueOf(endTime.getTime() - startTime.getTime()));
            cameraSystem.addFaceIDData("Photo_taken_" + numberOfPhoto, String.valueOf(numberOfPhoto),
                    "SimilarityScore", "SimilarityScorePassingMark", "LockOrUnLock"
            , String.valueOf(endTime.getTime() - startTime.getTime()));

            numberOfPhoto++;
        } while (true);
    }
}


