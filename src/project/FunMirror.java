package project;

import com.github.sarxos.webcam.*;
import filters.BackgroundFilter;
import filters.MotionDistortionFilter;
import filters.PinchFilter2;
import filters.WaterEffectFilter;

import javax.swing.*;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class FunMirror implements GameModeSwitch{
	private MotionDistortionFilter mdf = new MotionDistortionFilter();
	private WebcamMotionDetector detector;
	private BackgroundFilter bgf;
	private filters.WaterEffectFilter wef;
	private filters.WaveFilter wf;
	private filters.GrayFilter gray;
	private filters.FrameFilter ff;
	private PinchFilter2 pf;
	private GameModeSwitchDetector gms;
	private int currentGameMode = 0;
	private final int numberOfGameModes = 5;
	private Webcam webcam;

	public FunMirror() {

	}

	public void run(){
		gray = new filters.GrayFilter();
		wf = new filters.WaveFilter();
		pf = new PinchFilter2();
		wef = new WaterEffectFilter();
		ff = new filters.FrameFilter();

		bgf = new BackgroundFilter();
		bgf.setMaxArea(1);
		bgf.setMinTime(5000);
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.setImageTransformer(wf);
		//webcam.setImageTransformer(wef);
		//webcam.setImageTransformer(gray);
		//webcam.setImageTransformer(ff);
		//webcam.setImageTransformer(bgf);
		webcam.open();


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
		setupGameModeSwitch();
		detector.start();


		window.add(panel);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		FunMirror fm = new FunMirror();
		fm.run();
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
				webcam.setImageTransformer(wf); // WaveFilter
				break;
			case 1:
				webcam.setImageTransformer(pf); // PinchFilter
				break;
			case 2:
				webcam.setImageTransformer(gray); // GrayFilter
				break;
			case 3:
				webcam.setImageTransformer(wef); // WaterEffectFilter, líkist smá pinch filter eftir allar breytingarnar
				break;							 // en við getum experimentað meira.
			case 4:
				webcam.setImageTransformer(ff); // FrameFilter
				break;
		}

		System.out.println("Current gamemode: " + currentGameMode);
	}
}
