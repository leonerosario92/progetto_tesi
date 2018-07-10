package impl.base;

import java.math.BigDecimal;

import dataset.ColumnDescriptor;
import datatype.DataType;

public class BaseColumnFactory {
	//TODO: Find a way to do that in a factory method that is independent from specific Column implementation. 
		public static BaseColumn<?> createColumn(ColumnDescriptor descriptor, int length) {
			BaseColumn<?> newColumn = null;
			DataType type = descriptor.getColumnType();
			switch (type) {
			case INTEGER:
				newColumn = new BaseColumn<Integer>(descriptor,length);
				break;
			case DOUBLE:
				newColumn = new BaseColumn<Double>(descriptor,length);
				break;
			case FLOAT:
				newColumn = new BaseColumn<Float>(descriptor,length);
				break;
			case LONG:
				newColumn = new BaseColumn<Long>(descriptor,length);
				break;
			case STRING:
				newColumn = new BaseColumn<String>(descriptor,length);
				break;
			case BIG_DECIMAL:
				newColumn = new BaseColumn<BigDecimal>(descriptor,length);
				break;
			case BOOLEAN:
				newColumn = new BaseColumn<Boolean>(descriptor, length);
				break;
			case BYTE:
				newColumn = new BaseColumn<Byte>(descriptor, length);
				break;
			case SHORT:
				newColumn = new BaseColumn<Short>(descriptor, length);
				break;
			default:
				throw new IllegalArgumentException();
			}
			return newColumn;
		}
		
}
