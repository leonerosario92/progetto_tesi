package impl.datasource.jdbc;


import java.sql.Types;
import java.util.Map;

import datatype.DataType;
import datatype.ITypeFactory;


public  class JDBCDataTypeFactory implements ITypeFactory {



	@Override
	public DataType toDataType(Object typeIdentifier) {
DataType result = null;
		int jdbcType = (int) typeIdentifier;
		switch(jdbcType) {
		case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            result = DataType.STRING;
            break;
            
        case Types.BIT:
            result = DataType.BOOLEAN;
            break;

        case Types.TINYINT:
            result = DataType.BYTE;
            break;

        case Types.SMALLINT:
            result = DataType.SHORT;
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
