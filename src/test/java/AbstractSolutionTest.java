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
			TableDescriptor productTable = metaData.getTable("product");
			FieldDescriptor productName = productTable.getField("product_name");
			FieldDescriptor productSKU = productTable.getField("SKU");
			FieldDescriptor productNetWeight = productTable.getField("net_weight");
			
			Query query =
			context.query()
				.selection(productTable)
				.project(productName)
				.project(productSKU)
				.project(productNetWeight)
				.filter(productNetWeight, FilterStatementType.GREATER_THAN,new Integer(18))
				.getQuery();
			
			IRecordIterator result = context.executeQuery(query);
			
			int count = 0;
			while(result.hasNext()) {
				count ++;
			}
			
			assertEquals(190, count);
			
		} catch (ContextFactoryException e) {
			fail(e.getMessage());
		}
		
	}

}
