package project;

import com.github.sarxos.webcam.*;
import filters.*;
import utils.Facing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class FunMirror implements GameModeSwitch {
	private static FunMirror funMirror;
	private MotionDistortionFilter mdf = new MotionDistortionFilter();
	private WebcamMotionDetector detector;
	//private BackgroundFilter bgf;
	private filters.WaterEffectFilter wef;
	private filters.WaveFilter wf;
	private filters.GrayFilter gray;
	private filters.FrameFilter ff;
	private filters.CrystalFilter cf;
	private filters.EmbossFilter ef;
	private PinchFilter2 pf;
	private MultiPinchFilter mpf;
	private TwirlMotionFilter tmf;
	private PinchMotionFilter pmf;

	private GameModeSwitchDetector gms;
	private int currentDistortionGameMode = 0;
	private int currentImageGameMode = 0;
	private final int numberOfDistortionGameModes = 4;
	private final int numberOfImageGameModes = 6;
	private Webcam webcam;
	private WebcamPanel panel;
	private FacePainter facePainter1;
	private GifFacePainter facePainter2;
	private BodyPainter bodyPainter;
	private AcrobatPainter acrobatPainter;
	private MonkeyFacePainter monkeyFacePainter;
	private HairyGuyBodyPainter hairyGuyBodyPainter;

	private JTextArea text = new JTextArea("");
	private int lastArgs = 0;

	public FunMirror() {

	}

	public void start(){

		// Ekki notaðir filterar
		gray = new filters.GrayFilter();
		ff = new filters.FrameFilter();
		cf = new filters.CrystalFilter();
		ef = new filters.EmbossFilter();
		pf = new PinchFilter2();
		mpf = new MultiPinchFilter();

		// Notaðir filterar
		// (float xAmplitude, float yAmplitude, float xyWaveLength)
		wf = new filters.WaveFilter(5.0f, 10.0f, 20.0f);
		// (float amplitude, float phase, float radius, float wavelength)
		wef = new filters.WaterEffectFilter(0.1f, 50.0f, 700.0f, 200.0f);


		// Gamemode texta stillingar
		text.setOpaque(false);
		text.setEnabled(false);

		//bgf = new BackgroundFilter();
		//bgf.setMaxArea(1);
		//bgf.setMinTime(5000);
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		//System.out.println(webcam.getViewSize());

		webcam.open();

		//webcam.addWebcamListener(bgf);
		//webcam.addWebcamListener(wf);


		JFrame window = new JFrame("Interactive fun mirror");

		panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);
		panel.add(text);

		detector = new WebcamMotionDetector(webcam);
		detector.setInterval(100); // one check per x ms
		detector.setPixelThreshold(20);

		detector.addMotionListener(mdf);
		//detector.addMotionListener(bgf);
		detector.addMotionListener(mpf);
		//detector.setMaxMotionPoints(3); //Default is 100
		detector.setPointRange(40);
		detector.start();

		panel.setMirrored(true);


		tmf = new TwirlMotionFilter(webcam, panel, detector);
		// (Webcam webcam, WebcamPanel panel, WebcamMotionDetector detector, int angle)
		// Angle is the circle-twirl effect, multiplied by the movement detector (0-left to 1-right)
		pmf = new PinchMotionFilter(webcam, panel, detector, 3);

		facePainter1 = new FacePainter(webcam, panel);
		facePainter2 = new GifFacePainter(webcam, panel);
		bodyPainter = new BodyPainter(webcam, panel);
		acrobatPainter = new AcrobatPainter(webcam,panel);
		monkeyFacePainter = new MonkeyFacePainter(webcam,panel);
		hairyGuyBodyPainter = new HairyGuyBodyPainter(webcam, panel);


		//Sets the first gamemode
		currentDistortionGameMode = numberOfDistortionGameModes - 1;
		currentImageGameMode = numberOfImageGameModes - 1;
		setupGameModeSwitch(panel);
		//onGameModeSwitch(0);


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

	private void setupGameModeSwitch(WebcamPanel panel){
		gms = new GameModeSwitchDetector(panel);
		gms.setMinTime(3000);
		gms.addPoint(100,getScreenSizeY() - 100, Facing.LEFT, 0);
		gms.addPoint(100,100, Facing.LEFT, 1);
		gms.addPoint(getScreenSizeX() - 100,100, Facing.RIGHT, 2);
		gms.addPoint(getScreenSizeX() - 100, getScreenSizeY() - 100, Facing.RIGHT, 3);
		gms.setMinHands(2);

		gms.setPointRadius(40);
		gms.setHandScale(0.15);
		gms.setMinPoints(2);
		detector.addMotionListener(gms);
		gms.addGameModeSwitch(this);
		gms.initialGameModeSwitch();
	}

	@Override
	public void onGameModeSwitch(int args) {
		System.out.println("Game mode changed. args: " + args);
		stopCurrentGameMode();
		if(args % 2 == 0) {
			startNextDistortionGameMode();
		}
		else{
			startNextImageGameMode();
		}
		lastArgs = args;
	}

	private void stopCurrentGameMode(){
		//Dispose the current game mode
		if(lastArgs % 2 == 0) {
			//Current mode is a distortion mode
			switch (currentDistortionGameMode) {
				case 1:
					pmf.stop();
					break;
				case 3:
					tmf.stop();
					break;
				default:
					webcam.setImageTransformer(null);
					if (gms != null) {
						gms.setMinAreaDefault();
					}
					break;
			}
		}
		else{
			//Current mode is an image mode
			switch(currentImageGameMode){
				case 0:
					hairyGuyBodyPainter.stop();
					break;
				case 1:
					facePainter1.stop();
					break;
				case 2:
					facePainter2.stop();
					break;
				case 3:
					bodyPainter.stop();
					break;
				case 4:
					acrobatPainter.stop();
					break;
				case 5:
					monkeyFacePainter.stop();
					break;
				default:
					webcam.setImageTransformer(null);
					if (gms != null) {
						gms.setMinAreaDefault();
					}
					break;
			}
		}
	}

	private void startNextDistortionGameMode(){
		System.out.println("Starting distortion gamemode");
		currentDistortionGameMode = (currentDistortionGameMode + 1) % numberOfDistortionGameModes;

		//Setup the next distortion game mode
		switch (currentDistortionGameMode){
			case 0:
				webcam.setImageTransformer(wf); // WaveFilter DONE
				text.setText("Distortion gamemode 1");
				break;
			case 1:
				pmf.start();	//Movement changing PinchFilter2, modified.
				text.setText("Distortion gamemode 2");
				break;
			case 2:
				gms.setMinArea(30);
				webcam.setImageTransformer(wef);     // WaterEffectFilter, líkist smá pinch filter eftir allar breytingarnar
				text.setText("Distortion gamemode 3"); // en við getum experimentað meira.
				break;
			case 3:
				tmf.start(); // TwirlMotionFilter
				text.setText("Distortion gamemode 4");
				break;
		}

		text.setText("Man, you're funky!");
	}

	private void startNextImageGameMode(){
		System.out.println("Starting image gamemode");
		currentImageGameMode = (currentImageGameMode + 1) % numberOfImageGameModes;
		switch (currentImageGameMode){
			case 0:
				hairyGuyBodyPainter.start();
				break;
			case 1:
				facePainter1.start();
				text.setText("Image gamemode 1");
				break;
			case 2:
				facePainter2.start();
				text.setText("Image gamemode 2");
				break;
			case 3:
				bodyPainter.start();
				text.setText("Image gamemode 3");
				break;
			case 4:
				acrobatPainter.start();
				break;
			case 5:
				monkeyFacePainter.start();
				break;
		}
		text.setText("Definitely not you");
	}
}
