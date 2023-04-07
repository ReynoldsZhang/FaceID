package cn.zzhxccelerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import cn.zzhxccelerator.util.Utils;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;
import org.bytedeco.opencv.opencv_highgui.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_objdetect.*;
//import org.opencv.imgproc.Imgproc;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_objdetect.*;
@SuppressWarnings("all")
public class Facemark {

	/**
	 * This method will return a 2D Array of vectors which represent facial features on an image
	 * @param PhotoNameAndLocation
	 * @param CreateMarkers
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 */

	public static int[][] getFacemarks(String PhotoNameAndLocation, String CreateMarkers, String fileName)
			throws IOException, URISyntaxException, InterruptedException {

		/**
		 * This loads the Face Detector
		 */

		CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt2.xml");

		/**
		 * Creates a cn.zzhxccelerator.Facemark Object
		 */

		FacemarkKazemi facemark = FacemarkKazemi.create();

		/**
		 * Loads the Landmark Detector
		 */

		facemark.loadModel("face_landmark_model.dat");

		/**
		 * Loads the Image
		 */

		Mat img = imread(PhotoNameAndLocation);

		/**
		 * For better landmark detection the image is converted to gray scale and is adjusted to enhance the contrast
		 */

		Mat gray = new Mat();
		 cvtColor(img, gray, COLOR_BGR2GRAY);
		equalizeHist(gray, gray);

		/**
		 * Finds Faces on the image
		 */

		RectVector faces = new RectVector();
		faceDetector.detectMultiScale(gray, faces);

		System.out.println("Faces detected: " + faces.size());

		/**
		 * If more than 1 face or no faces are found then null is returned
		 */

		if (faces.size() != 1) {
			System.out.println(" More than 1 face or no Faces Detected");
			img.release();
			gray.release();
			return null;
		}

		/**
		 *
		 *  This creates a variable for landmarks
		 *
		 *  Landmarks for one face is a vector of points
		 *
		 */

		Point2fVectorVector landmarks = new Point2fVectorVector();

		/**
		 * Runs the landmark Detector
		 */

		boolean success = facemark.fit(img, faces, landmarks);

		/**
		 * Creates a 2D array to hold the landmarks
		 *
		 * 68 Landmarks in total, each coordinate has an X and Y
		 *
		 */

		int[][] FacialCoordinates = new int[68][2];

		if (success) {

			/**
			 * If its successful put the landmarks on the face
			 */

			for (long i = 0; i < landmarks.size(); i++) {

				/**
				 * Get the Vector points
				 */

				Point2fVector v = landmarks.get(i);

				for (int x = 0; x < v.size(); x++) {

					/**
					 * Convert the Vertor points to an int and then add them to the FacialCoordinates 2D Array
					 */

					FacialCoordinates[x][0] = (int) v.get(x).get(0);
					FacialCoordinates[x][1] = (int) v.get(x).get(1);

					/**
					 * Create a new point object so that we can place the marks on a face
					 */

					Point position = new Point(FacialCoordinates[x][0], FacialCoordinates[x][1]);

					/**
					 * If CreateMarkers == "XandY" then X and Y coordinates are added to the generated photo
					 *
					 * If CreateMarkers == "NumberedPoints" then the numbered coordinates are added to the generated photo
					 *
					 */

					if (CreateMarkers == "XandY") {

						opencv_imgproc.putText(img,
								". X: " + FacialCoordinates[x][0] + ", Y: " + FacialCoordinates[x][1], position,
								opencv_imgproc.FONT_HERSHEY_PLAIN, 0.7, Scalar.RED, 1, 0, false);

					} else if (CreateMarkers == "NumberedPoints") {

						opencv_imgproc.putText(img, "." + x, position, opencv_imgproc.FONT_HERSHEY_PLAIN, 1, Scalar.RED,
								1, 0, false);

					}

				}

//				v.position();

				if (CreateMarkers == "drawFacemarks") {

					/**
					 * This puts the landmarks on the image
					 */

					drawFacemarks(img, v, Scalar.YELLOW);

					/**
					 * This will display the results in a window
					 */

//					imshow("Kazemi Facial Landmark" , img);
//					cvWaitKey(0);

				}

			}
		}

		if (CreateMarkers != null) {

		/**
		 * Saves a photo with the landmarks
		 */

			imwrite(Utils.getPath("Photos", "Facemark Output", "Facemark_Output_" + fileName +".jpg"), img);

		}

		img.release();
		gray.release();

		/**
		 * Returns a 2D Array of Facial Landmarks
		 */

		return FacialCoordinates;
	}

	public static void AddLineOnPhoto(String PhotoNameAndLocation, Point Start, Point End, String Description) {


		/**
		 * You need to use Point Objects which can be created like so:
		 *
		 * Point start = new Point(x1, y1);
		 *
		 * Point end = new Point(x2, y2);
		 *
		 */

		Mat img = imread(PhotoNameAndLocation);

		opencv_imgproc.line(img, Start, End, Scalar.RED);

		PhotoNameAndLocation = removeExtension(PhotoNameAndLocation);

		imwrite(Utils.getPath("Photos", "Facemark Edits", "L_O_" + PhotoNameAndLocation + " " + Description
				+ ".jpg"), img);

	}

	public static String removeExtension(String s) {

		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path up to the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

}
