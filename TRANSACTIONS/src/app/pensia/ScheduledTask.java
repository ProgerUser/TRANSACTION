package app.pensia;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
	PensController sw = null;
	String typeop = null;
	public void run() {
		if(typeop.equals("INOUT")) {
			//sw.InitTable();
		}
		//System.out.println("!!!!!!!!!!!!!!!!!!");
	}

	void setSWC(PensController sc,String type) {
		this.sw = sc;
		this.typeop = type;
	}
}