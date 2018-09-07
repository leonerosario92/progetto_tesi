package utils;

public class ExecutableTreeNavigator {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String TABULATION = "    ";
	private int indentationLevel;
	private StringBuilder builder;
	private String tabSpacing;
	
	public  ExecutableTreeNavigator () {
		tabSpacing = "";
		builder = new StringBuilder();
		indentationLevel = 0;
	}
	
	public void addIndentation () {
		indentationLevel ++;
		updateTabSpacing(indentationLevel);
	}
	
	public void removeIndentation() {
		indentationLevel --;
		updateTabSpacing(indentationLevel);
	}
	
	public void appendLine(String line) {
		builder
			.append(LINE_SEPARATOR)
			.append(tabSpacing)
			.append(line);
	}
	
	public void append(String line) {
		builder.append(TABULATION).append(line);
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

	private void updateTabSpacing(int indentationLevel2) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < indentationLevel; i++) {
			sb.append(TABULATION);
		}
		tabSpacing = sb.toString();
	}
	
}