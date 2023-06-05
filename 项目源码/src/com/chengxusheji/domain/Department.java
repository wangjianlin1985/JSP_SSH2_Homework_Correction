package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Department {
    /*班级编号*/
    private String departmentNo;
    public String getDepartmentNo() {
        return departmentNo;
    }
    public void setDepartmentNo(String departmentNo) {
        this.departmentNo = departmentNo;
    }

    /*班级名称*/
    private String departmentName;
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /*成立日期*/
    private String bornDate;
    public String getBornDate() {
        return bornDate;
    }
    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDepartment=new JSONObject(); 
		jsonDepartment.accumulate("departmentNo", this.getDepartmentNo());
		jsonDepartment.accumulate("departmentName", this.getDepartmentName());
		jsonDepartment.accumulate("bornDate", this.getBornDate());
		return jsonDepartment;
    }}