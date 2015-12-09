package filters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
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

        BufferedImage bgImage = createCompatibleDestImage(image, null);
        
        Graphics2D g2 = bgImage.createGraphics();
        g2.drawImage(image, null, 0, 0);
        g2.drawImage(funFrame, null, 0, 0);
        g2.dispose();

        bgImage.flush();
    
        return bgImage;
    }

    private static final BufferedImage getImage(String image) {
        try {
            return ImageIO.read(FrameFilter.class.getResourceAsStream("/" + image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel();
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }
}

