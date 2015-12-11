package project;

import com.github.sarxos.webcam.*;
import filters.*;
import javafx.scene.paint.Color;
import utils.Facing;

import javax.swing.*;

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
	private GameModeSwitchDetector gms;
	private int currentGameMode = 0;
	private final int numberOfGameModes = 5;
	private Webcam webcam;
	private FacePainter facePainter1;
	private GifFacePainter facePainter2;
	private JTextArea text = new JTextArea("");

	public FunMirror() {

	}

	public void start(){
		gray = new filters.GrayFilter();
		wf = new filters.WaveFilter();
		pf = new PinchFilter2();
		mpf = new MultiPinchFilter();
		wef = new WaterEffectFilter();
		ff = new filters.FrameFilter();
		cf = new filters.CrystalFilter();
		ef = new filters.EmbossFilter();

		// Gamemode texta stillingar
		text.setOpaque(false);
		text.setEnabled(false);

		//bgf = new BackgroundFilter();
		//bgf.setMaxArea(1);
		//bgf.setMinTime(5000);
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());


		webcam.open();

		//Sets the first gamemode
		currentGameMode = numberOfGameModes - 1;
		onGameModeSwitch(0);


		//webcam.addWebcamListener(bgf);
		//webcam.addWebcamListener(wf);


		JFrame window = new JFrame("Test Transformer");

		WebcamPanel panel = new WebcamPanel(webcam);
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
		setupGameModeSwitch(panel);
		detector.start();

		panel.setMirrored(true);

		facePainter1 = new FacePainter(webcam, panel);
		//facePainter1.setScaleX(1.3);
		//facePainter1.setScaleY(1.4);

		facePainter2 = new GifFacePainter(webcam, panel);
		//facePainter2.setScaleX(1.4);
		//facePainter2.setScaleY(1.6);
		//facePainter2.setOffsetY(-50);

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
		gms.addPoint(100,100, Facing.LEFT);
		gms.setPointRadius(40);
		gms.setHandScale(0.2);
		gms.setMinPoints(2);
		detector.addMotionListener(gms);
		gms.addGameModeSwitch(this);
	}

	@Override
	public void onGameModeSwitch(int args) {
		//Dispose the current game mode
		switch (currentGameMode){
			case 1:
				facePainter1.stop();
				break;
			case 3:
				facePainter2.stop();
				break;
			default:
				webcam.setImageTransformer(null);
				if(gms != null) {
					gms.setMinAreaDefault();
				}
				break;
		}


		System.out.println("Game mode changed");
		currentGameMode = (currentGameMode + 1) % numberOfGameModes;

		//Setup the next game mode
		switch (currentGameMode){
			case 0:
				webcam.setImageTransformer(wf); // WaveFilter DONE
				text.setText("Fun Gamemode Title1");
				break;
			case 1:
				facePainter1.start();
				text.setText("Fun Gamemode Title2");
				break;
			case 2:
				webcam.setImageTransformer(pf); // PinchFilter
				text.setText("Fun Gamemode Title3");
				break;
			case 3:
				facePainter2.start();
				text.setText("Fun Gamemode Title4");
				break;
			case 4:
				gms.setMinArea(30);
				webcam.setImageTransformer(wef);     // WaterEffectFilter, líkist smá pinch filter eftir allar breytingarnar
				text.setText("Fun Gamemode Title5"); // en við getum experimentað meira.
				break;
			case 5:
				webcam.setImageTransformer(ff); // FrameFilter
				text.setText("Fun Gamemode Title6");
				break;
		}

		System.out.println("Current gamemode: " + currentGameMode);
	}
}
