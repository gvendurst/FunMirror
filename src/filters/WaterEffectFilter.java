package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.WaterFilter;

/**
 * Created by Hoddi on 9.12.2015.
 */
public class WaterEffectFilter implements WebcamImageTransformer, WebcamListener {

    // Gerði annan contructor fyrir WaterFilter til að experimenta.
    // WaterFilter(float setAmplitude, float setPhase, float setRadius, float setWavelength)
    private static final WaterFilter WATER_FILTER = new WaterFilter(0.1f, 50.0f, 700.0f, 200.0f);

    public WaterEffectFilter() {

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
        return WATER_FILTER.filter(image, null);
    }
}

