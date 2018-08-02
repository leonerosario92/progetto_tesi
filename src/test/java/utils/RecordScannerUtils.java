package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import datasource.IRecordScanner;

public class RecordScannerUtils {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String TABULATION = "    ";
	public static final int COLUMN_WIDTH = 24;
	
	
	private RecordScannerUtils() {}
	
	
	public static void printToFile(IRecordScanner rs, String destinationFIlePath) {
		StringBuilder sb = new StringBuilder();
		
		printHeader(rs,sb);
		
		while(rs.next()) {
			for(int i=1; i<=rs.getFieldsCount(); i++) {
				String currentValue = rs.getValueByColumnIndex(i).toString();
				appendFixedWidth(sb,currentValue);
				sb.append(TABULATION);
			}
			sb.append(LINE_SEPARATOR);
		}
		
		try {
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(destinationFIlePath)));
			bwr.write(sb.toString());
			bwr.flush();
			bwr.close();
		} catch (IOException e) {
			throw new RuntimeException("An error occourred while writing RecordScanner content to file "+ destinationFIlePath);
		}
		
	}
	
	
	private static void printHeader(IRecordScanner rs, StringBuilder sb) {
		for(int i=1; i<=rs.getFieldsCount(); i++) {
			String currentHeader = rs.getColumnName(i).toUpperCase();
			appendFixedWidth(sb,currentHeader);
			sb.append(TABULATION);
		}
		sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
	}


	private static void appendFixedWidth(StringBuilder sb, String stringToAppend) {
		int length = stringToAppend.length();
		if( length > COLUMN_WIDTH) {
			stringToAppend = stringToAppend.substring(0, COLUMN_WIDTH-3)+"...";
			sb.append( stringToAppend);
		}else {
			sb.append( stringToAppend);
			for (int j=length; j<COLUMN_WIDTH; j++) {
				sb.append(" ");
			}
		}
	}

}
