package filters;

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
 * Created by Hoddi on 12.12.2015.
 */
public class PinchMotionFilter extends JFrame implements WebcamPanel.Painter {

    private final Webcam webcam;
    private final WebcamPanel panel;
    private final WebcamMotionDetector detector;
    private PinchFilter2 pinch;


    public PinchMotionFilter(Webcam webcam, WebcamPanel panel, WebcamMotionDetector detector){
        this.webcam = webcam;
        this.panel = panel;
        this.detector = detector;

        pinch = new PinchFilter2();
    }

    public void start(){
        webcam.setImageTransformer(pinch);
        panel.setPainter(this);
    }

    public void stop(){
        webcam.setImageTransformer(null);
        panel.setPainter(panel.getDefaultPainter());
    }

    @Override
    public void paintPanel(WebcamPanel panel, Graphics2D g2) {
        panel.getDefaultPainter().paintPanel(panel, g2);
    }

    @Override
    public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

        Point cog = detector.getMotionCog();

        Graphics2D g = image.createGraphics();

        if (detector.isMotion()) {
            float x = ((((float)cog.getX() - 0) * (1 - 0)) / (640 - 0)) + 0;
            pinch = new PinchFilter2(3*x);
        }
        else {

        }

        g.dispose();

        panel.getDefaultPainter().paintImage(panel, image, g2);
    }
}
