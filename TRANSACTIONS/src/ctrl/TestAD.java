package ctrl;

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

class TestAD {
	static String Cron;
	private static final String TRIGGER_NAME = "PosMail";
	private static final String GROUP = "PosGroup";
	private static final String JOB_NAME = "PosJob";
	private static Scheduler scheduler;

	public static void main(String[] args) throws NamingException {
		try {
			DOMConfigurator.configure(TestAD.class.getResource("/log4j.xml"));
			/*
			// _____________________LOG______________________________
			ConsoleAppender console = new ConsoleAppender(); // create appender
			// configure the appender
			String PATTERN = "%d [%p|%c|%C{1}] %m%n";
			console.setLayout(new PatternLayout(PATTERN));
			console.setThreshold(Level.DEBUG);
			console.activateOptions();
			// add appender to any Logger (here is root)
			org.apache.log4j.Logger.getRootLogger().addAppender(console);

			FileAppender fa = new FileAppender();
			fa.setName("CERTS");
			fa.setFile("CERTS.log");
			fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
			fa.setThreshold(Level.DEBUG);
			fa.setAppend(true);
			fa.activateOptions();

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
			*/
			// repeat with all other desired appenders
			// ______________________________________________________

			// ______________________PROPERTY__FILE___________________
			try (InputStream input = new FileInputStream(System.getenv("CERTS_SHEDULER") + "/config.properties")) {
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
	static Logger MYLogger = Logger.getLogger(TestAD.class);

}