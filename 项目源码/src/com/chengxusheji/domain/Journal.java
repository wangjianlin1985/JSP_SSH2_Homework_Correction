package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Journal {
    /*日志id*/
    private int journalId;
    public int getJournalId() {
        return journalId;
    }
    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }

    /*日志标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*日志内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*日志日期*/
    private String journalDate;
    public String getJournalDate() {
        return journalDate;
    }
    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    /*学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonJournal=new JSONObject(); 
		jsonJournal.accumulate("journalId", this.getJournalId());
		jsonJournal.accumulate("title", this.getTitle());
		jsonJournal.accumulate("content", this.getContent());
		jsonJournal.accumulate("journalDate", this.getJournalDate().length()>19?this.getJournalDate().substring(0,19):this.getJournalDate());
		jsonJournal.accumulate("studentObj", this.getStudentObj().getName());
		jsonJournal.accumulate("studentObjPri", this.getStudentObj().getStudentNo());
		return jsonJournal;
    }}