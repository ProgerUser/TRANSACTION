package su.sbra.psv.app.pensia;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
	PensC pens = null;
	String taskname = "";
	Long id = 0l;

	public void run() {
		pens.RunPB(taskname, id);
		System.out.println("<PensLodrefresh>");
	}

	void setPens(PensC pens, String taskname, Long id) {
		this.pens = pens;
		this.taskname = taskname;
		this.id = id;
	}
}