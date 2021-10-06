package su.sbra.psv.app.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.sun.jna.Library;

public class FRREPRunner {
	public enum FRREPDllOptions {
		RUN, GRANTS, CLONE, DEPENDECES, DELETE, DESIGN, NEW;
	}

	private FRREPDllOptions dllOptions = FRREPDllOptions.RUN;
	private String dllOptionsStr = "";
	private String userName = "";
	private String passw = "";
	private String connect_string = "";
	private String serverName = "";
	private String sid = "";
	private String edit_enable = "";
	private String generate_type = "";
	private String dir = "";
	private String use_convertation = "";
	private String file_name = "";
	private String report_file = "";
	private String report_type_id = "";
	private String report_id = "";
	private String rep_name = "";
	private String report_sort = "";
	private String p1 = "";
	private String p2 = "";
	private String p3 = "";
	private String p4 = "";
	private String p5 = "";
	private String p6 = "";
	private String p7 = "";
	private String p8 = "";
	private String p9 = "";
	private String p10 = "";

//
//	FRREP
//	
	public interface FRREP extends Library {
		public String _SHOWREPORT(
				String DLLOPTIONS,
				String USERNAME,
				String PASSW,
				String CONNECT_STRING,
				String SERVERNAME,
				String SID,
				String Edit_Enable,
				String Generate_Type,
				String Dir,
				String Use_Convertation,
				String File_Name,
				String REPORT_FILE,
				String REPORT_TYPE_ID,
				String REPORT_ID,
				String REP_NAME,
				String REPORT_SORT,
				String P1,
				String P2,
				String P3,
				String P4,
				String P5,
				String P6,
				String P7,
				String P8,
				String P9,
				String P10);
	}
	
	public void setDllOptions(FRREPDllOptions opts) {
		this.dllOptions = opts;
		switch (opts) {
		case CLONE:
			this.dllOptionsStr = "CL";
			break;
		case DELETE:
			this.dllOptionsStr = "DL";
			break;
		case DEPENDECES:
			this.dllOptionsStr = "DP";
			break;
		case DESIGN:
			this.dllOptionsStr = "DE";
			break;
		case GRANTS:
			this.dllOptionsStr = "GR";
			break;
		case RUN:
			this.dllOptionsStr = "RUN";
			break;
		case NEW:
			this.dllOptionsStr = "DE";
			break;
		}

		if (opts == FRREPDllOptions.NEW) {
			this.report_file = "";
			this.file_name = "";
			this.rep_name = "";
			this.report_id = "";
		}
	}

	public FRREPDllOptions getDllOptions() {
		return this.dllOptions;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public String getPassw() {
		return this.passw;
	}

	public void setConnect_string(String connStr) {
		this.connect_string = connStr;
	}

	public String getConnect_string() {
		return this.connect_string;
	}

	public void setServerName(String servName) {
		this.serverName = servName;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return this.sid;
	}

	public void setEdit_enable(String edit_enable) {
		this.edit_enable = edit_enable;
	}

	public String getEdit_enable() {
		return this.edit_enable;
	}

	public void setGenerate_type(String gen_type) {
		this.generate_type = gen_type;
	}

	public String getGenerate_type() {
		return this.generate_type;
	}

	public void setFileName(String FileName) {
		this.file_name = FileName;
	}


	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return this.dir;
	}

	public void setUse_convertation(String uc) {
		this.use_convertation = uc;
	}

	public String getUse_convertation() {
		return this.use_convertation;
	}

	public void setReport_file(String rep_file) {
		if (this.dllOptions != FRREPDllOptions.NEW) {
			this.report_file = rep_file;
			//this.file_name = rep_file;
		}
	}

	public String getReport_file() {
		return this.report_file;
	}

	public void setReport_type_id(String t_id) {
		this.report_type_id = t_id;
	}

	public String getReport_type_id() {
		return this.report_type_id;
	}

	public void setReport_id(String r_id) {
		if (this.dllOptions != FRREPDllOptions.NEW) {
			this.report_id = r_id;
		}
	}

	public String getReport_id() {
		return this.report_id;
	}

	public void setRep_name(String rep_name) {
		if (this.dllOptions != FRREPDllOptions.RUN) {
			this.rep_name = rep_name;
		}
	}

	public String getRep_name() {
		return this.rep_name;
	}

	public void setReport_sort(String rep_sort) {
		this.report_sort = rep_sort;
	}

	public String getReport_sort() {
		return this.report_sort;
	}

	public void setP1(String p1) {
		this.p1 = (p1 == null) ? "" : p1;
	}

