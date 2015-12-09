package filters;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Gvendurst on 9.12.2015.
 */
public class FacePainter implements Runnable, WebcamPanel.Painter {
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();
	private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f }, 0.0f);
	private WebcamPanel.Painter painter = null;
	private java.util.List<DetectedFace> faces = null;
	private BufferedImage troll = null;
	private Webcam webcam;
	private WebcamPanel panel;


	public FacePainter(Webcam webcam, WebcamPanel panel){
		this.webcam = webcam;
		this.panel = panel;
	}

	public void start(){
		try {
			troll = ImageIO.read(getClass().getResourceAsStream("/troll-face.png"));
		}
		catch (IllegalArgumentException e){
			System.out.println("Image not found( in class FacePainter)");
		}
		catch (IOException e){
			System.out.println("Error opening file( in class FacePainter)");
		}


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
			Rectangle bounds = face.getBounds();

			int dx = (int) (0.1 * bounds.width);
			int dy = (int) (0.2 * bounds.height);
			int x = (int) bounds.x - dx;
			int y = (int) bounds.y - dy;
			int w = (int) bounds.width + 2 * dx;
			int h = (int) bounds.height + dy;

			g2.drawImage(troll, x, y, w, h, null);
		}
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
