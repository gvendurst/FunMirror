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
public class FieldWarpTest extends FieldWarpFilter implements WebcamImageTransformer, WebcamListener {

    private static final FieldWarpFilter FIELD_WARP_FILTER = new FieldWarpFilter();
    private Line[] inLines = new Line[2];
    private Line[] outLines = new Line[2];

    public FieldWarpTest() {
        inLines[0] = new Line(200, 100, 200,480);
        outLines[0] = new Line(300, 100, 300, 480);
        inLines[1] = new Line(400, 100, 400,480);
        outLines[1] = new Line(300, 100, 300, 480);

        FIELD_WARP_FILTER.setAmount(0.4f);
        FIELD_WARP_FILTER.setPower(0.5f);
        FIELD_WARP_FILTER.setStrength(0.5f);

        FIELD_WARP_FILTER.setInLines(inLines);
        FIELD_WARP_FILTER.setOutLines(outLines);
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
