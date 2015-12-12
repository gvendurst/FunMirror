package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.*;
import com.jhlabs.image.FieldWarpFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class FieldWarpTest implements WebcamImageTransformer, WebcamListener {

    private static final com.jhlabs.image.FieldWarpFilter FIELD_WARP_FILTER = new FieldWarpFilter();

    public FieldWarpTest() {
        FIELD_WARP_FILTER.setAmount(0.4f);
        FIELD_WARP_FILTER.setPower(0.5f);
        FIELD_WARP_FILTER.setStrength(0.5f);
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
        return FIELD_WARP_FILTER.filter(image, null);
    }
}
