import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import context.Context;
import context.ContextFactory;
import context.FactoryException;
import dataset.IRecordIterator;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.Query;
import query.builder.predicate.FilterStatementType;

public abstract class AbstractSolutionTest {
	
	protected static ContextFactory factory;
	protected IDataSource dataSource;
	
	public  abstract IDataSource getDataSourceImpl();
	
	public  abstract ContextFactory getContextFactoryImpl();
	
	
	@BeforeClass
	public  void setUpBeforeClass(){
		this.dataSource = getDataSourceImpl();
		this.factory = getContextFactoryImpl();
	}
	
	
	@Test
	public static void TestScanBigDataSet(){
		
		try {
			Context context = factory.getContext();
			
			TableDescriptor factTable = context.getMetadata().getTable("sales_fact_1998");
			FieldDescriptor factTableJoinField = factTable.getField("product_id");
			FieldDescriptor factTableStoreSales = factTable.getField("store_sales");

			TableDescriptor dimensionTable = context.getMetadata().getTable("product");
			FieldDescriptor dimensionTableJoinField = dimensionTable.getField("product_id");
			FieldDescriptor productName = dimensionTable.getField("factTableJoinField");
			FieldDescriptor productSKU = factTable.getField("SKU");
			FieldDescriptor productNetWeight = factTable.getField("net_weight");
			
			Query query =
			context.query()
				.joinSelection(factTable)
					.joinWith(dimensionTable, factTableJoinField, dimensionTableJoinField)
				.project(productName)
				.project(productSKU)
				.project(productNetWeight)
				.project(factTableStoreSales)
				.filter(productNetWeight, FilterStatementType.GREATER_THAN,new Integer(18))
				.getQuery();
			
			IRecordIterator result = context.executeQuery(query);
			
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
		
	}

}
