package utils.report;

public class ReportAggregator {
	
	private float dataSetProcessingTime;
	private float dataSetLoadingTime;
	private float memoryOccupation;
	
	public ReportAggregator() {
		dataSetProcessingTime = dataSetLoadingTime = memoryOccupation = 0.0f;
	}
	
	public void  sumToExecutionTime (ExecutionReport report) {
		dataSetProcessingTime += report.getExecutionTimeMs();
	}
	
	public void sumToDataSetLoadingTime(ExecutionReport report) {
		dataSetLoadingTime += report.getExecutionTimeMs();
	}
	
	public void sumToMemoryOccupation(ExecutionReport report) {
		memoryOccupation += memoryOccupation;
	}
	
	public void aggregate (ReportAggregator aggregator) {
		this.dataSetLoadingTime += aggregator.dataSetLoadingTime;
		this.dataSetProcessingTime  += aggregator.dataSetProcessingTime;
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
		return "Execution time = " + dataSetProcessingTime;
	}
}
