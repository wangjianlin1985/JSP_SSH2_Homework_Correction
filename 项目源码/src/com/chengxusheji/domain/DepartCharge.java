package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class DepartCharge {
    /*用户名*/
    private String chargeUserName;
    public String getChargeUserName() {
        return chargeUserName;
    }
    public void setChargeUserName(String chargeUserName) {
        this.chargeUserName = chargeUserName;
    }

    /*登录密码*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*所在班级*/
    private Department departmentObj;
    public Department getDepartmentObj() {
        return departmentObj;
    }
    public void setDepartmentObj(Department departmentObj) {
        this.departmentObj = departmentObj;
    }

    /*姓名*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*性别*/
    private String sex;
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    /*出生日期*/
    private String birthday;
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /*邮箱地址*/
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDepartCharge=new JSONObject(); 
		jsonDepartCharge.accumulate("chargeUserName", this.getChargeUserName());
		jsonDepartCharge.accumulate("password", this.getPassword());
		jsonDepartCharge.accumulate("departmentObj", this.getDepartmentObj().getDepartmentName());
		jsonDepartCharge.accumulate("departmentObjPri", this.getDepartmentObj().getDepartmentNo());
		jsonDepartCharge.accumulate("name", this.getName());
		jsonDepartCharge.accumulate("sex", this.getSex());
		jsonDepartCharge.accumulate("birthday", this.getBirthday().length()>19?this.getBirthday().substring(0,19):this.getBirthday());
		jsonDepartCharge.accumulate("email", this.getEmail());
		return jsonDepartCharge;
    }}