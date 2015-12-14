package filters;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import project.FunMirror;
import utils.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Gvendurst on 12.12.2015.
 */
public class BodyPainter implements Runnable, WebcamPanel.Painter {
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();
	private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f }, 0.0f);
	private WebcamPanel.Painter painter = null;
	private java.util.List<DetectedFace> faces = null;
	private BufferedImage image = null;
	private Webcam webcam;
	private WebcamPanel panel;
	private final String fileName = "/clown2.png";


	public BodyPainter(Webcam webcam, WebcamPanel panel){
		this.webcam = webcam;
		this.panel = panel;
	}

	public void start(){
		image = ImageLoader.loadImage(fileName);


		painter = panel.getDefaultPainter();
		panel.setPainter(this);

		EXECUTOR.execute(this);

	}

	public void stop(){
		panel.setPainter(panel.getDefaultPainter());
	}



	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		if (painter != null) {
			painter.paintPanel(panel, g2);
		}


	}

	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
		if (painter != null) {
			painter.paintImage(panel, image, g2);
		}

		if (faces == null) {
			return;
		}

		Iterator<DetectedFace> dfi = faces.iterator();
		while (dfi.hasNext()) {
			DetectedFace face = dfi.next();
			org.openimaj.math.geometry.shape.Rectangle bounds = face.getBounds();

			int dx = (int) (0.1 * bounds.width);
			int dy = (int) (0.2 * bounds.height);
			int x = (int) bounds.x - dx;
			int y = (int) bounds.y - dy;
			int w = (int) bounds.width + 2 * dx;
			int h = (int) bounds.height + dy;


			drawImage(this.image, g2,x,y,w,h,3.25,6.5,0,0);
		}

	}

	private void drawImage(BufferedImage image, Graphics2D g2, int x, int y, int w, int h, double xScale, double yScale, double xOffset, double yOffset){
		g2.drawImage(image,
				FunMirror.getScreenSizeX()
						- (int)(x + (w*xScale - ((xScale - 1) *w*0.5)) - xOffset*w*xScale),
				(int)(y + yOffset*(h*0.5)*yScale),
				(int)(w * xScale),
				(int)(h * yScale),
				null);
	}

	@Override
	public void run() {
		while (true) {
			if (!webcam.isOpen()) {
				return;
			}
			faces = detector.detectFaces(ImageUtilities.createFImage(webcam.getImage()));
		}
	}
}
