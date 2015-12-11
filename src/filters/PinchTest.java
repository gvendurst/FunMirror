package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.PinchFilter;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TwirlFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class PinchTest implements WebcamImageTransformer, WebcamListener {

    private static final PinchFilter PINCH_FILTER = new PinchFilter(-0.5f, 150);

    public PinchTest() {

    }

    public PinchTest(float x, float y) {
        PINCH_FILTER.setCentreX(x);
        PINCH_FILTER.setCentreY(y);
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
        return PINCH_FILTER.filter(image, null);
    }
}
