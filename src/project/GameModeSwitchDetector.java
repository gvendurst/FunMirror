package project;

import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

import java.util.ArrayList;

/**
 * Created by Gvendurst on 8.12.2015.
 */
public class GameModeSwitchDetector implements WebcamMotionListener{
	private double minTime;
	private double lastMotion;
	private double minArea;
	private final static double MIN_AREA_DEFAULT = 40;

	private ArrayList<GameModeSwitch> listeners;

	public GameModeSwitchDetector(){
		listeners = new ArrayList<>();
		minArea = MIN_AREA_DEFAULT;
	}

	public void addGameModeSwitch(GameModeSwitch gms){
		listeners.add(gms);
	}

	public double getMinTime() {
		return minTime;
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

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		System.out.println("Area: " + webcamMotionEvent.getArea());
		if((System.currentTimeMillis() - lastMotion) >= minTime){
			if(webcamMotionEvent.getArea() >= minArea){
				onGameModeSwitch();
				lastMotion = System.currentTimeMillis();
			}
		}

	}

	private void onGameModeSwitch(){
		for(GameModeSwitch g : listeners){
			g.onGameModeSwitch(0);
		}
	}
}
