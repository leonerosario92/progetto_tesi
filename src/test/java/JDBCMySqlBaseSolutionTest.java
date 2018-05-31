import context.ContextFactory;

public class JDBCMySqlBaseSolutionTest extends JDBCMySqlSolutionTest {

	@Override
	public ContextFactory getContextFactoryImpl() {
		
		ContextFactory factory = ContextFactory.getInstance(dataSource);
		return factory;
		
	}

}
