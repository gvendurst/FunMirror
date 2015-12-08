package filters;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.jhlabs.image.PinchFilter;

import java.awt.image.BufferedImage;

/**
 * Created by Gvendurst on 8.12.2015.
 */
public class PinchFilter2 implements WebcamImageTransformer, WebcamListener {
	private static final com.jhlabs.image.PinchFilter PINCH_FILTER = new com.jhlabs.image.PinchFilter();

	public PinchFilter2() {
		PINCH_FILTER.setAmount(0.5f);
		PINCH_FILTER.setRadius(200);
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
