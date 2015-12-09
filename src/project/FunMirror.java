package project;

import com.github.sarxos.webcam.*;
import filters.*;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class FunMirror implements GameModeSwitch {
	private static FunMirror funMirror;
	private MotionDistortionFilter mdf = new MotionDistortionFilter();
	private WebcamMotionDetector detector;
	private BackgroundFilter bgf;
	private filters.WaterEffectFilter wef;
	private filters.WaveFilter wf;
	private filters.GrayFilter gray;
	private filters.FrameFilter ff;
	private PinchFilter2 pf;
	private MultiPinchFilter mpf;
	private GameModeSwitchDetector gms;
	private int currentGameMode = 0;
	private final int numberOfGameModes = 6;
	private Webcam webcam;
	private FacePainter facePainter;

	public FunMirror() {

	}

	public void start(){
		gray = new filters.GrayFilter();
		wf = new filters.WaveFilter();
		pf = new PinchFilter2();
		mpf = new MultiPinchFilter();
		wef = new WaterEffectFilter();
		ff = new filters.FrameFilter();


		bgf = new BackgroundFilter();
		bgf.setMaxArea(1);
		bgf.setMinTime(5000);
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());


		webcam.open();

		//Sets the first gamemode
		currentGameMode = numberOfGameModes - 1;
		onGameModeSwitch(0);


		webcam.addWebcamListener(bgf);
		//webcam.addWebcamListener(wf);


		JFrame window = new JFrame("Test Transformer");

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);


		detector = new WebcamMotionDetector(webcam);
		detector.setInterval(100); // one check per x ms
		detector.setPixelThreshold(20);

		detector.addMotionListener(mdf);
		detector.addMotionListener(bgf);
		detector.addMotionListener(mpf);
		detector.setMaxMotionPoints(3);
		detector.setPointRange(40);
		setupGameModeSwitch();
		detector.start();


		facePainter = new FacePainter(webcam, panel);

		window.add(panel);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static int getScreenSizeX(){
		try {
			assert funMirror.webcam != null;
			return funMirror.webcam.getViewSize().width;
		}
		catch(NullPointerException e){
			return 1;
		}
	}

	public static int getScreenSizeY(){
		try {
			assert funMirror.webcam != null;
			return funMirror.webcam.getViewSize().height;
		}
		catch(NullPointerException e){
			return 1;
		}
	}

	public static void main(String[] args) {
		funMirror = new FunMirror();
		funMirror.start();
	}

	private void setupGameModeSwitch(){
		gms = new GameModeSwitchDetector();
		gms.setMinTime(5000);
		detector.addMotionListener(gms);
		gms.addGameModeSwitch(this);
	}

	@Override
	public void onGameModeSwitch(int args) {
		//Dispose the current game mode
		switch (currentGameMode){
			case 1:
				facePainter.stop();
				break;
			default:
				webcam.setImageTransformer(null);
				break;
		}


		System.out.println("Game mode changed");
		currentGameMode = (currentGameMode + 1) % numberOfGameModes;

		//Setup the next game mode
		switch (currentGameMode){
			case 0:
				webcam.setImageTransformer(wf); // WaveFilter
				break;
			case 1:
				facePainter.start();
				break;
			case 2:
				webcam.setImageTransformer(pf); // PinchFilter
				break;
			case 3:
				webcam.setImageTransformer(gray); // GrayFilter
				break;
			case 4:
				webcam.setImageTransformer(wef); // WaterEffectFilter, líkist smá pinch filter eftir allar breytingarnar
				break;							 // en við getum experimentað meira. 
			case 5:
				webcam.setImageTransformer(ff); // FrameFilter
				break;
			case 6:
				webcam.setImageTransformer(mpf); // Multi Pinch Filter
				break;
		}

		System.out.println("Current gamemode: " + currentGameMode);
	}
}
