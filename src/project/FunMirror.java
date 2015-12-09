package project;

import com.github.sarxos.webcam.*;
import filters.BackgroundFilter;
import filters.MotionDistortionFilter;
import filters.MultiPinchFilter;
import filters.PinchFilter2;

import javax.swing.*;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class FunMirror implements GameModeSwitch{
	private static FunMirror funMirror;
	private MotionDistortionFilter mdf = new MotionDistortionFilter();
	private WebcamMotionDetector detector;
	private BackgroundFilter bgf;
	private filters.WaveFilter wf;
	private filters.GrayFilter gray;
	private filters.FrameFilter ff;
	private PinchFilter2 pf;
	private MultiPinchFilter mpf;
	private GameModeSwitchDetector gms;
	private int currentGameMode = 0;
	private final int numberOfGameModes = 3;
	private Webcam webcam;

	public FunMirror() {

	}

	public void run(){
		gray = new filters.GrayFilter();
		wf = new filters.WaveFilter();
		pf = new PinchFilter2();
		mpf = new MultiPinchFilter();
		//ff = new filters.FrameFilter();

		bgf = new BackgroundFilter();
		bgf.setMaxArea(1);
		bgf.setMinTime(5000);
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		//webcam.setImageTransformer(wf);
		//webcam.setImageTransformer(gray);
		//webcam.setImageTransformer(ff);
		//webcam.setImageTransformer(bgf);
		webcam.open();

		System.out.println("ViewSize: " + webcam.getViewSize());

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
		funMirror.run();
	}

	private void setupGameModeSwitch(){
		gms = new GameModeSwitchDetector();
		gms.setMinTime(5000);
		detector.addMotionListener(gms);
		gms.addGameModeSwitch(this);
	}

	@Override
	public void onGameModeSwitch(int args) {
		System.out.println("Game mode changed");
		currentGameMode = (currentGameMode + 1) % numberOfGameModes;


		switch (currentGameMode){
			case 0:
				webcam.setImageTransformer(mpf);
				break;
			case 1:
				webcam.setImageTransformer(pf);
				break;
			case 2:
				webcam.setImageTransformer(wf);
				break;
		}

		System.out.println("Current gamemode: " + currentGameMode);
	}
}
