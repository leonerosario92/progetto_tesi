package utils.report;

import utils.ExecutionPlanNavigator;

public class OperatorReport implements IExecutionReport {
	
	private static final int MS_CONVERSION_FACTOR = 1000*1000;
	private static final int MB_CONVERSION_FACTOR = 1024*1024;
	
	private long executionStartTime;
	private long executionEndTime;
	
	private float memoryOccupation;
	
	public OperatorReport() {
		executionEndTime = executionStartTime = 0;
		memoryOccupation = 0;
	}

	@Override
	public void setExecutionStartTime() {
		this.executionStartTime = System.nanoTime();
	}

	@Override
	public void setExecutionEndTime() {
		this.executionEndTime = System.nanoTime();
	}

	@Override
	public void setMemoryOccupationByte(long memoryOccupation) {
		this.memoryOccupation = convertToMb(memoryOccupation);	
	}

	@Override
	public float getExecutionTimeMs() {
		return convertToMs(executionEndTime - executionStartTime);
	}

	@Override
	public float getMemoryOccupationMB() {
		return memoryOccupation;
	}
	
	
	/*TODO Move those methods in utility classes*/
	private float convertToMs(long nanoSecond) {
		return Float.valueOf(nanoSecond)/MS_CONVERSION_FACTOR;
	}
	
	private float convertToMb (long bytes) {
		return Float.valueOf(bytes)/ MB_CONVERSION_FACTOR;
	}
	
	public String formatValue(float floatValue) {
		return String.format("%.2f", floatValue);
	}
	/*____________________________________________*/

	
	private String printExecutionTime() {
		StringBuilder sb = new StringBuilder();
		if((executionStartTime != 0) || (executionEndTime != 0)) {
			sb.append("Execution Time :  = ") ;
			sb.append( formatValue(getExecutionTimeMs()) ).append(" ms");
		}
		return sb.toString();
	}
	
	
	private String printMemoryOccupation() {
		StringBuilder sb = new StringBuilder();
		if(memoryOccupation != 0) {
			sb.append("Memory Occupation : = ")
			.append(formatValue(getMemoryOccupationMB()))
			.append(" MB");
		}
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		return printExecutionTime() + "  " + printMemoryOccupation() ;
	}
}
