package cn.edu.swu;

import java.util.Timer;
import java.util.TimerTask;


public abstract class NetworkCollapse implements Runnable {
	
	protected abstract void collapse();
	
	private int interval = 5000;
	
	public NetworkCollapse(){
		
	}
	
	public NetworkCollapse(int interval){
		this.interval = interval;
	}
	
	@Override
	public void run() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(
			new TimerTask(){
				@Override public void run() {
					collapse();
				}
			}, 
			5000, interval
		);
		
	}

}
