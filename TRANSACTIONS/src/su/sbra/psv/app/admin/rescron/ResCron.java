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
	 * Название переменной среды
	 */
	public static String EnvName = "ResCert";
	
	/**
	 * Cron вырожение
	 */
	static String Cron;
	/**
	 * Название триггера
	 */
	private static final String TRIGGER_NAME = "ResCert";
	/**
	 * Название группы
	 */
	private static final String GROUP = "ResCertGroup";
	/**
	 * Название джоба
	 */
	private static final String JOB_NAME = "ResCertJob";
	/**
	 * Шедулер
	 */
	private static Scheduler scheduler;
	

	/**
	 * ТОчка Входа
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
	 * Запуск задачи
	 * 
	 * @param trigger
	 * @throws Exception
	 */
	private static void scheduleJob(Trigger trigger) throws Exception {
		JobDetail someJobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(JOB_NAME, GROUP).build();
		scheduler.scheduleJob(someJobDetail, trigger);
	}

	/**
	 * Получить выражение CRON
	 * 
	 * @return
	 */
	private static Trigger buildCronSchedulerTrigger() {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_NAME, GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(Cron)).build();
		return trigger;
	}

	/**
	 * Логирование
	 */
	static Logger MYLogger = Logger.getLogger(ResCron.class);
}