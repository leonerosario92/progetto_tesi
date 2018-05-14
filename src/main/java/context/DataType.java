package context;

public enum DataType {
	INTEGER (Integer.class),
	
	FLOAT (Float.class),
	
	STRING (String.class);
	
	private Class<?> clazz;
	DataType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getJavaClass(){
		return this.clazz;
	}
}
