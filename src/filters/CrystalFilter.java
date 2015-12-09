package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;

import java.awt.image.BufferedImage;

import com.jhlabs.image.CrystallizeFilter;
import com.jhlabs.image.RippleFilter;

/**
 * Created by Hoddi on 7.12.2015.
 */
public class CrystalFilter implements WebcamImageTransformer, WebcamListener {

    private static final CrystallizeFilter CRYSTALLIZE_FILTER = new CrystallizeFilter();

    public CrystalFilter() {

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
        return CRYSTALLIZE_FILTER.filter(image, null);
    }
}
