package query.execution.operator;


public abstract class Operator<F extends IOperatorFunction, A extends IOperatorArgs> {
	
	protected A args;
	protected F function;
	protected String operatorName;
	

	public A getArgs() {
		return args;
	}
	
	public void setArgs (A args) {
		this.args = args;
	}
}
