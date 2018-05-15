package impl.jdbc.mysql.nativeimpl;

import datasource.IConnectionParams;

public class JDBCConnectionParams implements IConnectionParams {
	
	private String url;
	private String username;
	private String password;
	
	private String portNumber;
	
	public JDBCConnectionParams() { }
	
	public String getUrl() {
		
		return url;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber()

}
