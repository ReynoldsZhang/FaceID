package cn.zzhxccelerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import cn.zzhxccelerator.util.Utils;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
//import com.lowagie.text.Font;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import com.opencsv.CSVWriter;

import static cn.zzhxccelerator.GetFaceMarks.keepTakingPhotos;
import static cn.zzhxccelerator.GetFaceMarks.processImage;

public class Camera_GUI_System {

	boolean BaseFaceSet = false;

	static CSVWriter writer;
	static String DatabaseName = "";

	public Container getContentPane() {
		return null;
	}

	List<Webcam> webcams;
	Webcam webcam;

	/**
	 * Creates all the labels used to show plate information
	 */

	JPanel bluePanel = new JPanel();

	JLabel LockedStatus = new JLabel("Locked 🔒");

	JPanel PlateImagePanel = new JPanel();
	JPanel AlteredImagePlateImagePanel = new JPanel();

	JLabel ProcessingText = new JLabel("Set up Face ID");

	JLabel textField = new JLabel("");
	ImageIcon imageIcon = new ImageIcon();
	JLabel WholeImageJLabel = new JLabel();
	JLabel Time = new JLabel("Processing Time");
	JLabel SimlarityScoreText = new JLabel("Similarity Score");
	JLabel IdentifiedImagePanelPhotoLabel = new JLabel();
	JLabel AlteredIdentifiedImagePanelPhotoLabel = new JLabel();
	JPanel ProcessingPanelLiveUpdates = new JPanel();

	JPanel SetupFaceID = new JPanel();

	int ImageHeight = 200;
	int ImageWidth = 400;

	int ImageInfoLabelHeight = 20;
	int ImageInfoLabelWidth = 10;

	int LabelImageHeight = 150;
	int LabelImageWidth = 100;

	int SideBarWidth = 450;

	int UpdatesPanelWidth = 22;
	int UpdatesPanelHeight = 22;

	int SimilarityScoreHeight = 25;
	int SimilarityScoreTextHeight = 23;

	public Camera_GUI_System(int Test) {

	}

	public Camera_GUI_System() {

		DatabaseName = "FaceID.csv";

		/**
		 * CAMERA VIEW SETUP
		 */

		this.webcams = Webcam.getWebcams();

		if (webcams.size() >= 2) {
			this.webcam = webcams.get(1);

		} else {
			this.webcam = webcams.get(0);
		}

		/**
		 * Used to create the camera resolution
		 */
		Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
				WebcamResolution.HVGA.getSize(), new Dimension(1980, 1080), };

		webcam.setCustomViewSizes(nonStandardResolutions);
//		webcam.setViewSize(WebcamResolution.HD720.getSize());

		/**
		 * Sets the Camera Resolution to 1080p
		 */
		webcam.setViewSize(new Dimension(1980, 1080));
		webcam.open();

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(false);

		JPanel totalGUI = new JPanel();
		totalGUI.setLayout(new BoxLayout(totalGUI, BoxLayout.Y_AXIS));

