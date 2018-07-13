package utils.report;

public class ReportAggregator implements IExecutionReport{
	
	private static final int MS_CONVERSION_FACTOR = 1000*1000;
	private static final int MB_CONVERSION_FACTOR = 1024*1024;

	private float executionTime;
	private float memoryOccupation;
	
	public ReportAggregator() {
		executionTime = 0;
		memoryOccupation = 0;
	}
	
	
	public void setExecutionTmeMs(float executionTimeMs) {
		this.executionTime = executionTimeMs;
	}
	
	public void setMemoryOccupationByte(long memoryOccupationBytes) {
		this.memoryOccupation = convertToMb(memoryOccupationBytes);
	}
	
	public void sumMemoryOccupationByte(long memoryOccupation) {
		this.memoryOccupation += convertToMb(memoryOccupation);
	}
	
	
	public void sumMemoryOccupationMByte(float memoryOccupation) {
		this.memoryOccupation += memoryOccupation;
	}
	
	
	@Override
	public float getExecutionTimeMs() {
		return executionTime;
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
		if(executionTime != 0) {
			sb.append("Execution Time : ") ;
			sb.append( formatValue(getExecutionTimeMs()) )
			.append(" ms");
		}
		return sb.toString();
	}
	
	
	private String printMemoryOccupation() {
		StringBuilder sb = new StringBuilder();
		if(memoryOccupation != 0) {
			sb.append("Memory Occupation : ")
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
