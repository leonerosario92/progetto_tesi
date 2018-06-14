package utils.report;

import utils.TreePrinter;

public class ExecutionReport {
	
	private static final int MS_CONVERSION_FACTOR = 1000*1000;
	private static final int MB_CONVERSION_FACTOR = 1024*1024;
	
	private long executionStartTime;
	private long executionEndTime;
	private long dataLoadingStartTIme;
	private long dataLoadingEndTIme;
	
	private long memoryOccupation;
	
	public ExecutionReport() {
		executionStartTime = executionEndTime = dataLoadingEndTIme = dataLoadingStartTIme = 0;
	}
	
	
	public void setExecutionStartTime(long executionStartTime) {
		this.executionStartTime = System.nanoTime();
	}
	public void setExecutionEndTime(long executionEndTime) {
		this.executionEndTime = System.nanoTime();
	}
	public void setDataLoadingStartTIme(long dataLoadingStartTIme) {
		this.dataLoadingStartTIme = System.nanoTime();
	}
	public void setDataLoadingEndTIme(long dataLoadingEndTIme) {
		this.dataLoadingEndTIme = System.nanoTime();
	}
	public void setMemoryOccupation(long memoryOccupation) {
		this.memoryOccupation = System.nanoTime();
	}
	
	
	private String printExecutionTime() {
		StringBuilder sb = new StringBuilder();
		sb.append("Execution Time = ") ;
		if((executionStartTime == 0) || (executionEndTime == 0)) {
			sb.append(getExecutionTimeMs()).append(" ms");
		}else {
			sb.append("NOT_MEASURED");
		}
		return sb.toString();
	}
	
	
	public float getExecutionTimeMs() {
		return convertToMs(memoryOccupation);
	}


	private String printMemoryOccupation() {
		StringBuilder sb = new StringBuilder();
		sb.append("Memory occupation = ");
		if(memoryOccupation == 0) {
			sb.append(getMemoryOccupationMB()).append(" MB");
		}else {
			sb.append("NOT_MEASURED");
		}
			
		return sb.toString();
	}
	
	
	public float getMemoryOccupationMB() {
		return convertToMb(memoryOccupation);
	}
	
	
	public String getStringRepresentation() {
		return printExecutionTime() + " , " + printMemoryOccupation();
	}
	
	
	private float convertToMs(long nanoSecond) {
		return nanoSecond/MS_CONVERSION_FACTOR;
	}
	
	
	private float convertToMb (long bytes) {
		return bytes/ MB_CONVERSION_FACTOR;
	}




	
}
