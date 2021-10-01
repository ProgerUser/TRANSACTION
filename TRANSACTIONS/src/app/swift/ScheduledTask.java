package app.swift;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
	SWC sw = null;
	String typeop = null;
	public void run() {
		if(typeop.equals("INOUT")) {
			sw.InitTable();
		}
		//System.out.println("!!!!!!!!!!!!!!!!!!");
	}

	void setSWC(SWC sc,String type) {
		this.sw = sc;
		this.typeop = type;
	}
}