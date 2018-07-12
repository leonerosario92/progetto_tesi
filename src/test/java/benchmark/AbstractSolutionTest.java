package benchmark;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static query.builder.predicate.FilterStatementType.GREATER_THAN;
import static query.builder.predicate.FilterStatementType.LESS_THAN;

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
import datasource.IRecordScanner;
import dispatcher.MeasurementType;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.dispatcher.jdbc.NativeQueryDispatcher;
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.CFilterStatement;
import query.execution.QueryExecutionException;
import utils.comparator.QueryResultComparator;

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
	public void TestScanSmallDataSetExecutionTime(){
		executeTest(MeasurementType.EVALUATE_EXECUTION_TIME);
	}
	
	
	@Test
	public void TestScanSmallDataSetMemoryOccupation(){
		executeTest(MeasurementType.EVALUATE_MEMORY_OCCUPATION);
	}
		
		

	private void executeTest(MeasurementType measurementType) {
		String testReport = new String();
		Query query = null;
		try {
			Context context = factory.getContext();
			
			
			/* RIPRSTINARE QUERY ORIGINALE */
			query = getScanSmallDataSetQuery(context);
			/*_____________________________*/
			
			
			String sql = query.writeSql();
			IRecordScanner resultScanner = context.executeQuery
					(query, measurementType);	

			long resultIterationStartTime = System.nanoTime();
			boolean correctness = testQueryResult(query, resultScanner);		
			long resultIterationEndTime = System.nanoTime();
			assertTrue("Error : Query execution returned a result that differs from the expected one.", correctness);
			
			long iterationNanos = (resultIterationEndTime - resultIterationStartTime);
			query.setResultIterationTime(Float.valueOf(iterationNanos)/ (1000*1000));
			
			testReport = writeTestReport(query);
		} 
		catch (ContextFactoryException | QueryExecutionException e) {
			testReport = getErrorReport(e);
		} 
		finally {
			appendReport(testReport);
		}
	}
	
	
	private Query getTestQuery(Context context) {
		IMetaData metaData = context.getMetadata();
		TableDescriptor salesTable = metaData.getTable("sales_fact_1998");
		FieldDescriptor storeSales = salesTable.getField("store_sales");
		FieldDescriptor unitSales = salesTable.getField("unit_sales");
		FieldDescriptor storeCost = salesTable.getField("store_cost");
		
		Query query = context.query()
		.select(salesTable)
		.project(storeSales)
		.project(unitSales)
		.project(storeCost)
		.orderBy(storeCost
				,unitSales
				)
		.getQuery();
		
		String sqlRepresentation = query.writeSql();
		
		return query;
	}
	

	private Query getScanSmallDataSetQuery(Context context) {
		IMetaData metaData = context.getMetadata();
		TableDescriptor salesTable = metaData.getTable("sales_fact_1998");
		FieldDescriptor storeSales = salesTable.getField("store_sales");
		FieldDescriptor unitSales = salesTable.getField("unit_sales");
		FieldDescriptor storeCost = salesTable.getField("store_cost");
		
		Query query =
		context.query()
			.select(salesTable)
			.project(storeSales)
			.project(unitSales)
			.project(storeCost)
			.filter(storeCost, FilterStatementType.GREATER_THAN, new Integer(2))
			.filter(unitSales, FilterStatementType.DIFFERENT_FROM, new Integer(5))
			.getQuery();
		
		return query;
	} 
	
	
	private boolean testQueryResult(Query query, IRecordScanner result) {
		boolean comparisonResult = false;
		try {
			ContextFactory factory = ContextFactory.getInstance(dataSource);
			factory.setQueryDispatcher(NativeQueryDispatcher.class);
			Context context = factory.getContext();
			IRecordScanner nativeResult;
			nativeResult = context.executeQuery(query);
			comparisonResult = QueryResultComparator.compareValues(result, nativeResult);
			return comparisonResult;
		} catch (QueryExecutionException | ContextFactoryException e) {
			//TODO Manage exception properly
			e.printStackTrace();
			return comparisonResult;
		}
	}
	
	
/*====REPORT MANAGEMENT==== */
	public String getTestReportHeader() {
		return ("TEST NAME: " + name.getMethodName());
	}
	
	
	private String writeTestReport(Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("TEST RESULT : ");
		if(query.getExecutionTime() != 0) {
			sb.append(LINE_SEPARATOR).append("Overall execution time : " +query.getExecutionTime() + " ms");
		}
		if(query.getResultIterationTime() != 0) {
			sb.append(LINE_SEPARATOR).append("ResultSet iteration time : " + query.getResultIterationTime() + " ms");
		}
		if(query.getMemoryOccupation() != 0) {
			sb.append(LINE_SEPARATOR).append("Oveall main memory occupation : " + query.getMemoryOccupation() + " MByte");
		}
		if(! query.getExecutionReport().isEmpty()) {
			sb.append(LINE_SEPARATOR).append("EXECUTION DETAILS : ").append(LINE_SEPARATOR).append(query.getExecutionReport());
		}
		return sb.toString();
	}
	 
	
	private String getErrorReport(Exception e ) {
		return ("RESULT : Test execution failed. Cause : " + e.getMessage());
	}

	
	private void appendReport(String testReport) {
		synchronized (benchmarkReport) {
			benchmarkReport
				.append(LINE_SEPARATOR)
				.append(LINE_SEPARATOR)
				.append(LINE_SEPARATOR)
				.append(getTestReportHeader())
				.append(LINE_SEPARATOR)
				.append(testReport)
				.append(LINE_SEPARATOR);
		}
	}	

}
