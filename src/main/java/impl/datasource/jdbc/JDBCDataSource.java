package impl.datasource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataset.IColumnIterator;
import dataset.IRecordIterator;
import datasource.DataSourceException;
import datasource.IRemoteDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public abstract class JDBCDataSource implements IRemoteDataSource{

	private Connection connection;
	private JDBCMetaData metaData;
	private JDBCConnectionParams params;
	
	private PreparedStatement getTableStatement;
	private PreparedStatement getColumnStatement;
	
	public JDBCDataSource(JDBCConnectionParams params) throws JDBCDataSourceException {
		
		this.params = params;
		
		//TODO Manage exception properly
		try {
			this.connection = connect();
			this.getTableStatement = prepareGetTableStatement();
			this.metaData = new JDBCMetaData(connection);
		} catch (ClassNotFoundException e) {
			throw new JDBCDataSourceException("An error occurred while loading JDBC driver");
		} catch (SQLException e) {
			throw new JDBCDataSourceException ("An error occurred while connecting to remote DB"
					+ "caused by : " + e.getMessage());
		}
	}
	
	/* ---METHODS THAT SPECIFIC JDBC IMPLEMENTATION MUST EXTEND---*/
	protected abstract String getBaseUrl();
	
	protected abstract String getDriverClassName ();
	/*------------------------------------------------------------*/
	
	private Connection connect() throws SQLException, ClassNotFoundException {
		//Class.forName(getDriverClassName());
		connection = DriverManager
		        .getConnection(getConnectionString(params));
		return connection; 
	}
	
	
	private String getConnectionString(JDBCConnectionParams params) {
		StringBuilder connectionString = new StringBuilder();
    	connectionString.append(getBaseUrl());
    	connectionString.append(":");
    	connectionString.append("//");
    	connectionString.append(params.getHost());
    	connectionString.append(":");
    	connectionString.append(params.getPortNumber());
    	connectionString.append("/");
    	connectionString.append(params.getDbName());
    	connectionString.append("?");
    	connectionString.append("user=").append(params.getUsername());
    	connectionString.append("&");
    	connectionString.append("password=").append(params.getPassword());
    	return connectionString.toString();
	}
	
	
	private PreparedStatement getPreparedStatement(String query) throws SQLException {
		PreparedStatement statement =
		        connection.prepareStatement(query);
		return statement;
	}
	
	
	private PreparedStatement prepareGetTableStatement() throws SQLException {
		String query = "select * from tableName=?";
		return getPreparedStatement(query);
	}
	
	
//	private PreparedStatement prepareGetColumnStatement() throws SQLException {
//		String query = "select columnName=? from tableName=?";
//		return getPreparedStatement(query);
//	}
	
	
	private PreparedStatement getColumnStatement(int columnCount) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		for(int i=0; i < columnCount; i++) {
			sb.append("column").append(i).append("=?");
			if(i != columnCount-1) {
				sb.append(", ");
			}else {
				sb.append(" ");
			}
		}
		sb.append("from table=?");
		return getPreparedStatement(sb.toString());
	}
	
	
	/*---METHODS INHERITED FROM IRemoteDataSource ---*/
	public JDBCMetaData getMetaData() {
		return metaData;
	}
	
	
	public Connection getConnection () {
		return connection;
	}
	
	
	public void close() throws JDBCDataSourceException {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new JDBCDataSourceException("Error while attempting to close connection");
			}
		}
	}
	
	
	public IRecordIterator getTable(TableDescriptor table) throws JDBCDataSourceException  {
		PreparedStatement statement = getTableStatement;
		try {
			statement.setString(1,table.getName());
			ResultSet result = statement.executeQuery();
			return new JDBCRecordIterator (result);
		} catch (SQLException e) {
			throw new JDBCDataSourceException("An error occour while trying to retrieve table "+table.getName()+" from data source");
		}
	}
	
	
//	public IColumnIterator getColumn(FieldDescriptor field) throws JDBCDataSourceException  {
//		PreparedStatement statement = getColumnStatement;
//		try {
//			statement.setString(1,field.getTable().getName());
//			ResultSet result = statement.executeQuery();
//			return new JDBCColumnIterator (result);
//		} catch (SQLException e) {
//			throw new JDBCDataSourceException("An error occour while trying to retrieve column "
//											+field.getTable().getName()+"."+field.getName()
//											+ " from data source");
//		}
//	}
	

	@Override
	public IRecordIterator getTableProjection(TableDescriptor table, FieldDescriptor... args) throws DataSourceException {
		int length = args.length;
		try {
			PreparedStatement statement = getColumnStatement (length);
			for(int i=1; i<=length; i++) {
				FieldDescriptor field = args[i];
				String fieldName = field.getTable().getName()+"."+field.getName();
				statement.setString(i,fieldName);
			}
			statement.setString(length+1,table.getName());
			ResultSet result = statement.executeQuery();
			return new JDBCRecordIterator(result);
		} catch (SQLException e) {
			throw new DataSourceException("An error occour while trying to retrieve a set of column from remote data surce");
		}
	}
}
