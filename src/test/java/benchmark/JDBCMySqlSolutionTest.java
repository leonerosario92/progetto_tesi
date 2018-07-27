package benchmark;

import static org.junit.Assert.fail;

import datasource.IDataSource;
import impl.datasource.jdbc.JDBCConnectionParams;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.datasource.jdbc.mysql.MySqlJDBCDataSource;

public abstract class JDBCMySqlSolutionTest extends AbstractSolutionTest{
	
	public  IDataSource getDataSourceImpl() throws JDBCDataSourceException {
		
		JDBCConnectionParams connParams =  new JDBCConnectionParams();
		connParams.setUsername("root");
		connParams.setPassword("root");
		connParams.setHost("localhost");
		connParams.setPortNumber("3306");
		connParams.setDbName("enron");
		
		try {
			return new MySqlJDBCDataSource(connParams);
		} catch (JDBCDataSourceException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
