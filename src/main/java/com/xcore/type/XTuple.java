package com.xcore.type;

import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.impl.ArrayTuple;
import io.vertx.sqlclient.impl.ListTuple;

import java.util.ArrayList;
import java.util.List;

public class XTuple extends ListTuple {

  public XTuple() {
    super(new ArrayList<>());
  }

  public XTuple(List<Object> list) {
    super(list);
  }

  public static Tuple of(Object... elts) {
    ArrayTuple tuple = new ArrayTuple(1 + elts.length);
    for (Object elt: elts) {
      tuple.addValue(elt);
    }
    return tuple;
  }

  @Override
  public String deepToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    final int size = size();
    for (int i = 0; i < size; i++) {
      sb.append(getValue(i));
      if (i + 1 < size)
        sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }

  public String deepToSqlString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    final int size = size();
    for (int i = 0; i < size; i++) {
      Object obj = getValue(i);
      sb.append(obj instanceof String ? String.format("'%s'", obj) : obj);
      if (i + 1 < size)
        sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }
}
