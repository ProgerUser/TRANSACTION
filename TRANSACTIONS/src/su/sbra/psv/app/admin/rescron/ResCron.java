package su.sbra.psv.app.admin.rescron;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.naming.NamingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

class ResCron {
	
	/**
	 * �������� ���������� �����
	 */
	public static String EnvName = "ResCert";
	
	/**
	 * Cron ���������
	 */
	static String Cron;
	/**
	 * �������� ��������
	 */
	private static final String TRIGGER_NAME = "ResCert";
	/**
	 * �������� ������
	 */
	private static final String GROUP = "ResCertGroup";
	/**
	 * �������� �����
	 */
	private static final String JOB_NAME = "ResCertJob";
	/**
	 * �������
	 */
	private static Scheduler scheduler;
	

	/**
	 * ����� �����
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException {
		try {
			DOMConfigurator.configure(ResCron.class.getResource("/log4j.xml"));
			// ______________________PROPERTY__FILE___________________
			try (InputStream input = new FileInputStream(System.getenv("ResCert") + "/config.properties")) {
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);
				Cron = prop.getProperty("cron");
			} catch (Exception e) {
				MYLogger.error(ExceptionUtils.getStackTrace(e));
			}

			MYLogger.info("Start");
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			Trigger trigger = buildCronSchedulerTrigger();
			scheduleJob(trigger);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * ������ ������
	 * 
	 * @param trigger
	 * @throws Exception
	 */
	private static void scheduleJob(Trigger trigger) throws Exception {
		JobDetail someJobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(JOB_NAME, GROUP).build();
		scheduler.scheduleJob(someJobDetail, trigger);
	}

	/**
	 * �������� ��������� CRON
	 * 
	 * @return
	 */
	private static Trigger buildCronSchedulerTrigger() {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_NAME, GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(Cron)).build();
		return trigger;
	}

	/**
	 * �����������
	 */
	static Logger MYLogger = Logger.getLogger(ResCron.class);
}