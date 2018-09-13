package query.optimization;

public class ColumnStatistics {

	private String columnName;
	private int estimatedValueSize;
	private int estimatedDistinctValeCount;
	
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public int getEstimatedValueSize() {
		return estimatedValueSize;
	}
	public void setEstimatedValueSize(int estimatedValueSize) {
		this.estimatedValueSize = estimatedValueSize;
	}
	public int getEstimatedDistinctValeCount() {
		return estimatedDistinctValeCount;
	}
	public void setEstimatedDistinctValeCount(int estimatedDistinctValeCount) {
		this.estimatedDistinctValeCount = estimatedDistinctValeCount;
	}
	
}
