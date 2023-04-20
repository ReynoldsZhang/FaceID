package cn.zzhxccelerator;

import cn.zzhxccelerator.face.Face;
import cn.zzhxccelerator.face.FaceModel;
import cn.zzhxccelerator.util.CompareResult;
import cn.zzhxccelerator.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import static cn.zzhxccelerator.face.Face.getFace;

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
                break;
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
                    Utils.getPath("Photos", "Camera Photos", "Photo_taken_" + numberOfPhoto + ".png"),
                    FacemarkSetting, "Photo_taken_" + numberOfPhoto);

            if (Facemarks == null) {
                cameraSystem.addProcessingText("Error with taken photo");
                continue;
            } else {
                //After taking photo, edit the photo and put is into Facemark output
                processImage(Utils.getPath("Photos", "Camera Photos", "Photo_taken_" + numberOfPhoto + ".png"), "Photo_taken_" + numberOfPhoto);
                cameraSystem.addProcessingText("Complete taking photo No." + numberOfPhoto);
                cameraSystem.updateIdentifiedImagePhoto(Utils.getPath("Photos", "Camera Photos", "Photo_taken_" + numberOfPhoto + ".png"));
            }
            //get the photo that edited and create the face model
            Face face = getFace(Utils.getPath("Photos", "Facemark Output", "Facemark_Output_Photo_taken_" + numberOfPhoto + ".jpg"));

            //TODO
            //starte to compare
            Date startTime = new Date();

            // example:
            // create face model by giving three image paths
            FaceModel model = new FaceModel("Photos\\Base Photos\\Base_Photo_1.png", "Photos\\Base Photos\\Base_Photo_2.png", "Photos\\Base Photos\\Base_Photo_3.png");
            CompareResult isTheSameFace = model.getCompareResult(face);

            String isLocked = "";

            if (isTheSameFace.isPassed()) {
                //if TRUE
                cameraSystem.updateLockedStatus(true);
                isLocked = "Unlock";
            } else {
                //if FALSE
                cameraSystem.updateLockedStatus(false);
                isLocked = "Lock";
            }

            Date endTime = new Date();

            //put the information into the CSV file
            cameraSystem.addProcessingTime(String.valueOf(endTime.getTime() - startTime.getTime()));
            cameraSystem.addFaceIDData("Photo_taken_" + numberOfPhoto, String.valueOf(numberOfPhoto),
                    String.valueOf((10.0 - isTheSameFace.diff) * 10), "77%", isLocked
                    , String.valueOf(endTime.getTime() - startTime.getTime()));

            numberOfPhoto++;
            //cameraSystem.updateLockedStatus(false);
        } while (true);
    }

    public static void testAllFace(FaceModel base, int num) {
        File dir = new File("Photos\\Camera Photos\\Photo_taken_" + num + ".jpg");
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
            Face face = getFace(Utils.getPath("Photos\\Camera Photos\\Photo_taken_" + num + ".jpg", files[i]));
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