		/**
		 * TOP Label GUI
		 */
		JPanel redPanel = new JPanel();
		redPanel.setBackground(Color.decode("#cf2a2a"));
		redPanel.setMinimumSize(new Dimension(SideBarWidth, 30));
		redPanel.setPreferredSize(new Dimension(SideBarWidth, 30));
		JLabel Title = new JLabel("LAOWAI FACEID");
		Title.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, 29));
		Title.setForeground(Color.WHITE);
		redPanel.add(Title);
		totalGUI.add(redPanel);

		// This is the first spacer. This creates a spacer 10px wide that
		// will never get bigger or smaller.
		totalGUI.add(Box.createRigidArea(new Dimension(10, 0)));

		/**
		 * WHOLE PHOTO GUI
		 */
		JPanel WholePhotoWithPlateID = new JPanel();
		WholePhotoWithPlateID.setBackground(Color.decode("#d1d6db"));
		WholePhotoWithPlateID.setPreferredSize(new Dimension(SideBarWidth, ImageInfoLabelHeight));

		/**
		 * SHows one of the photos taken by the camera to set the 'device owner face'
		 */
		JLabel WholePhotoLabel = new JLabel("Photo of Device Owner");
		WholePhotoLabel.setForeground(Color.decode("#475e71"));
		WholePhotoLabel.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, 30));
		WholePhotoWithPlateID.add(WholePhotoLabel);

		Image img = null;
		try {
			img = ImageIO.read(new File("WaitingWholePhoto.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(ImageWidth, ImageHeight, Image.SCALE_SMOOTH);
		imageIcon.setImage(dimg);
		WholeImageJLabel.setIcon(imageIcon);

		WholePhotoWithPlateID.add(WholeImageJLabel);

		JPanel yellowPanel2 = new JPanel();
		yellowPanel2.setBackground(Color.decode("#d1d6db"));
		yellowPanel2.setPreferredSize(new Dimension(ImageWidth, ImageHeight + 1));
		yellowPanel2.add(WholeImageJLabel);

		totalGUI.add(WholePhotoWithPlateID);
		totalGUI.add(yellowPanel2);

		/**
		 * Last face Identified GUI
		 */

		JPanel IdentifiedImagePanel = new JPanel();
		IdentifiedImagePanel.setBackground(Color.decode("#8c9aa6"));
		IdentifiedImagePanel.setPreferredSize(new Dimension(SideBarWidth, ImageInfoLabelHeight));

		JLabel y = new JLabel("Last Face Identified");
		y.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, 30));
		IdentifiedImagePanel.add(y);
		totalGUI.add(IdentifiedImagePanel);

		JPanel IdentifiedImagePanelPhoto = new JPanel();
		IdentifiedImagePanelPhoto.setBackground(Color.decode("#8c9aa6"));
		IdentifiedImagePanelPhoto.setPreferredSize(new Dimension(ImageWidth, ImageHeight + 1));

		/**
		 * Puts the image in the Panel - first photo is a loading image
		 */
		Image IDPlateImage = null;
		try {
			IDPlateImage = ImageIO.read(new File("WaitingWholePhoto.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Image dimgx = IDPlateImage.getScaledInstance(ImageWidth, ImageHeight, Image.SCALE_SMOOTH);
		ImageIcon ImageIcon2 = new ImageIcon();
		ImageIcon2.setImage(dimgx);

		IdentifiedImagePanelPhotoLabel.setIcon(ImageIcon2);
		IdentifiedImagePanelPhoto.add(IdentifiedImagePanelPhotoLabel);

		/**
		 * Adds the identified image to the GUI
		 */
		totalGUI.add(IdentifiedImagePanelPhoto);

		/**
		 * Adds the space between the panels
		 */
		totalGUI.add(Box.createHorizontalGlue());

		/**
		 * FILLER GUI
		 */

		Dimension minSize = new Dimension(10, 30);
		Dimension prefSize = new Dimension(10, 30);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 30);
		totalGUI.add(new Box.Filler(minSize, prefSize, maxSize));

		/**
		 * Lock / Unlock Status GUI
		 */

		bluePanel.setBackground(Color.red);
		bluePanel.setPreferredSize(new Dimension(100, 80));
		LockedStatus.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, 70));
		LockedStatus.setForeground(Color.WHITE);
		bluePanel.add(LockedStatus);
		totalGUI.add(bluePanel);

		totalGUI.add(new Box.Filler(minSize, prefSize, maxSize));

		/**
		 * Face ID Setup Button
		 *
		 * When Pressed it starts the Action Listener Below
		 */

		JButton b = new JButton("     Face ID Setup ", new ImageIcon("FaceID2.png"));

		b.setBackground(Color.blue);
		b.setForeground(Color.white);
		b.setFont(new java.awt.Font("Arial", Font.BOLD, 25));

		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				/**
				 * Creates a new thread so that the screen doesn't freeze
				 */

				Thread hilo = new Thread(new Runnable() {

					@Override
					public void run() {

						addProcessingText("Setting up Face ID ↻");

						/**
						 * This deletes all the base photos, so new ones can be taken
						 */

						try {

							addProcessingText("Taking Base Photos ↻");

							File Photo1 = new File(Utils.getPath("Photos", "Base Photos", "Base_Photo_1" + ".png"));
							File Photo2 = new File(Utils.getPath("Photos", "Base Photos", "Base_Photo_2" + ".png"));
							File Photo3 = new File(Utils.getPath("Photos", "Base Photos", "Base_Photo_3" + ".png"));

							if (Photo1.delete()) {

								addProcessingText("Deleted the file: " + Photo1.getName());

							} else {

								addProcessingText("Failed to delete the file.");

							}

							if (Photo2.delete()) {

								addProcessingText("Deleted the file: " + Photo2.getName());

							} else {

								addProcessingText("Failed to delete the file.");
							}

							if (Photo3.delete()) {

								addProcessingText("Deleted the file: " + Photo3.getName());

							} else {
								addProcessingText("Failed to delete the file.");
							}

							String FacemarkSetting = null;
//        					String FacemarkSetting = "NumberedPoints";

							int[][] Facemarks = null;

							/**
							 * This do while will take photos, if the photo contains no faces or more than 1
							 * then it will keep taking photos until there is only one face in the photo
							 */

							do {

								takeBasePhoto("Base_Photo_1");
								Facemarks = Facemark.getFacemarks(Utils.getPath("Photos", "Base Photos", "Base_Photo_1" + ".png"),
										FacemarkSetting, "Base_Photo_1");

								if (Facemarks == null) {

									addProcessingText("Error with Photo One");

								}

							} while (Facemarks == null);
							processImage(Utils.getPath("Photos", "Base Photos", "Base_Photo_1" + ".png"), "Base_Photo_1");

							addProcessingText("Taken Photo 1 ↻");

							Facemarks = null;

							/**
							 * This do while will take photos, if the photo contains no faces or more than 1
							 * then it will keep taking photos until there is only one face in the photo
							 */

							do {
								takeBasePhoto("Base_Photo_2");
								Facemarks = Facemark.getFacemarks(Utils.getPath("Photos" , "Base Photos", "Base_Photo_2" + ".png"),
										FacemarkSetting, "Base_Photo_2");

								if (Facemarks == null) {
									addProcessingText("Error with Photo Two");
								}

							} while (Facemarks == null);
							processImage(Utils.getPath("Photos", "Base Photos", "Base_Photo_2" + ".png"), "Base_Photo_2");

							addProcessingText("Taken Photo 2 ↻");

							Facemarks = null;

							/**
							 * This do while will take photos, if the photo contains no faces or more than 1
							 * then it will keep taking photos until there is only one face in the photo
							 */

							do {
								takeBasePhoto("Base_Photo_3");
								Facemarks = Facemark.getFacemarks(
										Utils.getPath("Photos", "Base Photos", "Base_Photo_3" + ".png"),
										FacemarkSetting, "Base_Photo_3");

								if (Facemarks == null) {
									addProcessingText("Error with Photo Three");
								}

							} while (Facemarks == null);
							processImage(Utils.getPath("Photos", "Base Photos", "Base_Photo_3" + ".png"), "Base_Photo_3");

							addProcessingText("Taken Photo 3 ↻");

						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						BaseFaceSet = true;

						updateWholeImage(Utils.getPath("Photos", "Base Photos", "Base_Photo_2" + ".png"));

						addProcessingText("Completed Taking Photos");

					}
				});

				/**
				 * Starts the thread, which is the photo taking method above
				 */


				hilo.start();

			}

		});

		SetupFaceID.setBackground(Color.blue);
		SetupFaceID.setPreferredSize(new Dimension(100, 65));
		Border emptyBorder = BorderFactory.createEmptyBorder();
		b.setBorder(emptyBorder);
		SetupFaceID.add(b);

		totalGUI.add(SetupFaceID);

		totalGUI.add(new Box.Filler(minSize, prefSize, maxSize));

		/**
		 * Updates Panel
		 */

		ProcessingPanelLiveUpdates.setBackground(Color.gray);
		ProcessingPanelLiveUpdates.setPreferredSize(new Dimension(UpdatesPanelWidth, UpdatesPanelHeight));
		textField.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, UpdatesPanelHeight + 5));
		textField.setForeground(Color.black);
		textField.setText("Start by setting up Face ID");
		ProcessingPanelLiveUpdates.add(textField);

		totalGUI.add(ProcessingPanelLiveUpdates);

		/**
		 * Panel to show time to process
		 */

		JPanel ProcessingPanelLiveTime = new JPanel();
		ProcessingPanelLiveTime.setBackground(Color.DARK_GRAY);
		ProcessingPanelLiveTime.setPreferredSize(new Dimension(SimilarityScoreHeight, SimilarityScoreHeight));
		Time.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, SimilarityScoreTextHeight));
		Time.setForeground(Color.white);
		ProcessingPanelLiveTime.add(Time);
		totalGUI.add(ProcessingPanelLiveTime);

		/**
		 * Panel to show number of plates found
		 */
		JPanel SimilarityScore = new JPanel();
		SimilarityScore.setBackground(Color.DARK_GRAY);
		SimilarityScore.setPreferredSize(new Dimension(SimilarityScoreHeight, SimilarityScoreHeight));
		SimlarityScoreText.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, SimilarityScoreTextHeight));
		SimlarityScoreText.setForeground(Color.white);
		SimilarityScore.add(SimlarityScoreText);
		totalGUI.add(SimilarityScore);

		/**
		 * Creates the window using the panels from above
		 */

		JSplitPane sl = new JSplitPane(SwingConstants.VERTICAL, totalGUI, panel);
		sl.setOrientation(SwingConstants.VERTICAL);

		JFrame window = new JFrame("Face ID");
		window.add(sl);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);

	}

	/**
	 * Takes a photo with the webcam
	 *
	 * @param PhotoName
	 * @return
	 * @throws IOException
	 */
	public String takePhoto(String PhotoName) throws IOException {

		BufferedImage image = webcam.getImage();
		ImageIO.write(image, "PNG", new File(Utils.getPath("Photos", "Camera Photos", PhotoName + ".png")));

		return PhotoName;

	}

	public String takeBasePhoto(String PhotoName) throws IOException {

		BufferedImage image = webcam.getImage();
		ImageIO.write(image, "PNG", new File(Utils.getPath("Photos", "Base Photos", PhotoName + ".png")));

		return PhotoName;

	}

	/**
	 * Closes the camera
	 */
	public void CloseCamera() {
		webcam.close();
	}

	/**
	 * adds the number plate to the GUI, you will need to call this in the other
	 * classes
	 *
	 * @param Status
	 */
	public void updateLockedStatus(Boolean Status) {

		if (Status) {

			LockedStatus.setText("unlocked 🔓");
			bluePanel.setBackground(Color.green);

		} else {

			LockedStatus.setText("Locked 🔒");
			bluePanel.setBackground(Color.red);

		}

	}

	/**
	 * Adds processing information to update the user
	 *
	 * @param ProcessingTextFromSystem
	 */
	public void addProcessingText(String ProcessingTextFromSystem) {

		if (ProcessingTextFromSystem == "No Face Found") {

			ProcessingPanelLiveUpdates.setBackground(Color.gray);
			ProcessingPanelLiveUpdates.setPreferredSize(new Dimension(UpdatesPanelWidth, UpdatesPanelHeight));
			textField.setFont(new java.awt.Font("Century Schoolbook L", 20, UpdatesPanelHeight + 5));
			textField.setForeground(Color.white);
			textField.setText(ProcessingTextFromSystem);

		} else {

			ProcessingPanelLiveUpdates.setBackground(Color.yellow);
			ProcessingPanelLiveUpdates.setPreferredSize(new Dimension(UpdatesPanelWidth, UpdatesPanelHeight));
			textField.setFont(new java.awt.Font("Century Schoolbook L", Font.BOLD, UpdatesPanelHeight + 5));
			textField.setForeground(Color.black);
			textField.setText(ProcessingTextFromSystem);
		}

		System.out.println(" ");
		System.out.println(ProcessingTextFromSystem);
		System.out.println(" ");

	}

	boolean plateDataCalled = false;

	/**
	 * Adds the Face ID Data to the CSV and updates the GUI
	 *
	 * @param PhotoName
	 * @param PhotoNumber
	 * @param SimilarityScore
	 * @param LockOrUnLock
	 * @param ProcessingTime
	 */

	public void addFaceIDData(String PhotoName, String PhotoNumber, String SimilarityScore, String SimlarityScorePassingMark , String LockOrUnLock,
							  String ProcessingTime) {
		SimlarityScoreText.setText("Similarity Score: " + SimilarityScore + "%");
		Time.setText(ProcessingTime + " ms");

		if (plateDataCalled == false) {

			String[] DataToWriteFirst = { "Photo Name", "Photo Number", "Similarity Score", "Similarity Passing Mark", "Lock or Unlock",
					"Processing Time" };

			WriteToDatabase(DataToWriteFirst);
			CloseWriter();

			String[] DataToWrite = { PhotoName, PhotoNumber, SimilarityScore, SimlarityScorePassingMark , LockOrUnLock, ProcessingTime };

			WriteToDatabase(DataToWrite);
			CloseWriter();

		} else {
			String[] DataToWrite = { PhotoName, PhotoNumber, SimilarityScore, SimlarityScorePassingMark,  LockOrUnLock, ProcessingTime };

			WriteToDatabase(DataToWrite);
			CloseWriter();
		}

		plateDataCalled = true;

	}

	/**
	 * Shows the number of plates found
	 *
	 * @param PlateCaptures
	 */
	public void addSimilarityScore(String PlateCaptures) {
		SimlarityScoreText.setText(PlateCaptures);
	}

	/**
	 * Shows the time to process the plate
	 *
	 * @param PTime
	 */
	public void addProcessingTime(String PTime) {
		Time.setText(PTime);
	}

	/**
	 * Updates the main image
	 *
	 * @param ImageFileAndLocation
	 */
	public void updateWholeImage(String ImageFileAndLocation) {
		Image img = null;
		try {
			img = ImageIO.read(new File(ImageFileAndLocation));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(ImageWidth, ImageHeight, Image.SCALE_SMOOTH);
		ImageIcon x = new ImageIcon();
		x.setImage(dimg);
		WholeImageJLabel.setIcon(x);
	}

	/**
	 * Updates the plate photo taken
	 *
	 * @param ImageFileAndLocation
	 */
	public void updateIdentifiedImagePhoto(String ImageFileAndLocation) {

		Image img = null;
		try {
			img = ImageIO.read(new File(ImageFileAndLocation));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(ImageWidth, ImageHeight, Image.SCALE_SMOOTH);
		ImageIcon x = new ImageIcon();
		x.setImage(dimg);
		IdentifiedImagePanelPhotoLabel.setIcon(x);

	}

	/**
	 * Writes the ANPR information to the CSV
	 *
	 * @param DataToWrite
	 * @return
	 */
	public static boolean WriteToDatabase(String[] DataToWrite) {
		createWriter(Utils.getPath("Photos", DatabaseName));
		writer.writeNext(DataToWrite);
		return true;

	}

	/**
	 * Creates a CSV Writer
	 *
	 * @param filePath
	 * @return
	 */
	public static CSVWriter createWriter(String filePath) {

		File file = new File(filePath);

		FileWriter outputfile = null;

		try {
			outputfile = new FileWriter(file, true);
			writer = new CSVWriter(outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return writer;
	}

	/**
	 * Closes the Writer
	 */
	public static void CloseWriter() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