	public String getP1() {
		return this.p1;
	}

	public void setP2(String p2) {
		this.p2 = (p2 == null) ? "" : p2;
	}

	public String getP2() {
		return this.p2;
	}

	public void setP3(String p3) {
		this.p3 = (p3 == null) ? "" : p3;
	}

	public String getP3() {
		return this.p3;
	}

	public void setP4(String p4) {
		this.p4 = (p4 == null) ? "" : p4;
	}

	public String getP4() {
		return this.p4;
	}

	public void setP5(String p5) {
		this.p5 = (p5 == null) ? "" : p5;
	}

	public String getP5() {
		return this.p5;
	}

	public void setP6(String p6) {
		this.p6 = (p6 == null) ? "" : p6;
	}

	public String getP6() {
		return this.p6;
	}

	public void setP7(String p7) {
		this.p7 = (p7 == null) ? "" : p7;
	}

	public String getP7() {
		return this.p7;
	}

	public void setP8(String p8) {
		this.p8 = (p8 == null) ? "" : p8;
	}

	public String getP8() {
		return this.p8;
	}

	public void setP9(String p9) {
		this.p9 = (p9 == null) ? "" : p9;
	}

	public String getP9() {
		return this.p9;
	}

	public void setP10(String p10) {
		this.p10 = (p10 == null) ? "" : p10;
	}

	public String getP10() {
		return this.p10;
	}

