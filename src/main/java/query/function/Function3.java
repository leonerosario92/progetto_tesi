package query.function;

@FunctionalInterface
public interface Function3 <A,B,C,D> {
	public  D apply (A a, B b, C c);
}
