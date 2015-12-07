package filters;

import com.github.sarxos.webcam.*;
import com.sun.glass.ui.Timer;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Date;

/**
 * Created by Gvendurst on 4.12.2015.
 *
 */
public class BackgroundFilter implements WebcamMotionListener, WebcamListener, WebcamImageTransformer{
	private BufferedImage backgroundImage;
	private BufferedImage currentImage;
	private double minTime; //In milliseconds
	private double maxArea;
	private long lastMotionTime = System.currentTimeMillis();
	private boolean active = true;
	private filters.GrayFilter gray = new GrayFilter();

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public double getMinTime() {
		return minTime;
	}

	public void setMinTime(double minTime) {
		this.minTime = minTime;
	}

	public double getMaxArea() {
		return maxArea;
	}

	public void setMaxArea(double maxArea) {
		this.maxArea = maxArea;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BackgroundFilter() {
	}

	public BackgroundFilter(double maxArea, double minTime) {
		this.maxArea = maxArea;
		this.minTime = minTime;
	}

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		System.out.println(webcamMotionEvent.getArea());
		//Will likely use the picture after the motion as a background
		//Should this check be moved to webcamImageObtained?
		//And should the background image be a combination of a few frames?
		if((System.currentTimeMillis() - lastMotionTime) >= minTime){
			//We have found a background
			backgroundImage = currentImage;

			System.out.println("New background image found");
		}

		if(webcamMotionEvent.getArea() > maxArea) {
			lastMotionTime = System.currentTimeMillis();
		}
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
		currentImage = webcamEvent.getImage();
	}

	@Override
	public BufferedImage transform(BufferedImage bufferedImage) {
		if(!active || backgroundImage == null){
			return bufferedImage;
		}

		BufferedImage retVal = createCompatibleDestImage(bufferedImage, null);

		for(int i = 0; i < bufferedImage.getWidth(); i++){
			for(int j = 0; j < bufferedImage.getHeight(); j++){
				int foreground = bufferedImage.getRGB(i,j);
				int background = backgroundImage.getRGB(i,j);
				int difference = Math.abs((foreground & 0xff) - (background & 0xff))
						+ Math.abs(((foreground & 0xff00) >> 8) - ((background & 0xff00) >> 8))
						+ Math.abs(((foreground & 0xff0000) >> 16) - ((background & 0xff0000) >> 16))
						+ Math.abs(((foreground & 0xff000000) >> 24) - ((background & 0xff000000) >> 24));
				if(difference < 75){
					//Seems to affect the motion sensing
					retVal.setRGB(i,j,0xff0000);
				}
				else {
					retVal.setRGB(i, j, foreground);
				}
			}
		}

		return retVal;
	}

	private BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
		if ( dstCM == null )
			dstCM = src.getColorModel();
		return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
	}
}
