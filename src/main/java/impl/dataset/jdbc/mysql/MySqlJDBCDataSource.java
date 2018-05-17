package impl.dataset.jdbc.mysql;

import impl.dataset.jdbc.JDBCConnectionParams;
import impl.dataset.jdbc.JDBCDataSource;
import impl.dataset.jdbc.JDBCDataSourceException;

public class MySqlJDBCDataSource extends JDBCDataSource {
	
	public static final String BASE_URL = "jdbc:mysql";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public MySqlJDBCDataSource(JDBCConnectionParams params) throws JDBCDataSourceException {
		super(params);
	}


	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	@Override
	protected String getDriverClassName() {
		return DRIVER_CLASS;
	}

}
