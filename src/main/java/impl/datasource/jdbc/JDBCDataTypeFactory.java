package impl.datasource.jdbc;


import java.sql.Types;
import java.util.Map;

import context.DataType;


public class JDBCDataTypeFactory {


	public static Class<?> toJavaClass(int nativeType) {
		
		Class<?> result = Object.class;
		
		 switch (nativeType) {
         case Types.CHAR:
         case Types.VARCHAR:
         case Types.LONGVARCHAR:
             result = String.class;
             break;

         case Types.NUMERIC:
         case Types.DECIMAL:
             result = java.math.BigDecimal.class;
             break;

         case Types.BIT:
             result = Boolean.class;
             break;

         case Types.TINYINT:
             result = Byte.class;
             break;

         case Types.SMALLINT:
             result = Short.class;
             break;

         case Types.INTEGER:
             result = Integer.class;
             break;

         case Types.BIGINT:
             result = Long.class;
             break;

         case Types.REAL:
         case Types.FLOAT:
             result = Float.class;
             break;

         case Types.DOUBLE:
             result = Double.class;
             break;

         case Types.BINARY:
         case Types.VARBINARY:
         case Types.LONGVARBINARY:
             result = Byte[].class;
             break;

         case Types.DATE:
             result = java.sql.Date.class;
             break;

         case Types.TIME:
             result = java.sql.Time.class;
             break;

         case Types.TIMESTAMP:
             result = java.sql.Timestamp.class;
             break;
     }
     return result;
	}
	
	
	public static DataType toDataType(int natveType) {
		
		DataType result = null;
		
		switch(natveType) {
		case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            result = DataType.STRING;
            break;
            
        case Types.INTEGER:
            result = DataType.INTEGER;
            break;
            
        case Types.BIGINT:
            result = DataType.LONG;
            break;

        case Types.REAL:
        case Types.FLOAT:
            result = DataType.FLOAT;
            break;

        case Types.DOUBLE:
            result = DataType.DOUBLE;
            break;
            
        case Types.NUMERIC:
        case Types.DECIMAL:
            result = DataType.BIG_DECIMAL;
            break;
		}
		return result;
	}

}
