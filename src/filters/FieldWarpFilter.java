package filters;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Hoddi on 12.12.2015.
 */
public class FieldWarpFilter extends JFrame implements WebcamPanel.Painter {

    private final Webcam webcam;
    private final WebcamPanel panel;
    private final WebcamMotionDetector detector;
    private FieldWarpTest fieldWarpTest;

    public FieldWarpFilter() {

        setTitle("Motion Detector Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fieldWarpTest = new FieldWarpTest();

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open(true);
        webcam.setImageTransformer(fieldWarpTest);

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
        new FieldWarpFilter();
    }

    @Override
    public void paintPanel(WebcamPanel panel, Graphics2D g2) {
        panel.getDefaultPainter().paintPanel(panel, g2);
    }

    @Override
    public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

        double s = detector.getMotionArea();

        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.drawString(String.format("Area: %.2f%%", s), 10, 20);

        // InLines
        g.drawLine(100, 0, 100,480); //in 0
        g.drawLine(200, 0, 200,480); //out 0

        // OutLines
        g.drawLine(500, 0, 500,480); // in 1
        g.drawLine(400, 0, 400, 480); // out 1


        g.dispose();

        panel.getDefaultPainter().paintImage(panel, image, g2);
    }
}
