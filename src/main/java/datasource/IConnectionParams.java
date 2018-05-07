package datasource;

public interface IConnectionParams {
	
	public void setUsername(String username);
	
	public void setPassword(String password);
	
	public void setUrl (String url);
	
	public String getUsername();
	
	public String getPassword();
	
	public String getUrl();
	
}
