package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.RippleFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class WaveFilter implements WebcamImageTransformer, WebcamListener {

    // Gerði annan Constructor fyrir RippleFilter til að prufa effectana. (5.0f, 0, 16.0f) er default.
    private static final RippleFilter RIPPLE_FILTER = new RippleFilter();

    public WaveFilter() {

    }

    public WaveFilter(float xAmp, float yAmp, float xyWaveLength) {
        RIPPLE_FILTER.setXAmplitude(xAmp);
        RIPPLE_FILTER.setYAmplitude(yAmp);
        RIPPLE_FILTER.setXWavelength(xyWaveLength);
        RIPPLE_FILTER.setYWavelength(xyWaveLength);
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
