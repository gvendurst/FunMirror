package project;

import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

import java.util.ArrayList;

/**
 * Created by Gvendurst on 8.12.2015.
 */
public class GameModeSwitchDetector implements WebcamMotionListener{
	private ArrayList<GameModeSwitch> listeners;

	public GameModeSwitchDetector(){
		listeners = new ArrayList<>();
	}

	public void addGameModeSwitch(GameModeSwitch gms){
		listeners.add(gms);
	}


	@Override
	public void motionDetected(WebcamMotionEvent webcamMotionEvent) {
		if(webcamMotionEvent.getArea() >= 75){
			onGameModeSwitch();
		}
	}

	private void onGameModeSwitch(){
		for(GameModeSwitch g : listeners){
			g.onGameModeSwitch(0);
		}
	}
}
