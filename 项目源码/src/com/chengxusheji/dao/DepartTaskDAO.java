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
import com.chengxusheji.domain.Department;
import com.chengxusheji.domain.DepartTask;

@Service @Transactional
public class DepartTaskDAO {

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

    /*添加DepartTask信息*/
    public void AddDepartTask(DepartTask departTask) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(departTask);
    }

    /*查询DepartTask信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartTask> QueryDepartTaskInfo(Department departmentObj,String title,String publishDate,String executeState,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From DepartTask departTask where 1=1";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departTask.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!title.equals("")) hql = hql + " and departTask.title like '%" + title + "%'";
    	if(!publishDate.equals("")) hql = hql + " and departTask.publishDate like '%" + publishDate + "%'";
    	if(!executeState.equals("")) hql = hql + " and departTask.executeState like '%" + executeState + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List departTaskList = q.list();
    	return (ArrayList<DepartTask>) departTaskList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartTask> QueryDepartTaskInfo(Department departmentObj,String title,String publishDate,String executeState) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From DepartTask departTask where 1=1";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departTask.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!title.equals("")) hql = hql + " and departTask.title like '%" + title + "%'";
    	if(!publishDate.equals("")) hql = hql + " and departTask.publishDate like '%" + publishDate + "%'";
    	if(!executeState.equals("")) hql = hql + " and departTask.executeState like '%" + executeState + "%'";
    	Query q = s.createQuery(hql);
    	List departTaskList = q.list();
    	return (ArrayList<DepartTask>) departTaskList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartTask> QueryAllDepartTaskInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From DepartTask";
        Query q = s.createQuery(hql);
        List departTaskList = q.list();
        return (ArrayList<DepartTask>) departTaskList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Department departmentObj,String title,String publishDate,String executeState) {
        Session s = factory.getCurrentSession();
        String hql = "From DepartTask departTask where 1=1";
        if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departTask.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
        if(!title.equals("")) hql = hql + " and departTask.title like '%" + title + "%'";
        if(!publishDate.equals("")) hql = hql + " and departTask.publishDate like '%" + publishDate + "%'";
        if(!executeState.equals("")) hql = hql + " and departTask.executeState like '%" + executeState + "%'";
        Query q = s.createQuery(hql);
        List departTaskList = q.list();
        recordNumber = departTaskList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public DepartTask GetDepartTaskByTaskId(int taskId) {
        Session s = factory.getCurrentSession();
        DepartTask departTask = (DepartTask)s.get(DepartTask.class, taskId);
        return departTask;
    }

    /*更新DepartTask信息*/
    public void UpdateDepartTask(DepartTask departTask) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(departTask);
    }

    /*删除DepartTask信息*/
    public void DeleteDepartTask (int taskId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object departTask = s.load(DepartTask.class, taskId);
        s.delete(departTask);
    }

}
