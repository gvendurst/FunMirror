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
import filters.TwirlMotionFilter;


public class ImageTransformerExample implements WebcamImageTransformer {

	private static final JHGrayFilter GRAY = new JHGrayFilter();
	private static final GrayFilter GRAY_NEW = new GrayFilter();
	private static final CircleFilter CIRCLE_FILTER = new CircleFilter();
	private static final DissolveFilter DISSOLVE_FILTER = new DissolveFilter();
	private static final NoiseFilter NOISE_FILTER = new NoiseFilter();
	private static final GlowFilter GLOW_FILTER = new GlowFilter();
	private static final WaterFilter WATER_FILTER = new WaterFilter();
	private static final CrystallizeFilter CRYSTALLIZE_FILTER = new CrystallizeFilter();
	private static final EmbossFilter EMBOSS_FILTER = new EmbossFilter();
	private static final RippleFilter RIPPLE_FILTER = new RippleFilter(5.0f, 10.0f, 20.0f);
	private static final FieldWarpFilter FIELD_WARP_FILTER = new FieldWarpFilter();
	private static final KaleidoscopeFilter KALEIDOSCOPE_FILTER = new KaleidoscopeFilter();
	private static final MarbleFilter MARBLE_FILTER = new MarbleFilter();
	private static final TwirlFilter TWIRL_FILTER = new TwirlFilter(1.0f, 0.5f, 0.5f);
	private static final PinchFilter PINCH_FILTER = new PinchFilter();

	public ImageTransformerExample() {

		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.setImageTransformer(this);
		webcam.open();

		FIELD_WARP_FILTER.setAmount(0.4f);
		FIELD_WARP_FILTER.setPower(0.5f);
		FIELD_WARP_FILTER.setStrength(0.5f);

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
		return FIELD_WARP_FILTER.filter(image, null);
	}

	public static void main(String[] args) {
		new ImageTransformerExample();
	}

}
