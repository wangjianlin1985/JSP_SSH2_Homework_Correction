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
import com.chengxusheji.domain.DepartCharge;
import com.chengxusheji.domain.StudentTask;

@Service @Transactional
public class StudentTaskDAO {

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

    /*添加StudentTask信息*/
    public void AddStudentTask(StudentTask studentTask) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(studentTask);
    }

    /*查询StudentTask信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<StudentTask> QueryStudentTaskInfo(String title,Student studentObj,String taskDate,DepartCharge chargeObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From StudentTask studentTask where 1=1";
    	if(!title.equals("")) hql = hql + " and studentTask.title like '%" + title + "%'";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and studentTask.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(!taskDate.equals("")) hql = hql + " and studentTask.taskDate like '%" + taskDate + "%'";
    	if(null != chargeObj && !chargeObj.getChargeUserName().equals("")) hql += " and studentTask.chargeObj.chargeUserName='" + chargeObj.getChargeUserName() + "'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List studentTaskList = q.list();
    	return (ArrayList<StudentTask>) studentTaskList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<StudentTask> QueryStudentTaskInfo(String title,Student studentObj,String taskDate,DepartCharge chargeObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From StudentTask studentTask where 1=1";
    	if(!title.equals("")) hql = hql + " and studentTask.title like '%" + title + "%'";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and studentTask.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(!taskDate.equals("")) hql = hql + " and studentTask.taskDate like '%" + taskDate + "%'";
    	if(null != chargeObj && !chargeObj.getChargeUserName().equals("")) hql += " and studentTask.chargeObj.chargeUserName='" + chargeObj.getChargeUserName() + "'";
    	Query q = s.createQuery(hql);
    	List studentTaskList = q.list();
    	return (ArrayList<StudentTask>) studentTaskList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<StudentTask> QueryAllStudentTaskInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From StudentTask";
        Query q = s.createQuery(hql);
        List studentTaskList = q.list();
        return (ArrayList<StudentTask>) studentTaskList;
    }
    
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<StudentTask> QueryAllStudentTaskInfo(String studentNo) {
        Session s = factory.getCurrentSession();
        String hql = "From StudentTask et where et.studentObj.studentNo='" + studentNo + "'";
        Query q = s.createQuery(hql);
        List studentTaskList = q.list();
        return (ArrayList<StudentTask>) studentTaskList;
    }
    

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String title,Student studentObj,String taskDate,DepartCharge chargeObj) {
        Session s = factory.getCurrentSession();
        String hql = "From StudentTask studentTask where 1=1";
        if(!title.equals("")) hql = hql + " and studentTask.title like '%" + title + "%'";
        if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and studentTask.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
        if(!taskDate.equals("")) hql = hql + " and studentTask.taskDate like '%" + taskDate + "%'";
        if(null != chargeObj && !chargeObj.getChargeUserName().equals("")) hql += " and studentTask.chargeObj.chargeUserName='" + chargeObj.getChargeUserName() + "'";
        Query q = s.createQuery(hql);
        List studentTaskList = q.list();
        recordNumber = studentTaskList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public StudentTask GetStudentTaskByTaskId(int taskId) {
        Session s = factory.getCurrentSession();
        StudentTask studentTask = (StudentTask)s.get(StudentTask.class, taskId);
        return studentTask;
    }

    /*更新StudentTask信息*/
    public void UpdateStudentTask(StudentTask studentTask) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(studentTask);
    }

    /*删除StudentTask信息*/
    public void DeleteStudentTask (int taskId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object studentTask = s.load(StudentTask.class, taskId);
        s.delete(studentTask);
    }

}
