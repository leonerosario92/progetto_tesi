package query.builder;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import context.Context;
import context.ContextFactory;
import datatype.DataType;
import impl.base.BaseQueryPlanner;
import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;
import query.execution.ExecutionPlan;
import utils.StringUtils;

import static query.builder.predicate.FilterStatementType.*;

import java.util.Set;

import static org.junit.Assert.assertTrue;
import  static org.mockito.Mockito.*;

public class QueryBuilderTest {

	private InitialBuilder queryBuilder;
	private static Context mockedContext;
	
	private static TableDescriptor salesTable;
	private static FieldDescriptor storeSales;
	private static FieldDescriptor unitSales;
	private static FieldDescriptor storeCost;
	
	@BeforeClass
	public static void setupBeforeClass() {
		mockedContext = getMockedContext();
		
		salesTable = new TableDescriptor("sales_fact_1998");
		storeSales = new FieldDescriptor(salesTable, "store_sales", DataType.INTEGER, false);
		unitSales = new FieldDescriptor(salesTable, "unit_sales", DataType.INTEGER, false);
		storeCost = new FieldDescriptor(salesTable, "store_cost", DataType.INTEGER, false);
	}
	

	@Before
	public void setupBefore() {
		queryBuilder = new InitialBuilder(mockedContext);
	}
	
	
	@Test
	public void testBuildSimpleQuery() {
		
		final String EXPECTED_QUERY = 
				"SELECT"
				+ System.getProperty("line.separator")
				+ "sales_fact_1998.store_sales, sales_fact_1998.unit_sales, sales_fact_1998.store_cost"
				+ System.getProperty("line.separator")
				+ "FROM sales_fact_1998"
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ "WHERE sales_fact_1998.store_cost > 2";
		
		Query query =
		queryBuilder
			.select(salesTable)
			.project(storeSales)
			.project(unitSales)
			.project(storeCost)
			.filter(storeCost, GREATER_THAN,new Integer(2))
			.getQuery();
		
		String sqlRepresentation = query.writeSql();
		assertTrue(StringUtils.equalsIgnoreNewlines(EXPECTED_QUERY, sqlRepresentation));
		
	}
	
	
	@Test
	public void testComposedFilterQuery() {
		
		final String EXPECTED_QUERY = 
			"SELECT"
			+ System.getProperty("line.separator")
			+ "sales_fact_1998.store_sales, sales_fact_1998.unit_sales, sales_fact_1998.store_cost"
			+ System.getProperty("line.separator")
			+ "FROM sales_fact_1998"
			+ System.getProperty("line.separator")
			+ System.getProperty("line.separator")
			+ "WHERE( sales_fact_1998.store_cost > 2 ) OR ( ( sales_fact_1998.unit_sales < 4 ) AND ( sales_fact_1998.unit_sales < 4 ) )";
		
		Query query =
			queryBuilder
			.select(salesTable)
			.project(storeSales)
			.project(unitSales)
			.project(storeCost)
			.composedfilter(storeCost, GREATER_THAN, new Integer(2))
				.or(
						new CFilterStatement(unitSales,LESS_THAN, new Integer(4))
						.and(unitSales,LESS_THAN, new Integer(4))
				)
		.getQuery();
		
		String sqlRepresentation = query.writeSql();
		assertTrue(StringUtils.equalsIgnoreNewlines(EXPECTED_QUERY, sqlRepresentation));
	}
	
	
	@Test
	public void testOrderByQuery() {
		
		final String EXPECTED_QUERY = 
			"SELECT"
			+ System.getProperty("line.separator")
			+ "sales_fact_1998.store_sales, sales_fact_1998.unit_sales, sales_fact_1998.store_cost"
			+ System.getProperty("line.separator")
			+ "FROM sales_fact_1998"
			+ System.getProperty("line.separator")
			+ System.getProperty("line.separator")
			+ "WHERE sales_fact_1998.store_cost > 2"
			+ System.getProperty("line.separator")
			+"ORDER BY sales_fact_1998.unit_sales";
		
		Query query = 
		queryBuilder
			.select(salesTable)
			.project(storeSales)
			.project(unitSales)
			.project(storeCost)
			.filter(storeCost, GREATER_THAN,new Integer(2))
			.orderBy(unitSales)
			.getQuery();
		
		String sqlRepresentation = query.writeSql();
		assertTrue(StringUtils.equalsIgnoreNewlines(EXPECTED_QUERY, sqlRepresentation));
	}
	
	
	
	/*
	 * TODO
	 * Write test cases that attempts to build "wrong" queries 
	 * and verify if the correct exception is thrown.
	*/
	
	
	
	private static Context getMockedContext() {
		Context result = mock(Context.class);
		return result;
	}
}
