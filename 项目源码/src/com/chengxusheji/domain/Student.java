package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Student {
    /*学生编号*/
    private String studentNo;
    public String getStudentNo() {
        return studentNo;
    }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
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

    /*联系电话*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*学生照片*/
    private String studentPhoto;
    public String getStudentPhoto() {
        return studentPhoto;
    }
    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    /*家庭地址*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*附加信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonStudent=new JSONObject(); 
		jsonStudent.accumulate("studentNo", this.getStudentNo());
		jsonStudent.accumulate("password", this.getPassword());
		jsonStudent.accumulate("departmentObj", this.getDepartmentObj().getDepartmentName());
		jsonStudent.accumulate("departmentObjPri", this.getDepartmentObj().getDepartmentNo());
		jsonStudent.accumulate("name", this.getName());
		jsonStudent.accumulate("sex", this.getSex());
		jsonStudent.accumulate("birthday", this.getBirthday().length()>19?this.getBirthday().substring(0,19):this.getBirthday());
		jsonStudent.accumulate("telephone", this.getTelephone());
		jsonStudent.accumulate("studentPhoto", this.getStudentPhoto());
		jsonStudent.accumulate("address", this.getAddress());
		jsonStudent.accumulate("memo", this.getMemo());
		return jsonStudent;
    }}