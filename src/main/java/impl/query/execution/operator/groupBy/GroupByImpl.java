package impl.query.execution.operator.groupBy;

import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;

public class GroupByImpl  extends GroupByFunction{

	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {
		
		IRecordIterator recordIterator = inputDataSet.getRecordIterator();
		int fieldsCount = inputDataSet.getFieldsCount();
		int recordCount = inputDataSet.getRecordCount();
		Stream<Object[]> recordStream = getRecordStream(recordIterator, recordCount);
		
		
		int index = inputDataSet.getColumnIndex(args.getGroupingSequence().get(0));
		Map<Object, List<Object[]>> groupedStream = recordStream.collect(
			Collectors.groupingBy( record -> record[index] )
		);
		
		List<Object[]> firstGroup = groupedStream.entrySet().iterator().next().getValue();
		
//		firstGroup.stream().
//		.forEach( 
//				record ->{
//					StringBuilder sb = new StringBuilder();
//					for(int i=0; i<fieldsCount; i++) {
//						sb.append("		"+record[i]);
//					}
					
//					System.out.println(sb.toString());
//				}
//		);
			
//		SCRIVERE CLASSI RECORD_AGGREGATOR DA USARE IN METODI FOREACH
//		(BASATE SU RECORD_COMPARATOR)
		
		
		
		
		return inputDataSet;
	}
	
	
	private Stream<Object[]> getRecordStream(IRecordIterator recordIterator, int recordCount) {
		Stream<Object[]> result = StreamSupport.stream
		(
			Spliterators.spliterator(recordIterator,recordCount, 0)
			,false
		);
		return result;
	}

}
