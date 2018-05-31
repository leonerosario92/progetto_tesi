import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import context.Context;
import context.ContextFactory;
import context.ContextFactoryException;
import dataset.IRecordIterator;
import datasource.IDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;

public abstract class AbstractSolutionTest {
	
	protected ContextFactory factory;
	protected IDataSource dataSource;
	
	public  abstract IDataSource getDataSourceImpl() throws JDBCDataSourceException;
	
	public  abstract ContextFactory getContextFactoryImpl();
	
	
	@Before
	public void setupBefore() {
		try {
			this.dataSource = getDataSourceImpl();
		} catch (JDBCDataSourceException e) {
			e.printStackTrace();
			fail();
		}
		this.factory = getContextFactoryImpl();
	}
	
	
	@Test
	public void TestScanBigDataSet(){
		
		try {
			Context context = factory.getContext();

			IMetaData metaData = context.getMetadata();
			TableDescriptor salesTable = metaData.getTable("sales_fact_1998");
			FieldDescriptor storeSales = salesTable.getField("store_sales");
			FieldDescriptor unitSales = salesTable.getField("unit_sales");
			FieldDescriptor storeCost = salesTable.getField("store_cost");
			
			Query query =
			context.query()
				.selection(salesTable)
				.project(storeSales)
				.project(unitSales)
				.project(storeCost)
				.filter(storeCost, FilterStatementType.GREATER_THAN,new Integer(2))
				.getQuery();
			
			String sql = query.writeSql();
			
			
			query.setExecutionStartTime();
			IRecordIterator result = context.executeQuery(query);	
			query.setExecutionEndTime();
			
			System.out.println("Query execution took " + query.getExecutionTimeMillisecond() + " ms");
			
			int count = 0;
			while(result.hasNext()) {
				result.next();
				count ++;
			}
			
			
			assertEquals(100926, count);
			
		} catch (ContextFactoryException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}
