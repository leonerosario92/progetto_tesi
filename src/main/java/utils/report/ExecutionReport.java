package utils.report;

import utils.ExecutionPlanNavigator;

public class ExecutionReport {
	
	private static final int MS_CONVERSION_FACTOR = 1000*1000;
	private static final int MB_CONVERSION_FACTOR = 1024*1024;
	
	private static final String NOT_MEASURED_MSG = "NOT MEASURED";
	
	private long executionStartTime;
	private long executionEndTime;
	private long dataLoadingStartTIme;
	private long dataLoadingEndTIme;
	
	private float memoryOccupation;
	
	public ExecutionReport() {
		executionStartTime = executionEndTime = dataLoadingEndTIme = dataLoadingStartTIme = 0;
	}
	
	
	public void setExecutionStartTime() {
		this.executionStartTime = System.nanoTime();
	}
	public void setExecutionEndTime() {
		this.executionEndTime = System.nanoTime();
	}
	public void setDataLoadingStartTIme() {
		this.dataLoadingStartTIme = System.nanoTime();
	}
	public void setDataLoadingEndTIme() {
		this.dataLoadingEndTIme = System.nanoTime();
	}
	public void setMemoryOccupationByte(long memoryOccupation) {
		this.memoryOccupation = convertToMb(memoryOccupation);
	}
	
	public void setMemoryOccupationMByte(float memoryOccupation) {
		this.memoryOccupation = memoryOccupation;
	}
	
	
	
	
	private String printExecutionTime() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataSet Processing Time = ") ;
		if((executionStartTime != 0) || (executionEndTime != 0)) {
			sb.append(getExecutionTimeMs()).append(" ms");
		}else {
			sb.append(NOT_MEASURED_MSG);
		}
		return sb.toString();
	}
	
	
	public float getExecutionTimeMs() {
		return convertToMs(executionEndTime - executionStartTime);
	}
	
	
	private String printDataSetLoadingTime() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataSet Loading time = ");
		if((dataLoadingStartTIme != 0) || (dataLoadingEndTIme != 0)) {
			sb.append(getDataSetLoadingTimeMs()).append(" ms");
		}else {
			sb.append(NOT_MEASURED_MSG);
		}
		return sb.toString();
	}
	
	
	public float getDataSetLoadingTimeMs() {
		return convertToMs(dataLoadingEndTIme - dataLoadingStartTIme);
	}


	private String printMemoryOccupation() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataSet size = ");
		if(memoryOccupation != 0) {
			sb.append(getMemoryOccupationMB()).append(" MB");
		}else {
			sb.append(NOT_MEASURED_MSG);
		}
			
		return sb.toString();
	}
	
	
	public float getMemoryOccupationMB() {
		return memoryOccupation;
	}
	
	
	private float convertToMs(long nanoSecond) {
		return Float.valueOf(nanoSecond)/MS_CONVERSION_FACTOR;
	}
	
	
	private float convertToMb (long bytes) {
		return Float.valueOf(bytes)/ MB_CONVERSION_FACTOR;
	}


	public void addRepresentation(ExecutionPlanNavigator printer) {
		
		printer.appendLine("Execution Report : ");
		printer.addIndentation();
		
		printer.appendLine(printDataSetLoadingTime());
		printer.appendLine(printExecutionTime());
		printer.appendLine(printMemoryOccupation());
		
		printer.removeIndentation();
	}




	
}
