package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class EmpProgress {
    /*进度id*/
    private int progressId;
    public int getProgressId() {
        return progressId;
    }
    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    /*所在作业*/
    private StudentTask taskObj;
    public StudentTask getTaskObj() {
        return taskObj;
    }
    public void setTaskObj(StudentTask taskObj) {
        this.taskObj = taskObj;
    }

    /*上传进度文件*/
    private String progressFile;
    public String getProgressFile() {
        return progressFile;
    }
    public void setProgressFile(String progressFile) {
        this.progressFile = progressFile;
    }

    /*上传的学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*上传时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /*评价*/
    private String evaluate;
    public String getEvaluate() {
        return evaluate;
    }
    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    /*评语*/
    private String evalateContent;
    public String getEvalateContent() {
        return evalateContent;
    }
    public void setEvalateContent(String evalateContent) {
        this.evalateContent = evalateContent;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonEmpProgress=new JSONObject(); 
		jsonEmpProgress.accumulate("progressId", this.getProgressId());
		jsonEmpProgress.accumulate("taskObj", this.getTaskObj().getTitle());
		jsonEmpProgress.accumulate("taskObjPri", this.getTaskObj().getTaskId());
		jsonEmpProgress.accumulate("progressFile", this.getProgressFile());
		jsonEmpProgress.accumulate("studentObj", this.getStudentObj().getName());
		jsonEmpProgress.accumulate("studentObjPri", this.getStudentObj().getStudentNo());
		jsonEmpProgress.accumulate("addTime", this.getAddTime());
		jsonEmpProgress.accumulate("evaluate", this.getEvaluate());
		jsonEmpProgress.accumulate("evalateContent", this.getEvalateContent());
		return jsonEmpProgress;
    }}