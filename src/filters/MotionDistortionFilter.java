package filters;

import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

import java.awt.image.BufferedImage;

/**
 * Created by Gvendurst on 4.12.2015.
 */
public class MotionDistortionFilter implements WebcamImageTransformer, WebcamMotionListener {
	private WebcamMotionEvent lastEvent;

	@Override
	public BufferedImage transform(BufferedImage bufferedImage) {
		if(lastEvent == null){
			return bufferedImage;
		}



		return bufferedImage;
	}

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		lastEvent = webcamMotionEvent;
	}
}
