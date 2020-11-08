package swift;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
	SWC sw = null;

	public void run() {
		sw.InitTable();
	}

	void setSWC(SWC sc) {
		this.sw = sc;
	}
}