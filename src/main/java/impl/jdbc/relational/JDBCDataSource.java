package impl.jdbc.relational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dataIterator.ColumnIterator;
import dataIterator.IDataIterator;
import dataIterator.TableIterator;
import datasource.AbstractRemoteDataSource;
import datasource.IConnectionParams;
import model.IMetaData;

public class JDBCDataSource extends AbstractRemoteDataSource{

	private Connection connection;
	private JDBCMetaData metaData;
	private JDBCQueryProvider queryProvider;
	
	protected JDBCDataSource(JDBCConnectionParams cp) {
		super(cp);
		this.connection = connect(cp);
		this.metaData = new JDBCMetaData(connection);
	}

	public JDBCMetaData getMetaData() {
		return metaData;
	}
	
	private Connection connect(JDBCConnectionParams params) {
		//Class.forName("com.mysql.jdbc.Driver");
	    try {
			connection = DriverManager
					//TODO make those infos parametric 
			        .getConnection(
			        		"jdbc:mysql:3306//localhost/feedback?"
			                + "user="+ params.getUsername()
			        		+"&password="+params.getPassword());
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public TableIterator getTable(String tableID) {
		// TODO Auto-generated method stub
		return null;
	}

	public TableIterator getTable(String tableID, int offset, int recordCount) {
		// TODO Auto-generated method stub
		return null;
	}

	public ColumnIterator getColumn(String fieldID) {
		// TODO Auto-generated method stub
		return null;
	}

	public ColumnIterator getColumn(String fieldID, int offset, int recordCount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
	public Connection getConnection () {
		return connection;
	}

	public IDataIterator delegateQuery() {
		return null;
		
	}

}
