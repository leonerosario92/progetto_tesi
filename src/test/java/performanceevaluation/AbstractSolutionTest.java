package performanceevaluation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import objectexplorer.MemoryMeasurer;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;

public abstract class AbstractSolutionTest {
	
	
	public static final String LOG_FILE_PATH = "log4j.xml";
	public static Logger LOGGER;
	public static StringBuilder testReport;
	protected ContextFactory factory;
	protected IDataSource dataSource;
	
	public  abstract IDataSource getDataSourceImpl() throws JDBCDataSourceException;
	
	public  abstract ContextFactory getContextFactoryImpl();
	
	
	@BeforeClass
	public static void setupBeforeClass() {
		
		testReport = new StringBuilder();
		
		String logFilePath =
				LOG_FILE_PATH;
				 DOMConfigurator.configure(logFilePath);
				 LOGGER = Logger.getLogger("SolutionTest");
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() {
		LOGGER.info(testReport.toString());
	}
	
	
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
		
		final int EXPECTED_RESULTSET_SIZE = 100926;
		
		Query query = null;
		try {
			Context context = factory.getContext();

			IMetaData metaData = context.getMetadata();
			TableDescriptor salesTable = metaData.getTable("sales_fact_1998");
			FieldDescriptor storeSales = salesTable.getField("store_sales");
			FieldDescriptor unitSales = salesTable.getField("unit_sales");
			FieldDescriptor storeCost = salesTable.getField("store_cost");
			
			query =
			context.query()
				.selection(salesTable)
				.project(storeSales)
				.project(unitSales)
				.project(storeCost)
				.filter(storeCost, FilterStatementType.GREATER_THAN,new Integer(2))
				.getQuery();
			
			String sql = query.writeSql();
			
			IRecordIterator result = context.executeQuery(query);	
			MemoryMeasurer.measureBytes(result);
			
			int count = 0;
			while(result.hasNext()) {
				result.next();
				count ++;
			}
			
			assertEquals(EXPECTED_RESULTSET_SIZE , count);
			
		} catch (ContextFactoryException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			if(query != null) {
				testReport.append("Query execution took " + query.getExecutionTimeMillisecond() + " ms");
			}
			
		}
		
	}

}
