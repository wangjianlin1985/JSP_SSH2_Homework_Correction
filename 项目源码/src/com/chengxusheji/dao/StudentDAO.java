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

import com.chengxusheji.domain.Admin;
import com.chengxusheji.domain.DepartCharge;
import com.chengxusheji.domain.Department;
import com.chengxusheji.domain.Student;

@Service @Transactional
public class StudentDAO {

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

    /*保存业务逻辑错误信息字段*/
	private String errMessage;
	public String getErrMessage() { return this.errMessage; }
	
	/*验证用户登录*/
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean CheckLogin(Admin admin) { 
		Session s = factory.getCurrentSession(); 

		Student db_student = (Student)s.get(Student.class, admin.getUsername());
		if(db_student == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_student.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	
    /*添加Student信息*/
    public void AddStudent(Student student) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(student);
    }

    /*查询Student信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Student> QueryStudentInfo(String studentNo,Department departmentObj,String name,String birthday,String telephone,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Student student where 1=1";
    	if(!studentNo.equals("")) hql = hql + " and student.studentNo like '%" + studentNo + "%'";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and student.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!name.equals("")) hql = hql + " and student.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and student.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) hql = hql + " and student.telephone like '%" + telephone + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List studentList = q.list();
    	return (ArrayList<Student>) studentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Student> QueryStudentInfo(String studentNo,Department departmentObj,String name,String birthday,String telephone) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Student student where 1=1";
    	if(!studentNo.equals("")) hql = hql + " and student.studentNo like '%" + studentNo + "%'";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and student.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!name.equals("")) hql = hql + " and student.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and student.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) hql = hql + " and student.telephone like '%" + telephone + "%'";
    	Query q = s.createQuery(hql);
    	List studentList = q.list();
    	return (ArrayList<Student>) studentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Student> QueryAllStudentInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Student";
        Query q = s.createQuery(hql);
        List studentList = q.list();
        return (ArrayList<Student>) studentList;
    }
    
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Student> QueryAllStudentInfo(String departmentNo) {
        Session s = factory.getCurrentSession();
        String hql = "From Student student where student.departmentObj.departmentNo='" + departmentNo + "'";
        Query q = s.createQuery(hql);
        List studentList = q.list();
        return (ArrayList<Student>) studentList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String studentNo,Department departmentObj,String name,String birthday,String telephone) {
        Session s = factory.getCurrentSession();
        String hql = "From Student student where 1=1";
        if(!studentNo.equals("")) hql = hql + " and student.studentNo like '%" + studentNo + "%'";
        if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and student.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
        if(!name.equals("")) hql = hql + " and student.name like '%" + name + "%'";
        if(!birthday.equals("")) hql = hql + " and student.birthday like '%" + birthday + "%'";
        if(!telephone.equals("")) hql = hql + " and student.telephone like '%" + telephone + "%'";
        Query q = s.createQuery(hql);
        List studentList = q.list();
        recordNumber = studentList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Student GetStudentByStudentNo(String studentNo) {
        Session s = factory.getCurrentSession();
        Student student = (Student)s.get(Student.class, studentNo);
        return student;
    }

    /*更新Student信息*/
    public void UpdateStudent(Student student) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(student);
    }

    /*删除Student信息*/
    public void DeleteStudent (String studentNo) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object student = s.load(Student.class, studentNo);
        s.delete(student);
    }
	 

}
