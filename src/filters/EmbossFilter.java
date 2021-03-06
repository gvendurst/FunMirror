package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.jhlabs.image.ImageMath;

import java.awt.image.BufferedImage;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class EmbossFilter implements WebcamImageTransformer, WebcamListener {

    private static final com.jhlabs.image.EmbossFilter EMBOSS_FILTER = new com.jhlabs.image.EmbossFilter(400.0f * ImageMath.PI / 180.0f,70.0f * ImageMath.PI / 180.0f);

    public EmbossFilter() {

    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {

    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {

    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {

    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {

    }

    public BufferedImage transform(BufferedImage image) {
        return EMBOSS_FILTER.filter(image, null);
    }
}
