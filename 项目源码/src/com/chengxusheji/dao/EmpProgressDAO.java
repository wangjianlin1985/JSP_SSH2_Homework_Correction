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
import com.chengxusheji.domain.StudentTask;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.EmpProgress;

@Service @Transactional
public class EmpProgressDAO {

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

    /*添加EmpProgress信息*/
    public void AddEmpProgress(EmpProgress empProgress) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(empProgress);
    }

    /*查询EmpProgress信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<EmpProgress> QueryEmpProgressInfo(StudentTask taskObj,Student studentObj,String addTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From EmpProgress empProgress where 1=1";
    	if(null != taskObj && taskObj.getTaskId()!=0) hql += " and empProgress.taskObj.taskId=" + taskObj.getTaskId();
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and empProgress.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(!addTime.equals("")) hql = hql + " and empProgress.addTime like '%" + addTime + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List empProgressList = q.list();
    	return (ArrayList<EmpProgress>) empProgressList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<EmpProgress> QueryEmpProgressInfo(StudentTask taskObj,Student studentObj,String addTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From EmpProgress empProgress where 1=1";
    	if(null != taskObj && taskObj.getTaskId()!=0) hql += " and empProgress.taskObj.taskId=" + taskObj.getTaskId();
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and empProgress.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(!addTime.equals("")) hql = hql + " and empProgress.addTime like '%" + addTime + "%'";
    	Query q = s.createQuery(hql);
    	List empProgressList = q.list();
    	return (ArrayList<EmpProgress>) empProgressList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<EmpProgress> QueryAllEmpProgressInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From EmpProgress";
        Query q = s.createQuery(hql);
        List empProgressList = q.list();
        return (ArrayList<EmpProgress>) empProgressList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(StudentTask taskObj,Student studentObj,String addTime) {
        Session s = factory.getCurrentSession();
        String hql = "From EmpProgress empProgress where 1=1";
        if(null != taskObj && taskObj.getTaskId()!=0) hql += " and empProgress.taskObj.taskId=" + taskObj.getTaskId();
        if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and empProgress.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
        if(!addTime.equals("")) hql = hql + " and empProgress.addTime like '%" + addTime + "%'";
        Query q = s.createQuery(hql);
        List empProgressList = q.list();
        recordNumber = empProgressList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public EmpProgress GetEmpProgressByProgressId(int progressId) {
        Session s = factory.getCurrentSession();
        EmpProgress empProgress = (EmpProgress)s.get(EmpProgress.class, progressId);
        return empProgress;
    }

    /*更新EmpProgress信息*/
    public void UpdateEmpProgress(EmpProgress empProgress) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(empProgress);
    }

    /*删除EmpProgress信息*/
    public void DeleteEmpProgress (int progressId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object empProgress = s.load(EmpProgress.class, progressId);
        s.delete(empProgress);
    }

}
