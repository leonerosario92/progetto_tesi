package impl.datasource.jdbc;

import datasource.DataSourceException;

public class JDBCDataSourceException extends DataSourceException {

	public JDBCDataSourceException(String message) {
		super(message);
	}

	public JDBCDataSourceException() {
		super();
	}

}
