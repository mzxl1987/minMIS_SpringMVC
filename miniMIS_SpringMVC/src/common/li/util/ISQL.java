package common.li.util;

import java.util.Date;

/**
 * 自定义SQL语句
 * 
 * @author Administrator
 *
 */
public class ISQL {

	private static int index = 0;
	private static Object obj_lock = new Object();

	private String select;
	private String delete;
	private String selectColumns;
	private String table;
	private String where = " 1=1 ";
	private String order;
	private String tableAlis; // 表昵称
	private int whereCount = 0;

	public ISQL where(){
		where = " where" + where;
		return this;
	}
	
	public ISQL() {
		tableAlis = "a" + createID();
	}

	public static ISQL newInstance() {
		return new ISQL();
	}

	private static String createID() {
		synchronized (obj_lock) {
			// return "" + Long.toHexString(new Date().getTime()) +
			// Long.toHexString(index++);
			return Long.toHexString(index++);
		}
	}

	private String formatDateToyyyyMMddHHmmss(Date obj) {
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(obj);
	}

	/**
	 * select
	 * 
	 * @return
	 */
	public ISQL select() {
		select = " select ";
		return this;
	}

	public ISQL delete() {
		delete = " delete ";
		return this;
	}

	/**
	 * 设置显示的列 默认是全部:*,或者显示
	 * 
	 * @param columns
	 * @return
	 */
	public ISQL columns(String[] columns) {
		if (noEmpty(columns)) {
			for (String column : columns) {
				column = addColumnTo(column);
				if (noEmpty(selectColumns)) {
					selectColumns += "," + column;
				} else {
					selectColumns = " " + column;
				}
			}
		} else {
			selectColumns = addColumnTo("*");
		}

		return this;
	}

	public ISQL from(String table) {
		this.table = " from" + addAlis(table);

		return this;
	}

	private boolean noEmpty(Object obj) {
		return null != obj && obj != null && !"".equals(obj);
	}

	private String addAlis(String tableName) {
		return " " + tableName + " " + tableAlis + "";
	}

	private String addColumnTo(String columnName) {
		return " " + tableAlis + "." + columnName + "";
	}

