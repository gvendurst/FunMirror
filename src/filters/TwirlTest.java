package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TwirlFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class TwirlTest implements WebcamImageTransformer, WebcamListener {

    private static final TwirlFilter TWIRL_FILTER = new TwirlFilter(1.0f, 0.5f, 0.5f);

    public TwirlTest() {

    }

    public TwirlTest(float x, float y) {
        TWIRL_FILTER.setCentreX(x);
        TWIRL_FILTER.setCentreY(y);
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
        return TWIRL_FILTER.filter(image, null);
    }
}
