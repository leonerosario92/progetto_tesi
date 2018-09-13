package query.optimization;

import java.util.List;

public class DataSetStatistics {
	
	private int estimatedRowCount;
	private int estimatedRowSize;
	private List<ColumnStatistics> columnStatistics;
	
	
	public int getEstimatedRowCount() {
		return estimatedRowCount;
	}
	public void setEstimatedRowCount(int estimatedRowCount) {
		this.estimatedRowCount = estimatedRowCount;
	}
	public int getEstimatedRowSize() {
		return estimatedRowSize;
	}
	public void setEstimatedRowSize(int estimatedRowSize) {
		this.estimatedRowSize = estimatedRowSize;
	}
	public List<ColumnStatistics> getColumnStatistics() {
		return columnStatistics;
	}
	public void setColumnStatistics(List<ColumnStatistics> columnStatistics) {
		this.columnStatistics = columnStatistics;
	}

}
