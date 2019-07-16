package common.li.util;


/**
 * 自定义SQL语句
 * 
 * @author Administrator
 *
 */
public class ISQLmini {

	private String sql;

	public ISQLmini() {
		sql = "";
	}

	public static ISQLmini newSQL() {
		return new ISQLmini();
	}

	public ISQLmini select(String columnNames) {

		sql += " select " + columnNames;

		return this;
	}

	public ISQLmini from(String tableName) {
		sql += " from " + tableName;
		return this;
	}
	
	public ISQLmini connectByPrior(String columnName) {
		sql += " connect by prior " + columnName;
		return this;
	}

	public ISQLmini startWith(String columnName) {
		sql += " start with " + columnName;
		return this;
	}
	
	public ISQLmini join(String columnName) {
		sql += " join " + columnName;
		return this;
	}

	public ISQLmini leftJoin(String columnName) {
		sql += " left Join " + columnName;
		return this;
	}

	public ISQLmini rightJoin(String columnName) {
		sql += " right Join " + columnName;
		return this;
	}

	public ISQLmini innerJoin(String columnName) {
		sql += " inner Join " + columnName;
		return this;
	}

	public ISQLmini fullJoin(String columnName) {
		sql += " full Join " + columnName;
		return this;
	}

	public ISQLmini on(String condition) {
		sql += " on " + condition;
		return this;
	}

	public ISQLmini where(String condition) {
		sql += " where " + condition;
		return this;
	}

	public ISQLmini group(String columnName) {
		sql += " group by " + columnName;
		return this;
	}

	public ISQLmini order(String columnName) {
		sql += " order by " + columnName;
		return this;
	}

	@Override
	public String toString() {
		return sql;
	}

	public static void main(String[] args) {

		String sql = ISQLmini.newSQL()
				.select("t.*")
				.from("dsc_company t")
				.connectByPrior("t.id = t.lastId")
				.startWith("t.id = '14415981168943'")
				.toString();

		System.out.println(sql);

	}

}
