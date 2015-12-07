package filters;

import com.github.sarxos.webcam.WebcamImageTransformer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Gvendurst on 3.12.2015.
 */
public class TestFilter implements WebcamImageTransformer {
	@Override
	public BufferedImage transform(BufferedImage bufferedImage) {
		int[] rgbArray = new int[100];
		for(int i = 0 ; i < rgbArray.length; i++ ) {
			rgbArray[i] = Color.red.getRGB();
		}
		BufferedImage bi = bufferedImage;

		bi.setRGB(5, 5, 10, 10, rgbArray, 0, 1);

		return bi;
	}
}
