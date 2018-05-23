package impl.datasource.jdbc.mysql;

import dataset.IRecordIterator;
import datasource.DataSourceException;
import impl.datasource.jdbc.JDBCConnectionParams;
import impl.datasource.jdbc.JDBCDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import model.FieldDescriptor;

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
