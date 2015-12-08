package project;

import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import filters.BackgroundFilter;
import filters.MotionDistortionFilter;

import javax.swing.*;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class FunMirror {
	private MotionDistortionFilter mdf = new MotionDistortionFilter();
	private WebcamMotionDetector detector;
	private BackgroundFilter bgf;
	private filters.WaveFilter wf;
	private filters.GrayFilter gray;

	public FunMirror() {

	}

	public void run(){
		gray = new filters.GrayFilter();
		wf = new filters.WaveFilter();

		bgf = new BackgroundFilter();
		bgf.setMaxArea(1);
		bgf.setMinTime(5000);


		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		//webcam.setImageTransformer(gray);
		//webcam.setImageTransformer(wf);
		webcam.setImageTransformer(bgf);
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
}
