package impl.jdbc.relational;

import datasource.IConnectionParams;

public class JDBCConnectionParams implements IConnectionParams {
	
	String url;
	String username;
	String password;
	
	public JDBCConnectionParams() { }
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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

}