	@SuppressWarnings("serial")
	public class AltPrintParamsNotSetException extends Exception {
		public AltPrintParamsNotSetException(String message) {
			super(message);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void checkMinimalParams() throws AltPrintParamsNotSetException {
		switch (this.dllOptions) {

		case RUN:
			if (this.userName.isEmpty() || this.passw.isEmpty() || this.connect_string.isEmpty()
					|| this.report_file.isEmpty()) {
				AltPrintParamsNotSetException ex = new AltPrintParamsNotSetException(
						"Для запуска FRREP в режиме RUN необходимо заполнить параметры userName, passw, connect_string и report_file");
				throw ex;
			}
			break;

		case DESIGN:
			if (this.userName.isEmpty() || this.passw.isEmpty() || this.connect_string.isEmpty()
					|| this.report_file.isEmpty() || this.report_type_id.isEmpty() || this.report_id.isEmpty()) {
				AltPrintParamsNotSetException ex = new AltPrintParamsNotSetException(
						"Для запуска FRREP в режиме DESIGN необходимо заполнить параметры userName, passw, connect_string, report_file, report_type_id, report_id");
				throw ex;
			}
			break;

		case CLONE:
			if (this.userName.isEmpty() || this.passw.isEmpty() || this.connect_string.isEmpty()
					|| this.report_type_id.isEmpty() || this.report_id.isEmpty()) {
				AltPrintParamsNotSetException ex = new AltPrintParamsNotSetException(
						"Для запуска FRREP в режиме DESIGN необходимо заполнить параметры userName, passw, connect_string, report_type_id, report_id");
				throw ex;
			}
			break;

		case DEPENDECES:
			if (this.userName.isEmpty() || this.passw.isEmpty() || this.connect_string.isEmpty()
					|| this.report_file.isEmpty()) {
				AltPrintParamsNotSetException ex = new AltPrintParamsNotSetException(
						"Для запуска FRREP в режиме DEPENDECES необходимо заполнить параметры userName, passw, connect_string и report_file");
				throw ex;
			}
			break;

		case GRANTS:
			if (this.userName.isEmpty() || this.passw.isEmpty() || this.connect_string.isEmpty()
					|| this.report_type_id.isEmpty() || this.report_id.isEmpty() || this.serverName.isEmpty()) {
				AltPrintParamsNotSetException ex = new AltPrintParamsNotSetException(
						"Для запуска FRREP в режиме DESIGN необходимо заполнить параметры userName, passw, connect_string, report_type_id, report_id, serverName");
				throw ex;
			}
			break;
		}
	}

	public void run() throws AltPrintParamsNotSetException, IOException, InterruptedException {
		
//		FRREP lib = (FRREP) Native.loadLibrary(System.getenv("MJ_PATH") + "bin/FRREP.dll", FRREP.class);
//		String ret = lib._SHOWREPORT(this.dllOptionsStr, // DLLOPTIONS
//				this.userName, // USERNAME
//				this.passw, // PASSW
//				this.connect_string, // CONNECT_STRING
//				this.serverName, // SERVERNAME
//				this.sid, // SID
//				this.edit_enable, // Edit_Enable
//				this.generate_type, // Generate_Type
//				this.dir, // Dir
//				this.use_convertation, // Use_Convertation
//				this.file_name, // File_Name
//				this.report_file, // REPORT_FILE
//				this.report_type_id, // REPORT_TYPE_ID
//				this.report_id, // REPORT_ID
//				this.rep_name, // REP_NAME
//				this.report_sort, // REPORT_SORT
//				this.p1, // P1
//				this.p2, // P2
//				this.p3, // P3
//				this.p4, // P4
//				this.p5, // P5
//				this.p6, // P6
//				this.p7, // P7
//				this.p8, // P8
//				this.p9, // P9
//				this.p10 // P10
//		);
//		System.out.println(ret);
		
		Process p;
		checkMinimalParams();

		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			p = Runtime.getRuntime().exec("i:/bin/FRREPRunner.exe");
			System.out.println("win");
		} else {
			p = Runtime.getRuntime().exec(new String[] { "wine", "FRREPRunner.exe" });
		}

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		writer.write("DLLOPTIONS\n");
		writer.write(this.dllOptionsStr + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("USERNAME\n");
		writer.write(this.userName + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("PASSW\n");
		writer.write(this.passw + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("CONNECT_STRING\n");
		writer.write(this.connect_string + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("SERVERNAME\n");
		writer.write(this.serverName + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("SID\n");
		writer.write(this.sid + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("Edit_Enable\n");
		writer.write(this.edit_enable + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("Generate_Type\n");
		writer.write(this.generate_type + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("Dir\n");
		writer.write(this.dir + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("Use_Convertation\n");
		writer.write(this.use_convertation + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("File_Name\n");
		writer.write(this.file_name + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("REPORT_FILE\n");
		writer.write(this.report_file + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("REPORT_TYPE_ID\n");
		writer.write(this.report_type_id + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("REPORT_ID\n");
		writer.write(this.report_id + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("REP_NAME\n");
		writer.write(this.rep_name + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("REPORT_SORT\n");
		writer.write(this.report_sort + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P1\n");
		writer.write(this.p1 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P2\n");
		writer.write(this.p2 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P3\n");
		writer.write(this.p3 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P4\n");
		writer.write(this.p4 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P5\n");
		writer.write(this.p5 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P6\n");
		writer.write(this.p6 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P7\n");
		writer.write(this.p7 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P8\n");
		writer.write(this.p8 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P9\n");
		writer.write(this.p9 + "\n");
		writer.write("ALTPRINTPARAMEND\n");

		writer.write("P10\n");
		writer.write(this.p10 + "\n");
		writer.write("ALTPRINTPARAMEND\n");
		writer.write("RUNFRREP\n");

		System.out.println("DLLOPTIONS=\"" + this.dllOptionsStr + "\"");
		System.out.println("USERNAME=\"" + this.userName + "\"");
		System.out.println("PASSW=\"" + this.passw + "\"");
		System.out.println("CONNECT_STRING=\"" + this.connect_string + "\"");
		System.out.println("SERVERNAME=\"" + this.serverName + "\"");
		System.out.println("SID=\"" + this.sid + "\"");
		System.out.println("Edit_Enable=\"" + this.edit_enable + "\"");
		System.out.println("Generate_Type=\"" + this.generate_type + "\"");
		System.out.println("Dir=\"" + this.dir + "\"");
		System.out.println("Use_Convertation=\"" + this.use_convertation + "\"");
		System.out.println("File_Name=\"" + this.file_name + "\"");
		System.out.println("REPORT_FILE=\"" + this.report_file + "\"");
		System.out.println("REPORT_TYPE_ID=\"" + this.report_type_id + "\"");
		System.out.println("REPORT_ID=\"" + this.report_id + "\"");
		System.out.println("REP_NAME=\"" + this.rep_name + "\"");
		System.out.println("REPORT_SORT=\"" + this.report_sort + "\"");
		System.out.println("P1=\"" + this.p1 + "\"");
		System.out.println("P2=\"" + this.p2 + "\"");
		System.out.println("P3=\"" + this.p3 + "\"");
		System.out.println("P4=\"" + this.p4 + "\"");
		System.out.println("P5=\"" + this.p5 + "\"");
		System.out.println("P6=\"" + this.p6 + "\"");
		System.out.println("P7=\"" + this.p7 + "\"");
		System.out.println("P8=\"" + this.p8 + "\"");
		System.out.println("P9=\"" + this.p9 + "\"");
		System.out.println("P10=\"" + this.p10 + "\"");

		writer.flush();
		int exitCode = p.waitFor();
		System.out.println(exitCode);

	}
}
