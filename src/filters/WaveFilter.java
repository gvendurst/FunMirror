package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.WebcamListener;
import com.jhlabs.image.RippleFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class WaveFilter implements WebcamImageTransformer, WebcamListener {

    private static final RippleFilter RIPPLE_FILTER = new RippleFilter(5.0f);

    public WaveFilter() {

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
        return RIPPLE_FILTER.filter(image, null);
    }
}