	public ISQL where(String columnName, Object value) {
		where("and", columnName, "=", value);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ISQL where(String orAnd, String columnName, String operator,
			Object value) {

		if (!noEmpty(columnName)) {
			return this;
		}

		columnName = addColumnTo(columnName);

		if (noEmpty(value)) {

			orAnd = (whereCount == 0 || !noEmpty(orAnd)) ? " and " : " "
					+ orAnd + " ";
			columnName = " " + columnName + " ";
			operator = " " + operator + " ";

			switch (operator.trim()) {
			case "like":
				if (value.getClass().equals(String.class)) {
					where += orAnd + " " + " NLS_UPPER(" + columnName + ") "
							+ operator + " '%" + ((String) value).toUpperCase()
							+ "%' ";
					whereCount++;
				}
				break;

			case ">":
			case ">=":
			case "<":
			case "<=":
			case "=":

				Class vClass = value.getClass();

				if (vClass.equals(int.class) || vClass.equals(float.class)
						|| vClass.equals(double.class)
						|| vClass.equals(Integer.class)
						|| vClass.equals(Float.class)
						|| vClass.equals(Double.class)) {
					where += orAnd + columnName + operator + value + " ";
					whereCount++;
				} else if (vClass.equals(String.class)) {
					where += orAnd + columnName + operator + " '" + value
							+ "' ";
					whereCount++;
				} else if (vClass.equals(Date.class)) {
					where += orAnd + columnName + operator + " to_date('"
							+ formatDateToyyyyMMddHHmmss((Date) value)
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
					whereCount++;
				}
				break;
			}

		}
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ISQL in(String orAnd, String columnName, Object[] values) {

		if (!noEmpty(columnName)) {
			return this;
		}

		columnName = addColumnTo(columnName);
		String operator = " in ";
		orAnd = (whereCount == 0 || !noEmpty(orAnd)) ? " and " : " " + orAnd
				+ " ";

		if (noEmpty(values)) {
			String sql_in = "";
			for (int i = 0, count = values.length; i < count; i++) {

				Object value = values[i];
				Class vClass = value.getClass();

				if (vClass.equals(String.class)) {
					if (noEmpty(sql_in)) {
						sql_in += " , '" + value + "' ";
					} else {
						sql_in = " ( '" + value + "'";
					}
				}

				if (vClass.equals(int.class) || vClass.equals(float.class)
						|| vClass.equals(double.class)
						|| vClass.equals(Integer.class)
						|| vClass.equals(Float.class)
						|| vClass.equals(Double.class)) {
					if (noEmpty(sql_in)) {
						sql_in += " , " + value + " ";
					} else {
						sql_in = " ( " + value + " ";
					}
				}

				if (i == count - 1) {
					sql_in += " ) ";
				}
			}

			where += orAnd + columnName + operator + " " + sql_in + " ";
		}

		return this;
	}

	public ISQL in(String orAnd, String columnName, String sqlIn) {

		if (!noEmpty(columnName)) {
			return this;
		}

		columnName = addColumnTo(columnName);
		String operator = " in ";
		orAnd = (whereCount == 0 || !noEmpty(orAnd)) ? " and " : " " + orAnd
				+ " ";

		where += orAnd + columnName + operator + " (" + sqlIn + ") ";

		return this;
	}

	@SuppressWarnings("rawtypes")
	public ISQL between(String orAnd, String columnName, Object min, Object max) {

		if (!noEmpty(columnName)) {
			return this;
		}
		columnName = addColumnTo(columnName);
		orAnd = (whereCount == 0 || !noEmpty(orAnd)) ? " and " : " " + orAnd
				+ " ";

		if (noEmpty(min) && noEmpty(max)
				&& min.getClass().equals(max.getClass())) {

			Class minClass = min.getClass();
			if (minClass.equals(String.class)) {
				where += orAnd + columnName + " BETWEEN  '" + min + "' and '"
						+ max + "' ";
			} else if (minClass.equals(int.class)
					|| minClass.equals(float.class)
					|| minClass.equals(double.class)
					|| minClass.equals(Integer.class)
					|| minClass.equals(Float.class)
					|| minClass.equals(Double.class)) {
				where += orAnd + columnName + " BETWEEN  " + min + " and "
						+ max + " ";
			} else if (minClass.equals(Date.class)) {

				where += orAnd + columnName + " BETWEEN  " + " to_date('"
						+ formatDateToyyyyMMddHHmmss((Date) min)
						+ "', 'yyyy-mm-dd hh24:mi:ss') " + " and "
						+ " to_date('" + formatDateToyyyyMMddHHmmss((Date) max)
						+ "', 'yyyy-mm-dd hh24:mi:ss') " + " ";
			}

		}

		return this;
	}

	@SuppressWarnings("rawtypes")
	public ISQL notBetween(String orAnd, String columnName, Object min,
			Object max) {

		if (!noEmpty(columnName)) {
			return this;
		}
		columnName = addColumnTo(columnName);
		orAnd = (whereCount == 0 || !noEmpty(orAnd)) ? " and " : " " + orAnd
				+ " ";

		if (noEmpty(min) && noEmpty(max)
				&& min.getClass().equals(max.getClass())) {

			Class minClass = min.getClass();
			if (minClass.equals(String.class)) {
				where += orAnd + columnName + " not BETWEEN  '" + min
						+ "' and '" + max + "' ";
			} else if (minClass.equals(int.class)
					|| minClass.equals(float.class)
					|| minClass.equals(double.class)
					|| minClass.equals(Integer.class)
					|| minClass.equals(Float.class)
					|| minClass.equals(Double.class)) {
				where += orAnd + columnName + " not BETWEEN  " + min + " and "
						+ max + " ";
			} else if (minClass.equals(Date.class)) {

				where += orAnd + columnName + " not BETWEEN  " + " to_date('"
						+ formatDateToyyyyMMddHHmmss((Date) min)
						+ "', 'yyyy-mm-dd hh24:mi:ss') " + " and "
						+ " to_date('" + formatDateToyyyyMMddHHmmss((Date) max)
						+ "', 'yyyy-mm-dd hh24:mi:ss') " + " ";
			}
		}

		return this;
	}

	public ISQL orderAsc(String columnName) {

		if (!noEmpty(columnName)) {
			return this;
		}

		columnName = addColumnTo(columnName);
		String operator = " asc ";

		if (noEmpty(order)) {
			order = " , " + columnName + operator;
		} else {
			order = " order by " + columnName + operator;
		}

		return this;
	}

	public ISQL orderDesc(String columnName) {

		if (!noEmpty(columnName)) {
			return this;
		}

		columnName = addColumnTo(columnName);
		String operator = " desc ";

		if (noEmpty(order)) {
			order = " , " + columnName + operator;
		} else {
			order = " order by " + columnName + operator;
		}

		return this;
	}

	public String toSelectSql() {

		select();

		if (!noEmpty(select)) {
			select = " select ";
		}

		if (!noEmpty(selectColumns)) {
			selectColumns = " * ";
		}

		if (!noEmpty(table)) {
			table = " ";
		}

		if (!noEmpty(where)) {
			where = " ";
		}

		if (!noEmpty(order)) {
			order = " ";
		}

		return select + selectColumns + table + where + order;
	}

	public String toDeleteSql() {

		delete();

		if (!noEmpty(delete)) {
			delete = " delete ";
		}

		if (!noEmpty(table)) {
			table = " ";
		}

		if (!noEmpty(where)) {
			where = " ";
		}

		return delete + table + where;
	}
	
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getTableAlis() {
		return tableAlis;
	}

	public void setTableAlis(String tableAlis) {
		this.tableAlis = tableAlis;
	}

	public static void main(String[] args) {

		String sql = ISQL.newInstance().from("student")
				.columns(new String[] { "name", "age", "sex" })
				.where("age", 16).where("or", "name", " like ", "小明")
				.where("and", "name", ">", new Date())
				.in(null, "name", "select v.id from vt v").toSelectSql();
		String sql_delete = ISQL.newInstance().toDeleteSql();

		System.out.println(sql);
		System.out.println(sql_delete);

	}

}
