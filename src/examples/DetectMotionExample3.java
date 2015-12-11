package examples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.jhlabs.image.TwirlFilter;
import filters.PinchTest;
import filters.TwirlTest;


/**
 * Detect motion. This example demonstrates how to use build-in motion detector
 * and motion listener to fire motion events.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class DetectMotionExample3 extends JFrame implements WebcamPanel.Painter {

	private static final long serialVersionUID = 1L;

	private final Webcam webcam;
	private final WebcamPanel panel;
	private final WebcamMotionDetector detector;
	private TwirlTest twirlTest;
	private PinchTest pinchTest;

	public DetectMotionExample3() {

		setTitle("Motion Detector Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		twirlTest = new TwirlTest();
		pinchTest = new PinchTest();

		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.open(true);
		webcam.setImageTransformer(twirlTest);

		panel = new WebcamPanel(webcam, false);
		panel.setPainter(this);
		panel.start();

		detector = new WebcamMotionDetector(webcam);
		detector.setInterval(100); // one check per 500 ms
		detector.setPixelThreshold(20);
		detector.start();


		add(panel);

		pack();
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new DetectMotionExample3();
	}

	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		panel.getDefaultPainter().paintPanel(panel, g2);
	}

	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

		double s = detector.getMotionArea();
		Point cog = detector.getMotionCog();
		Point2D cog2 = detector.getMotionCog();

		Graphics2D g = image.createGraphics();
		g.setColor(Color.WHITE);
		g.drawString(String.format("Area: %.2f%%", s), 10, 20);

		if (detector.isMotion()) {
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.RED);
			g.drawOval(cog.x - 5, cog.y - 5, 10, 10);
			float x = ((((float)cog2.getX() - 0) * (1 - 0)) / (640 - 0)) + 0;
			float y = ((((float)cog2.getY() - 0) * (1 - 0)) / (480 - 0)) + 0;
			System.out.println("x:" + x); //w=640xx h=480yy
			System.out.println("y:" + y); //w=640xx h=480yy
			twirlTest = new TwirlTest(x, y);
			if(detector.getMotionArea() > 35) {
				pinchTest = new PinchTest(x, y);
				webcam.setImageTransformer(pinchTest);
			}
			else {
				webcam.setImageTransformer(twirlTest);
			}
		} else {
			g.setColor(Color.GREEN);
			g.drawRect(cog.x - 5, cog.y - 5, 10, 10);
		}

		g.dispose();

		panel.getDefaultPainter().paintImage(panel, image, g2);
	}
}
