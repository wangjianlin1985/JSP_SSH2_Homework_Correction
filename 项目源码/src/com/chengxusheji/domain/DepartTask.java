package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class DepartTask {
    /*作业id*/
    private int taskId;
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /*作业班级*/
    private Department departmentObj;
    public Department getDepartmentObj() {
        return departmentObj;
    }
    public void setDepartmentObj(Department departmentObj) {
        this.departmentObj = departmentObj;
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

    /*发布日期*/
    private String publishDate;
    public String getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /*执行状态*/
    private String executeState;
    public String getExecuteState() {
        return executeState;
    }
    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }

    /*进度评价*/
    private String evaluate;
    public String getEvaluate() {
        return evaluate;
    }
    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDepartTask=new JSONObject(); 
		jsonDepartTask.accumulate("taskId", this.getTaskId());
		jsonDepartTask.accumulate("departmentObj", this.getDepartmentObj().getDepartmentName());
		jsonDepartTask.accumulate("departmentObjPri", this.getDepartmentObj().getDepartmentNo());
		jsonDepartTask.accumulate("title", this.getTitle());
		jsonDepartTask.accumulate("content", this.getContent());
		jsonDepartTask.accumulate("publishDate", this.getPublishDate().length()>19?this.getPublishDate().substring(0,19):this.getPublishDate());
		jsonDepartTask.accumulate("executeState", this.getExecuteState());
		jsonDepartTask.accumulate("evaluate", this.getEvaluate());
		return jsonDepartTask;
    }}