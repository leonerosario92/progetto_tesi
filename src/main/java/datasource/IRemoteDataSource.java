package datasource;

public interface IRemoteDataSource extends IDataSource {
	
	public void setConnectionParams (IConnectionParams connectionParams);
	
	public void connect ();
	
}
