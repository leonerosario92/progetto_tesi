package utils.report;

public class ReportAggregator {
	
	private float executionTime;
	private float dataSetLoadingTime;
	private float memoryOccupation;
	
	public ReportAggregator() {
		executionTime = dataSetLoadingTime = memoryOccupation = 0.0f;
	}
	
	public void  sumToExecutionTime (ExecutionReport report) {
		executionTime += report.getExecutionTimeMs();
	}
	
	public void sumToDataSetLoadingTime(ExecutionReport report) {
		dataSetLoadingTime += report.getExecutionTimeMs();
	}
	
	public void sumToMemoryOccupation(ExecutionReport report) {
		memoryOccupation += memoryOccupation;
	}
	
	public void aggregate (ReportAggregator aggregator) {
		this.dataSetLoadingTime += aggregator.dataSetLoadingTime;
		this.executionTime  += aggregator.executionTime;
		this.memoryOccupation += aggregator.memoryOccupation;
	}
	
	public String getStringRepresentation() {
		return printExecutionTime() + " , " + printDataSetLoadingTime() + " , " + printMemoryOccupation();
	}

	private String printMemoryOccupation() {
		return "Memory occupation = " + memoryOccupation;
	}

	private String printDataSetLoadingTime() {
		return "Dataset loading time = " + dataSetLoadingTime;
	}

	private String printExecutionTime() {
		return "Execution time = " + executionTime;
	}
}
