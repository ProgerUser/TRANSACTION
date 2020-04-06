package sb_tr.model;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class CustomDate extends java.sql.Date {

	public CustomDate(long date) {
		super(date);
	}

	@Override
	public String toString() {
		return new SimpleDateFormat("dd/MM/yyyy").format(this);
	}
}