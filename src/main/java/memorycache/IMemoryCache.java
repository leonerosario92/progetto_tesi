package memorycache;

public interface IMemoryCache {
	
	public enum CachingStrategy{
		
	}
	
	public int getSize();
	public CachingStrategy getStrategy();

}
