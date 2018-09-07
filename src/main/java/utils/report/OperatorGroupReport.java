package utils.report;

import utils.ExecutableTreeNavigator;

public class OperatorGroupReport implements IExecutionReport {
	
	private static final int MS_CONVERSION_FACTOR = 1000*1000;
	private static final int MB_CONVERSION_FACTOR = 1024*1024;
	
	private long executionStartTime;
	private long executionEndTime;
	
	private long dataLoadingStartTIme;
	private long dataLoadingEndTIme;
	
	private long materializationStartTime;
	private long materializationEndTime;
	
	
	private float memoryOccupation;
	
	public OperatorGroupReport() {
		executionStartTime = executionEndTime = dataLoadingEndTIme = dataLoadingStartTIme = materializationEndTime = materializationStartTime = 0;
	}
	
	/*Execution Time Measurements */
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
	public void setMaterializationStartTime() {
		this.materializationStartTime = System.nanoTime();
	}
	public void setMaterializationEndTime() {
		this.materializationEndTime = System.nanoTime();
	}
	/*_____________________________*/
	
	
	/*Memory Occupation Measurements */
	public void setMemoryOccupationByte(long memoryOccupation) {
		this.memoryOccupation = convertToMb(memoryOccupation);
	}
	
	public void sumMemoryOccupationByte(long memoryOccupation) {
		this.memoryOccupation += convertToMb(memoryOccupation);
	}
	
	public void setMemoryOccupationMByte(float memoryOccupation) {
		this.memoryOccupation = memoryOccupation;
	}
	
	public void sumMemoryOccupationMByte(float memoryOccupation) {
		this.memoryOccupation += memoryOccupation;
	}
	/*_____________________________*/

	
	private void printExecutionTime(ExecutableTreeNavigator printer) {
		StringBuilder sb = new StringBuilder();
		if((executionStartTime != 0) || (executionEndTime != 0)) {
			sb.append("DataSet Processing Time = ") ;
			sb.append( formatValue(getExecutionTimeMs()) ).append(" ms");
			printer.appendLine(sb.toString());
		}
	}
	
	@Override
	public float getExecutionTimeMs() {
		return convertToMs(executionEndTime - executionStartTime);
	}
	
	
	private void printDataSetLoadingTime(ExecutableTreeNavigator printer) {
		StringBuilder sb = new StringBuilder();
		if((dataLoadingStartTIme != 0) || (dataLoadingEndTIme != 0)) {
			sb.append("DataSet Loading time = ");
			sb.append( formatValue(getDataSetLoadingTimeMs()) ).append(" ms");
			printer.appendLine(sb.toString());
		}
	}
	
	public float getDataSetLoadingTimeMs() {
		return convertToMs(dataLoadingEndTIme - dataLoadingStartTIme);
	}
	
	
	private void printMaterializationTime(ExecutableTreeNavigator printer) {
		StringBuilder sb = new StringBuilder();
		if((materializationStartTime != 0) || (materializationEndTime != 0)) {
			sb.append("Materialization time = ");
			sb.append( formatValue(getMaterializationTimeMs()) ).append(" ms");
			printer.appendLine(sb.toString());
		}
	}
	
	public float getMaterializationTimeMs() {
		return convertToMs(materializationEndTime - materializationStartTime);
	}


	private void printMemoryOccupation(ExecutableTreeNavigator printer) {
		StringBuilder sb = new StringBuilder();
		if(memoryOccupation != 0) {
			sb.append("DataSet size = ")
			.append(formatValue(getMemoryOccupationMB()))
			.append(" MB");
			printer.appendLine(sb.toString());
		}
	}
	
	@Override
	public float getMemoryOccupationMB() {
		return memoryOccupation;
	}
	
	
	private float convertToMs(long nanoSecond) {
		return Float.valueOf(nanoSecond)/MS_CONVERSION_FACTOR;
	}
	
	
	private float convertToMb (long bytes) {
		return Float.valueOf(bytes)/ MB_CONVERSION_FACTOR;
	}
	
	
	public String formatValue(float floatValue) {
		return String.format("%.2f", floatValue);
	}


	public void addRepresentation(ExecutableTreeNavigator printer) {
		
		printer.appendLine("Execution Report : ");
		printer.addIndentation();
		
		printDataSetLoadingTime(printer);
		printExecutionTime(printer);
		printMaterializationTime(printer);
		printMemoryOccupation(printer);

		printer.removeIndentation();
	}




	
}
