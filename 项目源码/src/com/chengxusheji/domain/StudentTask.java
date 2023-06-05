package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentTask {
    /*作业id*/
    private int taskId;
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /*作业标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*作业内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*作业学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*作业文件*/
    private String taskFile;
    public String getTaskFile() {
        return taskFile;
    }
    public void setTaskFile(String taskFile) {
        this.taskFile = taskFile;
    }

    /*作业发布日期*/
    private String taskDate;
    public String getTaskDate() {
        return taskDate;
    }
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    /*负责的老师*/
    private DepartCharge chargeObj;
    public DepartCharge getChargeObj() {
        return chargeObj;
    }
    public void setChargeObj(DepartCharge chargeObj) {
        this.chargeObj = chargeObj;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonStudentTask=new JSONObject(); 
		jsonStudentTask.accumulate("taskId", this.getTaskId());
		jsonStudentTask.accumulate("title", this.getTitle());
		jsonStudentTask.accumulate("content", this.getContent());
		jsonStudentTask.accumulate("studentObj", this.getStudentObj().getName());
		jsonStudentTask.accumulate("studentObjPri", this.getStudentObj().getStudentNo());
		jsonStudentTask.accumulate("taskFile", this.getTaskFile());
		jsonStudentTask.accumulate("taskDate", this.getTaskDate().length()>19?this.getTaskDate().substring(0,19):this.getTaskDate());
		jsonStudentTask.accumulate("chargeObj", this.getChargeObj().getName());
		jsonStudentTask.accumulate("chargeObjPri", this.getChargeObj().getChargeUserName());
		return jsonStudentTask;
    }}