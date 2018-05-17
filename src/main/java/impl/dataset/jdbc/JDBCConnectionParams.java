package impl.dataset.jdbc;

import datasource.IConnectionParams;

public class JDBCConnectionParams implements IConnectionParams {
	
	private String username;
	private String password;
	private String portNumber;
	private String url;
	
	public JDBCConnectionParams() {
		username = password = portNumber = url = "";
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

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
	

}
