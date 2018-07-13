package utils.report;

public interface IExecutionReport {
	
	public void setExecutionStartTime();
	
	public void setExecutionEndTime() ;

	void setMemoryOccupationByte(long memoryOccupation);

	float getExecutionTimeMs();

	float getMemoryOccupationMB();

}
