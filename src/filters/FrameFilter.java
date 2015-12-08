package filters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.github.sarxos.webcam.WebcamImageTransformer;


/**
 * Created by HörðurMár on 8.12.2015.
 */
public class FrameFilter implements WebcamImageTransformer {

    private static final BufferedImage funFrame = getImage("frame.png");

    public FrameFilter() {

    }

    @Override
    public BufferedImage transform(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage modified = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = modified.createGraphics();
        g2.drawImage(image, null, 0, 0);
        g2.drawImage(funFrame, null, 0, 0);
        g2.dispose();

        modified.flush();

        return modified;
    }

    private static final BufferedImage getImage(String image) {
        try {
            return ImageIO.read(FrameFilter.class.getResourceAsStream("/" + image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

