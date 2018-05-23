import context.ContextFactory;
import impl.dispatcher.jdbc.NativeQueryDispatcher;

public class JDBCMySqlNativeSolutionTest extends JDBCMySqlSolutionTest{

	@Override
	public ContextFactory getContextFactoryImpl() {
		
		ContextFactory factory = ContextFactory.getInstance(dataSource);
		factory.setQueryDispatcher(new NativeQueryDispatcher());
		return factory;
	}
	
}
