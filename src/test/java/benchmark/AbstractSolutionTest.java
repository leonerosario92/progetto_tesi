package benchmark;
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
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;
import query.execution.QueryExecutionException;

public abstract class AbstractSolutionTest {
	
	@Rule public TestName name = new TestName();
	
	public static final String LOG_FILE_PATH = "log4j.xml";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TABULATION = "    ";
	private static Logger LOGGER;
	private static StringBuilder benchmarkReport;
	protected ContextFactory factory;
	protected IDataSource dataSource;
	
	public  abstract IDataSource getDataSourceImpl() throws JDBCDataSourceException;
	
	public  abstract ContextFactory getContextFactoryImpl();
	
	public static final int SCAN_SMALL_DATASET_EXPECTED_RESULT_SIZE = 96015;
	
	
	
	@BeforeClass
	public static void setupBeforeClass() {
		benchmarkReport = new StringBuilder();
		String logFilePath = LOG_FILE_PATH;
		DOMConfigurator.configure(logFilePath);
		LOGGER = Logger.getLogger("SolutionTest");
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() {
		benchmarkReport.append(LINE_SEPARATOR);
		LOGGER.info(benchmarkReport.toString());
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
	public void TestScanSmallDataSetPerformance(){
		
		String testReport = new String();
		Query query = null;
		try {
			Context context = factory.getContext();

			query = getScanSmallDataSetQuery(context);
			String sql = query.writeSql();
			IRecordIterator result = context.executeQuery(query,MeasurementType.EVALUATE_PERFORMANCE);	
			
			query.setResultIterationStartTime();
			int count = 0;
			while(result.hasNext()) {
				result.next();
				count ++;
			}
			query.setResultIterationEndTime();
			assertEquals("Wrong ResultSet Size", SCAN_SMALL_DATASET_EXPECTED_RESULT_SIZE, count);
			
			testReport = getPerformanceEvaluationReport(query);
			
		} 
		catch (ContextFactoryException | QueryExecutionException e) {
			testReport = getErrorReport(e);
			fail(e.getMessage());
		}finally {
			appendReport (testReport);
		}
	}
	
	
	@Test
	public void TestScanSmallDataSetMemoryOccupation(){
		
		String testReport = new String();
		Query query = null;
		try {
			Context context = factory.getContext();

			query = getScanSmallDataSetQuery(context);
			String sql = query.writeSql();
			IRecordIterator result = context.executeQuery
					(query, MeasurementType.EVALUATE_MEMORY_OCCUPATION);	
			
			int count = 0;
			while(result.hasNext()) {
				result.next();
				
				 Object res = result.getValueByColumnIndex(1);
				
				count ++;
			}
			
			assertEquals("Wrong ResultSet Size", SCAN_SMALL_DATASET_EXPECTED_RESULT_SIZE , count);
			testReport = getMemoryOccupationReport(query);
			
		} 
		catch (ContextFactoryException | QueryExecutionException e) {
			testReport = getErrorReport(e);
		} 
		finally {
			appendReport(testReport);
		}
	}
	
	
	private Query getScanSmallDataSetQuery(Context context) {
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
			.filter(unitSales, FilterStatementType.DIFFERENT_FROM, new Integer(5))
			.getQuery();
		
		return query;
	} 
	
	
	public String getTestReportHeader() {
		return ("TEST Method : " + name.getMethodName());
	}
	
	
	private String getPerformanceEvaluationReport(Query query) {
		return (
				"RESULT : "
//				+ "Query overall execution time :  " + query.getOverallExecutionTime() +" ms"
				+LINE_SEPARATOR +TABULATION
				+"DataSet loading time : " + query.getDataSetloadingTimeMillisecond() +" ms"
				+LINE_SEPARATOR+TABULATION
				+"Query execution time : " + query.getExecutionTimeMillisecond() +" ms"
				+LINE_SEPARATOR+TABULATION
				+"Result iteration time : " + query.getResultIterationTimeMillisecond() +" ms"
				);
	}
	
	
	private String getMemoryOccupationReport(Query query) {
		return("RESULT : Query execution caused a occupation of " + new Float(query.getResultSetByteSize())/(1024*1024) + " MByte in main memory");
	}
	 
	
	private String getErrorReport(Exception e ) {
		return ("RESULT : Test execution failed. Cause : " + e.getMessage());
	}

	
	private void appendReport(String testReport) {
		synchronized (benchmarkReport) {
			benchmarkReport
				.append(LINE_SEPARATOR)
				.append(getTestReportHeader())
				.append(LINE_SEPARATOR)
				.append(testReport)
				.append(LINE_SEPARATOR);
		}
	}	

}
