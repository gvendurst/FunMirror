package filters;

import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Gvendurst on 7.12.2015.
 */
public class GrayFilter implements WebcamImageTransformer {
	private JHGrayFilter gray;

	public GrayFilter(){
		gray = new JHGrayFilter();
	}

	@Override
	public BufferedImage transform(BufferedImage bufferedImage) {
		gray.filter(bufferedImage, bufferedImage);

		return bufferedImage;
	}
}

