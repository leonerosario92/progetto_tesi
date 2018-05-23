package dataset;

import java.util.Set;
import java.util.stream.Stream;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface ILayoutManager {
	
//	public IDataSet loadTable(TableDescriptor table);
//	
//	public Stream <IRecord> loadTableStream(TableDescriptor table);	
//	
//	public Stream <IRecord> loadColumnStream(FieldDescriptor field);
	
//	public IDataSet loadColumn (FieldDescriptor field);

	public IDataSet mergeDatasets(Set<IDataSet> partialResults);

	public IDataSet newDataSet(IRecordIterator it);
	
}
