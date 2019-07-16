package mis.li.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao<T> {
	@Resource(name = "sessionFactory")
	public SessionFactory sessionFactory;

	private Criteria criteria;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	
	@SuppressWarnings("unchecked")
	public List<T> queryAllByHql(String str) {
		Session session = getSession();
		Query query = session.createQuery(str);
		return query.list();
	}

	public int executeSQL(String str) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(str);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryPageByHql(String str, int currentPage, int perPageCount) {
		Session session = getSession();
		Query query = session.createQuery(str);
		query.setFirstResult(currentPage * perPageCount);
		query.setMaxResults(perPageCount);
		return query.list();
	}

	public long getTotalCountByHql(String str) {
		String hql = "select count(*) from (" + str + ") o";
		Session session = getSession();
		Query query = session.createQuery(hql);
		return Long.parseLong(query.uniqueResult().toString());
	}
	
	public long getTotalCountBySql(String sql) {
		String sql_count = "select count(*) from (" + sql + ") o";
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql_count);
		return Long.parseLong(query.uniqueResult().toString());
	}

	public Object queryUniqueByHql(String str) {
		Session session = getSession();
		Query query = session.createQuery(str);
		return query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryAllBySql(String str) {
		Session session = getSession();
		Query query = session.createSQLQuery(str);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryPageBySql(String str,int start, int perPageCount) {
		Session session = getSession();
		Query query = session.createSQLQuery(str);
		query.setFirstResult(start);
		query.setMaxResults(perPageCount);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryAllBySql(Class<T> clazz ,String str) {
		Session session = getSession();
		Query query = session.createSQLQuery(str).addEntity(clazz);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryPageBySql(String str, Class<T> clazz ,int start, int limit) {
		Session session = getSession();
		Query query = session.createSQLQuery(str).addEntity(clazz);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		return query.list();
	}

	public long getTotalCountBySql(String str, Object[] objs) {
		String hql = "select count(*) from (" + str + ") o";
		Session session = getSession();
		Query query = session.createSQLQuery(hql);

		if (null != objs) {
			for (int i = 0; i < objs.length; i++) {
				query.setParameter(i, objs[i]);
			}
		}

		return Long.parseLong(query.uniqueResult().toString());
	}

	@SuppressWarnings("rawtypes")
	public Object queryUniqueBySql(String str) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(str);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(1);
		
		List list = query.list();
		
		return null == list || list.size() ==0 ? null :list.get(0);
	}

	public Serializable save(Object obj) {
		Session session = getSession();
		return session.save(obj);
	}

	public void update(Object obj) {
		Session session = getSession();
		session.update(obj);
	}

	public void saveOrUpdate(Object obj) {
		Session session = getSession();
		session.saveOrUpdate(obj);
	}

	public void delete(Object obj) {
		Session session = getSession();
		session.delete(obj);
	}

	/*******************************************************************
	 * 通用的查询方法
	 */

	public Criteria getCriteria() {
		return criteria;
	}

	@SuppressWarnings("rawtypes")
	public BaseDao createCriteria(Class<T> clazz) {

		criteria = null;
		Session session = getSession();
		criteria = session.createCriteria(clazz);

		return this;
	}

	/**
	 * propertyName like value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao like(String propertyName, Object value) {
		criteria.add(Restrictions.like(propertyName, value));
		return this;
	}

	/**
	 * 将map中所有的键值都转换成 propertyName = value ..... keyn = valuen
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao allEqual(Map<String, T> map) {
		criteria.add(Restrictions.allEq(map));
		return this;
	}

	/**
	 * propertyName = value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao equal(String propertyName, Object value) {
		criteria.add(Restrictions.eq(propertyName, value));
		return this;
	}

	/**
	 * propertyName != value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao notEqual(String propertyName, Object value) {
		criteria.add(Restrictions.ne(propertyName, value));
		return this;
	}

	/**
	 * propertyName between lo and hi
	 * 
	 * @param propertyName
	 * @param lo
	 * @param hi
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao between(String propertyName, Object lo, Object hi) {
		criteria.add(Restrictions.between(propertyName, lo, hi));
		return this;
	}

	/**
	 * propertyName in {value1......valuen}
	 * 
	 * @param propertyName
	 * @param values
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao in(String propertyName, Object[] values) {
		criteria.add(Restrictions.in(propertyName, values));
		return this;
	}

	@SuppressWarnings("rawtypes")
	public BaseDao in(String propertyName, Collection values) {
		criteria.add(Restrictions.in(propertyName, values));
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public BaseDao notIn(String propertyName, Collection values) {
		criteria.add(Restrictions.not(Restrictions.in(propertyName, values)));
		return this;
	}

	/**
	 * propertyName = ''
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao isEmpty(String propertyName) {
		criteria.add(Restrictions.isEmpty(propertyName));
		return this;
	}

	/**
	 * propertyName != ''
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao isNotEmpty(String propertyName) {
		criteria.add(Restrictions.isNotEmpty(propertyName));
		return this;
	}

	/**
	 * propertyName is null
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao isNull(String propertyName) {
		criteria.add(Restrictions.isNull(propertyName));
		return this;
	}

	/**
	 * propertyName is not null
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao isNotNull(String propertyName) {
		criteria.add(Restrictions.isNotNull(propertyName));
		return this;
	}

	/**
	 * propertyName <= value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao lessThanOrEqual(String propertyName, Object value) {
		criteria.add(Restrictions.le(propertyName, value));
		return this;
	}

	/**
	 * propertyName < value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao lessThan(String propertyName, Object value) {
		criteria.add(Restrictions.lt(propertyName, value));
		return this;
	}

	/**
	 * propertyName >= value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao greaterThanOrEqual(String propertyName, Object value) {
		criteria.add(Restrictions.ge(propertyName, value));
		return this;
	}

	/**
	 * propertyName > value
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao greaterThan(String propertyName, Object value) {
		criteria.add(Restrictions.gt(propertyName, value));
		return this;
	}

	/**
	 * or 关联
	 * @param objs
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao or(Object[] objs) {

		if(null != objs && objs.length > 0){
		
			Disjunction disJun = Restrictions.disjunction();
			Junction jun = null;
	
			for (Object object : objs) {
				if(null == jun){
					jun = disJun.add((Criterion) object);
				}else{
					jun = jun.add((Criterion) object);
				}
			}
			criteria.add(jun);
		}

		return this;
	}

	/**
	 * 关联查询
	 * @param associationPath
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao createCriteria(String associationPath){
		
		criteria.createCriteria(associationPath);
		
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public BaseDao createCriteria(String associationPath,JoinType jt){
		
		criteria.createCriteria(associationPath,jt);
		
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public BaseDao createAlias(String associationPath,String alias){
		
		criteria.createAlias(associationPath,alias);
		
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public BaseDao createAlias(String associationPath,String alias,JoinType jt){
		
		criteria.createAlias(associationPath,alias,jt);
		
		return this;
	} 
	
	/**
	 * FetchMode
	 * @param key
	 * @param fm
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	public BaseDao setFetchMode(String key,FetchMode fm){
		criteria.setFetchMode(key, fm);
		return this;
	}
	
	/**
	 * 递增排序
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao orderAsc(String propertyName) {
		criteria.addOrder(Order.asc(propertyName));
		return this;
	}

	/**
	 * 递减排序
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDao orderDesc(String propertyName) {
		criteria.addOrder(Order.desc(propertyName));
		return this;
	}

	/**
	 * 根据criteria查询
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> query() {
		return criteria.list();
	}

	/**
	 * 根据criteria翻页查询
	 * 
	 * @param start
	 * @param perPageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(int start, int perPageSize) {
		criteria.setFirstResult(start);
		criteria.setMaxResults(perPageSize);

		return criteria.list();
	}

	/**
	 * 根据条件返回一条数据
	 * 
	 * @return
	 */
	public Object queryUnique() {
		return criteria.uniqueResult();
	}

	/**
	 * 输出criteria中的SQL语句
	 * 
	 * @return
	 */
	public String getCriteriaSql() {
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;// 转型
		SessionImplementor session = criteriaImpl.getSession();// 获取SESSION
		SessionFactoryImplementor factory = session.getFactory();// 获取FACTORY
		CriteriaQueryTranslator translator = new CriteriaQueryTranslator(
				factory, criteriaImpl, criteriaImpl.getEntityOrClassName(),
				CriteriaQueryTranslator.ROOT_SQL_ALIAS);
		String[] implementors = factory.getImplementors(criteriaImpl
				.getEntityOrClassName());

		CriteriaJoinWalker walker = new CriteriaJoinWalker(
				(OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
				translator, factory, criteriaImpl, criteriaImpl
						.getEntityOrClassName(), session
						.getLoadQueryInfluencers());
		return walker.getSQLString();
	}

	/**
	 * 根据criteria对象中的条件查询所有数量
	 * 
	 * @return
	 */
	public long getTotalCount(Object[] objs) {
		return getTotalCountBySql(getCriteriaSql(), objs);
	}

}
