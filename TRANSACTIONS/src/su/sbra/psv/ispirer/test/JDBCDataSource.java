package su.sbra.psv.ispirer.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class JDBCDataSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDataSource.class);

	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;

	static {
		Properties appProps = loadProperties();
		config.setJdbcUrl(appProps.getProperty("app.connection.url"));
		config.setUsername(appProps.getProperty("app.connection.username"));
		config.setPassword(appProps.getProperty("app.connection.password"));
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	public static Properties loadProperties() {
		Properties appProps = new Properties();
		try {
			appProps.load(new FileInputStream("./application.properties"));
		} catch (IOException e) {
			LOGGER.info("Unable to read application.properties file");
			LOGGER.info(String.valueOf(e));
		}
		return appProps;
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	private JDBCDataSource() { }
}