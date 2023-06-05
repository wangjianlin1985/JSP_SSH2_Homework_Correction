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
import com.chengxusheji.domain.Department;
import com.chengxusheji.domain.DepartCharge;

@Service @Transactional
public class DepartChargeDAO {

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

		DepartCharge db_departCharge = (DepartCharge)s.get(DepartCharge.class, admin.getUsername());
		if(db_departCharge == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_departCharge.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	
	
    /*添加DepartCharge信息*/
    public void AddDepartCharge(DepartCharge departCharge) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(departCharge);
    }

    /*查询DepartCharge信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartCharge> QueryDepartChargeInfo(String chargeUserName,Department departmentObj,String name,String birthday,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From DepartCharge departCharge where 1=1";
    	if(!chargeUserName.equals("")) hql = hql + " and departCharge.chargeUserName like '%" + chargeUserName + "%'";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departCharge.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!name.equals("")) hql = hql + " and departCharge.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and departCharge.birthday like '%" + birthday + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List departChargeList = q.list();
    	return (ArrayList<DepartCharge>) departChargeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartCharge> QueryDepartChargeInfo(String chargeUserName,Department departmentObj,String name,String birthday) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From DepartCharge departCharge where 1=1";
    	if(!chargeUserName.equals("")) hql = hql + " and departCharge.chargeUserName like '%" + chargeUserName + "%'";
    	if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departCharge.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
    	if(!name.equals("")) hql = hql + " and departCharge.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and departCharge.birthday like '%" + birthday + "%'";
    	Query q = s.createQuery(hql);
    	List departChargeList = q.list();
    	return (ArrayList<DepartCharge>) departChargeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<DepartCharge> QueryAllDepartChargeInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From DepartCharge";
        Query q = s.createQuery(hql);
        List departChargeList = q.list();
        return (ArrayList<DepartCharge>) departChargeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String chargeUserName,Department departmentObj,String name,String birthday) {
        Session s = factory.getCurrentSession();
        String hql = "From DepartCharge departCharge where 1=1";
        if(!chargeUserName.equals("")) hql = hql + " and departCharge.chargeUserName like '%" + chargeUserName + "%'";
        if(null != departmentObj && !departmentObj.getDepartmentNo().equals("")) hql += " and departCharge.departmentObj.departmentNo='" + departmentObj.getDepartmentNo() + "'";
        if(!name.equals("")) hql = hql + " and departCharge.name like '%" + name + "%'";
        if(!birthday.equals("")) hql = hql + " and departCharge.birthday like '%" + birthday + "%'";
        Query q = s.createQuery(hql);
        List departChargeList = q.list();
        recordNumber = departChargeList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public DepartCharge GetDepartChargeByChargeUserName(String chargeUserName) {
        Session s = factory.getCurrentSession();
        DepartCharge departCharge = (DepartCharge)s.get(DepartCharge.class, chargeUserName);
        return departCharge;
    }

    /*更新DepartCharge信息*/
    public void UpdateDepartCharge(DepartCharge departCharge) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(departCharge);
    }

    /*删除DepartCharge信息*/
    public void DeleteDepartCharge (String chargeUserName) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object departCharge = s.load(DepartCharge.class, chargeUserName);
        s.delete(departCharge);
    }
	 

}
