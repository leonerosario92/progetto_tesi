package query.builder;

public class QueryConstants {
	public static final String GREATER_THAN = ">";
	public static final String LESS_THAN = "<";
	public static final String GREATER_THAN_OR_EQUALS = ">=";
	public static final String LESS_THAN_OR_EQUALS = "<=";
	public static final String EQUALS_TO= "=";
	public static final String DIFFERENT_FROM = "<>";
	
	public static final String WHITESPACE_CHAR = " ";
	public static final String DOT_CHAR = ".";
	public static final String COMMA_CHAR = ",";
	public static final String SEMICOLON_CHAR = ";";
	public static final String NEWLINE = System.getProperty("line.separator");
	
	public static final String SELECTION_CLAUSE = "FROM";
	public static final String PROJECTION_CLAUSE = "SELECT";
	public static final String FILTER_CLAUSE = "WHERE";
	public static final String JOIN = "JOIN";
	public static final String ON = "ON";

	public static final String AND = "AND";
	public static final String OR = "OR";
	
	public static final String OPEN_BRACKET = "( ";
	public static final String CLOSE_BRACKET = " )";
}
