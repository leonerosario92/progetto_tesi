package benchmark;

import context.ContextFactory;
import impl.base.StreamedQueryPlanner;

public class JDBCMySqlStreamedSolutionTest extends JDBCMySqlSolutionTest{

	@Override
	public ContextFactory getContextFactoryImpl() {
		ContextFactory factory = ContextFactory.getInstance(dataSource);
		factory.setQueryPlanner(StreamedQueryPlanner.class);
		return factory;
	}

}
