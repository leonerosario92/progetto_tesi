package query.execution.operator;

public enum RelOperatorType {
	FILTER_ON_COLUMN,
	FILTER_ON_MULTIPLE_COLUMN,
	LOAD_COLUMN,
	LOAD_VERTICAL_PARTITION,
	ORDER_BY,
	GROUP_BY,
	MERGE_ON_BITSETS
}
