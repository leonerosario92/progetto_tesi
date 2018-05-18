import static org.junit.Assert.fail;

import datasource.IDataSource;
import impl.dataset.jdbc.JDBCConnectionParams;
import impl.dataset.jdbc.JDBCDataSourceException;
import impl.dataset.jdbc.mysql.MySqlJDBCDataSource;

public abstract class JDBCMySqlSolutionTest extends AbstractSolutionTest{
	
	public  IDataSource getDataSourceImpl() {
		
		JDBCConnectionParams connParams =  new JDBCConnectionParams();
		connParams.setUsername("root");
		connParams.setPassword("root");
		connParams.setUrl("localhost");
		connParams.setHost("localhost");
		connParams.setDbName("foodmart");
		
		try {
			return new MySqlJDBCDataSource(connParams);
		} catch (JDBCDataSourceException e) {
			fail();
		}
		return null;
	}

}
