package filters;

import com.github.sarxos.webcam.*;
import project.FunMirror;
import project.PointDouble;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by Gvendurst on 8.12.2015.
 */
public class MultiPinchFilter implements WebcamImageTransformer, WebcamMotionListener {
	private static final com.jhlabs.image.PinchFilter PINCH_FILTER = new com.jhlabs.image.PinchFilter();
	private ArrayList<PointDouble> lastPoints;
	private ArrayList<PointDouble> lastPoints2;
	private boolean useLastPoints1 = true;
	Semaphore pointSem = new Semaphore(1,true);

	public MultiPinchFilter() {
		PINCH_FILTER.setAmount(0.5f);
		PINCH_FILTER.setRadius(200);
	}

	public BufferedImage transform(BufferedImage image) {
		/*
		if(lastPoints == null && lastPoints2 == null){
			return image;
		}
		*/

		System.out.println("Editing");
		BufferedImage retVal = image;
		boolean first = true;
		ArrayList<Point> theList = null;

		theList = new ArrayList<Point>();
		theList.add(new Point(100,100));
		theList.add(new Point(300,300));

		for (PointDouble p : PointListToPointDoubleList(theList)) {
			PINCH_FILTER.setCentreX((float)p.getX());
			PINCH_FILTER.setCentreY((float)p.getY());
			System.out.println("(" + PINCH_FILTER.getCentreX() + " , " + PINCH_FILTER.getCentreY() + ")");
			System.out.println("2");
			retVal = PINCH_FILTER.filter(retVal, null);
		}
		//pointSem.release();

		System.out.println("3");

		return retVal;
	}

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		System.out.println("Acquiring");
		if(lastPoints == null){
			lastPoints = PointListToPointDoubleList(webcamMotionEvent.getPoints());
		}
		else if(lastPoints2 == null){
			lastPoints2 = PointListToPointDoubleList(webcamMotionEvent.getPoints());
		}
		else{
			System.out.println("No write action");
		}

		System.out.println(lastPoints);
	}

	private PointDouble PointPixelsToPercentage(Point p, int totalX, int totalY){
		return new PointDouble(p.x / (double)totalX, p.y / (double)totalY);
	}

	private ArrayList<PointDouble> PointListToPointDoubleList(ArrayList<Point> list){
		ArrayList<PointDouble> retVal = new ArrayList<>();

		for(Point p : list){
			retVal.add(PointPixelsToPercentage(p, FunMirror.getScreenSizeX(), FunMirror.getScreenSizeY()));
		}

		return retVal;
	}
}