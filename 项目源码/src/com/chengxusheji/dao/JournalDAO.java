package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Journal;

@Service @Transactional
public class JournalDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private int rows = 5;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加Journal信息*/
    public void AddJournal(Journal journal) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(journal);
    }

    /*查询Journal信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Journal> QueryJournalInfo(String title,String journalDate,Student studentObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Journal journal where 1=1";
    	if(!title.equals("")) hql = hql + " and journal.title like '%" + title + "%'";
    	if(!journalDate.equals("")) hql = hql + " and journal.journalDate like '%" + journalDate + "%'";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and journal.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List journalList = q.list();
    	return (ArrayList<Journal>) journalList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Journal> QueryJournalInfo(String title,String journalDate,Student studentObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Journal journal where 1=1";
    	if(!title.equals("")) hql = hql + " and journal.title like '%" + title + "%'";
    	if(!journalDate.equals("")) hql = hql + " and journal.journalDate like '%" + journalDate + "%'";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and journal.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	Query q = s.createQuery(hql);
    	List journalList = q.list();
    	return (ArrayList<Journal>) journalList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Journal> QueryAllJournalInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Journal";
        Query q = s.createQuery(hql);
        List journalList = q.list();
        return (ArrayList<Journal>) journalList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String title,String journalDate,Student studentObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Journal journal where 1=1";
        if(!title.equals("")) hql = hql + " and journal.title like '%" + title + "%'";
        if(!journalDate.equals("")) hql = hql + " and journal.journalDate like '%" + journalDate + "%'";
        if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and journal.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
        Query q = s.createQuery(hql);
        List journalList = q.list();
        recordNumber = journalList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Journal GetJournalByJournalId(int journalId) {
        Session s = factory.getCurrentSession();
        Journal journal = (Journal)s.get(Journal.class, journalId);
        return journal;
    }

    /*更新Journal信息*/
    public void UpdateJournal(Journal journal) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(journal);
    }

    /*删除Journal信息*/
    public void DeleteJournal (int journalId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object journal = s.load(Journal.class, journalId);
        s.delete(journal);
    }

}
