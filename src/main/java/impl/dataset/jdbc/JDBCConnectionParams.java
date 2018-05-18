package impl.dataset.jdbc;

import datasource.IConnectionParams;

public class JDBCConnectionParams implements IConnectionParams {
	
	private String username;
	private String password;
	private String portNumber;
	private String host;
	private String dbName;
	
	public JDBCConnectionParams() {
		username = password = portNumber = host = dbName = "";
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

	public String getHost() {
		return host;
	}

	public void setUrl(String hostName) {
		this.host = hostName;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}
	

}
