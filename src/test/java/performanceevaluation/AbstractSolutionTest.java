package performanceevaluation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import context.Context;
import context.ContextFactory;
import context.ContextFactoryException;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.MeasurementType;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.query.execution.ExecutionException;
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;

public abstract class AbstractSolutionTest {
	
	@Rule public TestName name = new TestName();
	
	public static final String LOG_FILE_PATH = "log4j.xml";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static Logger LOGGER;
	public static StringBuilder testReport;
	protected ContextFactory factory;
	protected IDataSource dataSource;
	
	public  abstract IDataSource getDataSourceImpl() throws JDBCDataSourceException;
	
	public  abstract ContextFactory getContextFactoryImpl();
	
	public static final int SCAN_BIG_DATASET_EXPECTED_RESULT_SIZE = 100926;
	
	
	
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
	public void TestScanBigDataSetPerformance(){
		
		Query query = null;
		try {
			Context context = factory.getContext();

			query = getScanBigDataSetQuery(context);
			String sql = query.writeSql();
			IRecordIterator result = context.executeQuery(query,MeasurementType.EVALUATE_PERFORMANCE);	
			
			int count = 0;
			while(result.hasNext()) {
				result.next();
				count ++;
			}
			
			assertEquals(SCAN_BIG_DATASET_EXPECTED_RESULT_SIZE , count);
			
		} 
		catch (ContextFactoryException | ExecutionException e) {
			fail(e.getMessage());
		} 
		finally {
			if(query != null) {
				testReport.append(LINE_SEPARATOR);
				testReport.append("TEST : ").append(name.getMethodName());
				testReport.append(LINE_SEPARATOR);
				testReport.append("Query execution took " + query.getExecutionTimeMillisecond() + " ms");
			}
		}
		
	}
	
	
	@Test
	public void TestScanBigDataSetMemoryOccupation(){
		
		Query query = null;
		try {
			Context context = factory.getContext();

			query = getScanBigDataSetQuery(context);
			String sql = query.writeSql();
			IRecordIterator result = context.executeQuery
					(query, MeasurementType.EVALUATE_MEMORY_OCCUPATION);	
			
			int count = 0;
			while(result.hasNext()) {
				result.next();
				count ++;
			}
			
			assertEquals(SCAN_BIG_DATASET_EXPECTED_RESULT_SIZE , count);
			
		} 
		catch (ContextFactoryException | ExecutionException e) {
			fail(e.getMessage());
		} 
		finally {
			if(query != null) {
				testReport.append(LINE_SEPARATOR);
				testReport.append("TEST : ").append(name.getMethodName());
				testReport.append(LINE_SEPARATOR);
				testReport.append("Query execution caused a occupation of " + query.getResultSetByteSize() + " byte in main memory");
			}
		}
		
	}
	
	
	private Query getScanBigDataSetQuery(Context context) {
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
			.filter(storeCost, FilterStatementType.GREATER_THAN, new Integer(2))
			.getQuery();
		
		return query;
	} 

}
