package datasource;

public abstract class AbstractRemoteDataSource extends AbstractDataSource implements IRemoteDataSource{
	
	protected IConnectionParams connectionParams;
	
	protected AbstractRemoteDataSource (IConnectionParams cp) {
		this.connectionParams = cp;
	}
	
}
