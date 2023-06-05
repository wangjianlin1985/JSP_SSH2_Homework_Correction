package com.chengxusheji.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.dao.JournalDAO;
import com.chengxusheji.domain.Journal;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
@Controller @Scope("prototype")
public class JournalAction extends ActionSupport {

    /*界面层需要查询的属性: 日志标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 日志日期*/
    private String journalDate;
    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }
    public String getJournalDate() {
        return this.journalDate;
    }

    /*界面层需要查询的属性: 学生*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
    }

    /*当前第几页*/
    private int page;
    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }

    /*每页显示多少条数据*/
    private int rows;
    public void setRows(int rows) {
    	this.rows = rows;
    }
    public int getRows() {
    	return this.rows;
    }
    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int journalId;
    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }
    public int getJournalId() {
        return journalId;
    }

    /*要删除的记录主键集合*/
    private String journalIds;
    public String getJournalIds() {
		return journalIds;
	}
	public void setJournalIds(String journalIds) {
		this.journalIds = journalIds;
	}
    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource JournalDAO journalDAO;

    @Resource StudentDAO studentDAO;
    /*待操作的Journal对象*/
    private Journal journal;
    public void setJournal(Journal journal) {
        this.journal = journal;
    }
    public Journal getJournal() {
        return this.journal;
    }

    /*ajax添加Journal信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddJournal() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(journal.getStudentObj().getStudentNo());
            journal.setStudentObj(studentObj);
            journalDAO.AddJournal(journal);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Journal添加失败!";
            writeJsonResponse(success, message);
        }
    }

    /*向客户端输出操作成功或失败信息*/
	private void writeJsonResponse(boolean success,String message)
			throws IOException, JSONException {
		HttpServletResponse response=ServletActionContext.getResponse(); 
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject json=new JSONObject();
		json.accumulate("success", success);
		json.accumulate("message", message);
		out.println(json.toString());
		out.flush(); 
		out.close();
	}

    /*查询Journal信息*/
    public void ajaxQueryJournal() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(journalDate == null) journalDate = "";
        if(rows != 0) journalDAO.setRows(rows);
        List<Journal> journalList = journalDAO.QueryJournalInfo(title, journalDate, studentObj, page);
        /*计算总的页数和总的记录数*/
        journalDAO.CalculateTotalPageAndRecordNumber(title, journalDate, studentObj);
        /*获取到总的页码数目*/
        totalPage = journalDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = journalDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Journal journal:journalList) {
			JSONObject jsonJournal = journal.getJsonObject();
			jsonArray.put(jsonJournal);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Journal信息*/
    public void ajaxQueryAllJournal() throws IOException, JSONException {
        List<Journal> journalList = journalDAO.QueryAllJournalInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Journal journal:journalList) {
			JSONObject jsonJournal = new JSONObject();
			jsonJournal.accumulate("journalId", journal.getJournalId());
			jsonJournal.accumulate("title", journal.getTitle());
			jsonArray.put(jsonJournal);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Journal信息*/
    public void ajaxModifyJournalQuery() throws IOException, JSONException {
        /*根据主键journalId获取Journal对象*/
        Journal journal = journalDAO.GetJournalByJournalId(journalId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonJournal = journal.getJsonObject(); 
		out.println(jsonJournal.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Journal信息*/
    public void ajaxModifyJournal() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(journal.getStudentObj().getStudentNo());
            journal.setStudentObj(studentObj);
            journalDAO.UpdateJournal(journal);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Journal修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Journal信息*/
    public void ajaxDeleteJournal() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _journalIds[] = journalIds.split(",");
        	for(String _journalId: _journalIds) {
        		journalDAO.DeleteJournal(Integer.parseInt(_journalId));
        	}
        	success = true;
        	message = _journalIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Journal信息*/
    public String FrontQueryJournal() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(journalDate == null) journalDate = "";
        List<Journal> journalList = journalDAO.QueryJournalInfo(title, journalDate, studentObj, page);
        /*计算总的页数和总的记录数*/
        journalDAO.CalculateTotalPageAndRecordNumber(title, journalDate, studentObj);
        /*获取到总的页码数目*/
        totalPage = journalDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = journalDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("journalList",  journalList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("title", title);
        ctx.put("journalDate", journalDate);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "front_query_view";
    }

    /*查询要修改的Journal信息*/
    public String FrontShowJournalQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键journalId获取Journal对象*/
        Journal journal = journalDAO.GetJournalByJournalId(journalId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("journal",  journal);
        return "front_show_view";
    }

    /*删除Journal信息*/
    public String DeleteJournal() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            journalDAO.DeleteJournal(journalId);
            ctx.put("message", "Journal删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Journal删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryJournalOutputToExcel() { 
        if(title == null) title = "";
        if(journalDate == null) journalDate = "";
        List<Journal> journalList = journalDAO.QueryJournalInfo(title,journalDate,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Journal信息记录"; 
        String[] headers = { "日志id","日志标题","日志日期","学生"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<journalList.size();i++) {
        	Journal journal = journalList.get(i); 
        	dataset.add(new String[]{journal.getJournalId() + "",journal.getTitle(),journal.getJournalDate(),journal.getStudentObj().getName()
});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Journal.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
}
