package query.execution.operator;

import dataset.IDataSet;
import query.execution.IReportableExecutable;

public interface IOperatorGroup<T extends IDataSet> extends IReportableExecutable,Executable<T> { }

 