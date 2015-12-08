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

	private ArrayList<GameModeSwitch> listeners;

	public GameModeSwitchDetector(){
		listeners = new ArrayList<>();
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

	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		System.out.println("Area: " + webcamMotionEvent.getArea());
		if((System.currentTimeMillis() - lastMotion) >= minTime){
			if(webcamMotionEvent.getArea() >= 75){
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
