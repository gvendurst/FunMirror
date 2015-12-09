package examples;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;

import com.jhlabs.image.*;
import com.sun.scenario.effect.Glow;


public class ImageTransformerExample implements WebcamImageTransformer {

	private static final JHGrayFilter GRAY = new JHGrayFilter();
	private static final GrayFilter GRAY_NEW = new GrayFilter();
	private static final CircleFilter CIRCLE_FILTER = new CircleFilter();
	private static final DissolveFilter DISSOLVE_FILTER = new DissolveFilter();
	private static final NoiseFilter NOISE_FILTER = new NoiseFilter();
	private static final RippleFilter RIPPLE_FILTER = new RippleFilter();
	private static final GlowFilter GLOW_FILTER = new GlowFilter();
	private static final WaterFilter WATER_FILTER = new WaterFilter();
	private static final CrystallizeFilter CRYSTALLIZE_FILTER = new CrystallizeFilter();
	private static final EmbossFilter EMBOSS_FILTER = new EmbossFilter();

	public ImageTransformerExample() {

		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.setImageTransformer(this);
		webcam.open();

		JFrame window = new JFrame("Test Transformer");

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);

		window.add(panel);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public BufferedImage transform(BufferedImage image) {
		return EMBOSS_FILTER.filter(image, null);
	}

	public static void main(String[] args) {
		new ImageTransformerExample();
	}

}
