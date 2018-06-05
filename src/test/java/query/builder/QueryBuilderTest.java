package query.builder;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import context.Context;

import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.predicate.FilterStatementType;
import utils.StringUtils;

import static org.junit.Assert.assertTrue;
import  static org.mockito.Mockito.*;

public class QueryBuilderTest {

	private InitialBuilder queryBuilder;
	private static Context mockedContext;
	
	private static final String EXPECTED_QUERY = 
			"SELECT"
			+ System.getProperty("line.separator")
			+ "sales_fact_1998.store_sales, sales_fact_1998.unit_sales, sales_fact_1998.store_cost"
			+ System.getProperty("line.separator")
			+ "FROM sales_fact_1998"
			+ System.getProperty("line.separator")
			+ System.getProperty("line.separator")
			+ "WHERE sales_fact_1998.store_cost > 2";
	 
	
	@BeforeClass
	public static void setupBeforeClass() {
		mockedContext = getMockedContext();
	}
	


	@Before
	public void setupBefore() {
		queryBuilder = new InitialBuilder(mockedContext);
	}
	
	
	@Test
	public void testBuildProjectionFilterQuery() {
		
		TableDescriptor salesTable = new TableDescriptor("sales_fact_1998");
		
		FieldDescriptor storeSales = new FieldDescriptor(salesTable, "store_sales", null, false);
		FieldDescriptor unitSales = new FieldDescriptor(salesTable, "unit_sales", null, false);
		FieldDescriptor storeCost = new FieldDescriptor(salesTable, "store_cost", null, false);
		
		Query query =
		queryBuilder
			.selection(salesTable)
			.project(storeSales)
			.project(unitSales)
			.project(storeCost)
			.filter(storeCost, FilterStatementType.GREATER_THAN,new Integer(2))
			.getQuery();
		
		assertTrue(StringUtils.equalsIgnoreNewlines(EXPECTED_QUERY, query.writeSql()));
		
	}
	
	
	private static Context getMockedContext() {
		Context result = mock(Context.class);
		return result;
	}
}
