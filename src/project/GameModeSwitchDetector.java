package project;

import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;
import utils.Facing;
import utils.ImageLoader;
import utils.PicturePoint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gvendurst on 8.12.2015.
 */
public class GameModeSwitchDetector implements WebcamMotionListener,WebcamPanel.Painter{
	private double minTime;
	private double lastMotion;
	private double minArea;
	private final static double MIN_AREA_DEFAULT = 40;
	private ArrayList<PicturePoint> points = new ArrayList<PicturePoint>();
	private double pointRadius;
	private int minPoints;
	private int minHands = 1;
	private WebcamPanel.Painter lastPainter;

	private BufferedImage handImageLeft;
	private BufferedImage handImageRight;
	private BufferedImage handDistortionLeft;
	private BufferedImage handDistortionRight;



	private String fileNameDistortionLeft = "/haegrilodin.png";
	private String fileNameDistortionRight = "/vinstrilodin.png";
	private String fileNameImageLeft = "/vinstribla.png";
	private String fileNameImageRight = "/haegribla.png";

	private WebcamPanel panel;
	private WebcamPanel.Painter painter;
	private double handScale = 0.25;
	private ArrayList<PicturePoint> pointsMovedThisTime = new ArrayList<>();

	private ArrayList<GameModeSwitch> listeners;

	public GameModeSwitchDetector(WebcamPanel panel){
		listeners = new ArrayList<>();
		minArea = MIN_AREA_DEFAULT;
		this.panel = panel;

		handImageLeft = ImageLoader.loadImage(fileNameImageLeft);
		handImageRight = ImageLoader.loadImage(fileNameImageRight);
		handDistortionLeft = ImageLoader.loadImage(fileNameDistortionLeft);
		handDistortionRight = ImageLoader.loadImage(fileNameDistortionRight);


		painter = panel.getPainter();
		lastPainter = painter;
		panel.setPainter(this);
	}

	public void addPoint(int x, int y, Facing facing, int id){
		points.add(new PicturePoint(x,y,facing, id));
	}

	public void addPoint(int x, int y, Facing facing){
		points.add(new PicturePoint(x,y,facing));
	}

	public double getPointRadius() {
		return pointRadius;
	}

	public void setPointRadius(double pointRadius) {
		this.pointRadius = pointRadius;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}

	public double getMinTime() {
		return minTime;
	}

	public int getMinHands() {
		return minHands;
	}

	public void setMinHands(int minHands) {
		if(minHands > 0) {
			this.minHands = minHands;
		}
		else{
			throw new IllegalArgumentException("minHands must be greater than 0");
		}
	}

	public void setMinTime(double minTime) {
		this.minTime = minTime;
	}

	public double getMinArea() {
		return minArea;
	}

	public void setMinArea(double minArea) {
		this.minArea = minArea;
	}

	public void setMinAreaDefault(){
		minArea = MIN_AREA_DEFAULT;
	}

	public double getHandScale() {
		return handScale;
	}

	public void setHandScale(double handScale) {
		this.handScale = handScale;
	}

	public void addGameModeSwitch(GameModeSwitch gms){
		listeners.add(gms);
	}

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		pointsMovedThisTime.clear();
		//System.out.println("Area: " + webcamMotionEvent.getArea());

		if((System.currentTimeMillis() - lastMotion) >= minTime){
			/*
			if(webcamMotionEvent.getArea() >= minArea){
				onGameModeSwitch();
				lastMotion = System.currentTimeMillis();
			}
			*/

			ArrayList<Point> thePoints = webcamMotionEvent.getPoints();

			for(PicturePoint pp : points) {
				int numberOfPoints = 0;
				for (Point p : thePoints) {

					if (pp.distanceSq(p.getX(), p.getY()) <= pointRadius*pointRadius) {
						numberOfPoints++;
						if(numberOfPoints >= minPoints){
							pointsMovedThisTime.add(pp);
							break;
						}
					}
				}

				//System.out.println("Number of points moved: " + numberOfPoints);
			}
		}

		if(!pointsMovedThisTime.isEmpty()){

			int distortion = 0;
			int image = 0;
			for(PicturePoint pp : pointsMovedThisTime){
				distortion += ~pp.getId() & 1;
				image += pp.getId() & 1;
			}

			if(distortion > image && distortion >= minHands){
				onGameModeSwitch(1);
			}
			else if(image >= distortion && image >= minHands){
				onGameModeSwitch(0);
			}
		}
	}


	public void initialGameModeSwitch(){
		onGameModeSwitch(0);
	}

	private void onGameModeSwitch(int args){
		System.out.println("Switching gamemodes with args: " + args);
		lastMotion = System.currentTimeMillis();
		panel.setPainter(lastPainter);
		for(GameModeSwitch g : listeners){
			g.onGameModeSwitch(args);
		}
		lastPainter = panel.getPainter();
		panel.setPainter(this);

	}

	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		if (painter != null) {
			painter.paintPanel(panel, g2);
			if(lastPainter != null){
				lastPainter.paintPanel(panel,g2);
			}
		}
	}

	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
		if (painter != null) {
			painter.paintImage(panel, image, g2);
			if(lastPainter != null){
				lastPainter.paintImage(panel, image, g2);
			}
		}

		if (points == null) {
			return;
		}

		Iterator<PicturePoint> dfi = points.iterator();
		while (dfi.hasNext()) {
			PicturePoint p = dfi.next();
			BufferedImage img;
			if(p.getFacing() == Facing.LEFT && p.getId() % 2 == 0){
				img = handDistortionLeft;
			}
			else if(p.getFacing() == Facing.LEFT && p.getId() % 2 == 1){
				img = handImageLeft;
			}
			else if(p.getFacing() == Facing.RIGHT && p.getId() % 2 == 0){
				img = handDistortionRight;
			}
			else{
				img = handImageRight;
			}

			g2.drawImage(img, (int)(FunMirror.getScreenSizeX() - (p.x + 0.5* img.getWidth()*handScale)),
					(int)(p.y - 0.5* img.getHeight()*handScale),
					(int)(img.getWidth()*handScale),
					(int)(img.getHeight()*handScale),
					null);

			g2.drawOval(FunMirror.getScreenSizeX() - (p.x + (int)pointRadius), p.y - (int)pointRadius, (int)(2*pointRadius), (int)(2*pointRadius));
		}
	}
}
