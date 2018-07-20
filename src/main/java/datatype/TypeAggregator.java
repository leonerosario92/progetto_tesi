package datatype;

public interface TypeAggregator<T> {
	
	public T getMax();
	
	public T getMin();
	
	public Double getSum();
	
	public Double getAverage();
	
	public Long getCount();
	
	void addValue(Object value);
	
	public void combine(TypeAggregator<T> other);


}
