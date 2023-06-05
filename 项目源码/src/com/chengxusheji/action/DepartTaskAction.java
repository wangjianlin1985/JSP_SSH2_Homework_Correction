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
import com.chengxusheji.dao.DepartTaskDAO;
import com.chengxusheji.domain.DepartTask;
import com.chengxusheji.dao.DepartmentDAO;
import com.chengxusheji.domain.Department;
@Controller @Scope("prototype")
public class DepartTaskAction extends ActionSupport {

    /*界面层需要查询的属性: 作业班级*/
    private Department departmentObj;
    public void setDepartmentObj(Department departmentObj) {
        this.departmentObj = departmentObj;
    }
    public Department getDepartmentObj() {
        return this.departmentObj;
    }

    /*界面层需要查询的属性: 作业标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 发布日期*/
    private String publishDate;
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublishDate() {
        return this.publishDate;
    }

    /*界面层需要查询的属性: 执行状态*/
    private String executeState;
    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }
    public String getExecuteState() {
        return this.executeState;
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

    private int taskId;
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public int getTaskId() {
        return taskId;
    }

    /*要删除的记录主键集合*/
    private String taskIds;
    public String getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
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
    @Resource DepartTaskDAO departTaskDAO;

    @Resource DepartmentDAO departmentDAO;
    /*待操作的DepartTask对象*/
    private DepartTask departTask;
    public void setDepartTask(DepartTask departTask) {
        this.departTask = departTask;
    }
    public DepartTask getDepartTask() {
        return this.departTask;
    }

    /*ajax添加DepartTask信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddDepartTask() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(departTask.getDepartmentObj().getDepartmentNo());
            departTask.setDepartmentObj(departmentObj);
            departTaskDAO.AddDepartTask(departTask);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "DepartTask添加失败!";
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

    /*查询DepartTask信息*/
    public void ajaxQueryDepartTask() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        if(executeState == null) executeState = "";
        if(rows != 0) departTaskDAO.setRows(rows);
        List<DepartTask> departTaskList = departTaskDAO.QueryDepartTaskInfo(departmentObj, title, publishDate, executeState, page);
        /*计算总的页数和总的记录数*/
        departTaskDAO.CalculateTotalPageAndRecordNumber(departmentObj, title, publishDate, executeState);
        /*获取到总的页码数目*/
        totalPage = departTaskDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departTaskDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(DepartTask departTask:departTaskList) {
			JSONObject jsonDepartTask = departTask.getJsonObject();
			jsonArray.put(jsonDepartTask);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询DepartTask信息*/
    public void ajaxQueryAllDepartTask() throws IOException, JSONException {
        List<DepartTask> departTaskList = departTaskDAO.QueryAllDepartTaskInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(DepartTask departTask:departTaskList) {
			JSONObject jsonDepartTask = new JSONObject();
			jsonDepartTask.accumulate("taskId", departTask.getTaskId());
			jsonDepartTask.accumulate("title", departTask.getTitle());
			jsonArray.put(jsonDepartTask);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的DepartTask信息*/
    public void ajaxModifyDepartTaskQuery() throws IOException, JSONException {
        /*根据主键taskId获取DepartTask对象*/
        DepartTask departTask = departTaskDAO.GetDepartTaskByTaskId(taskId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonDepartTask = departTask.getJsonObject(); 
		out.println(jsonDepartTask.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改DepartTask信息*/
    public void ajaxModifyDepartTask() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(departTask.getDepartmentObj().getDepartmentNo());
            departTask.setDepartmentObj(departmentObj);
            departTaskDAO.UpdateDepartTask(departTask);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "DepartTask修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除DepartTask信息*/
    public void ajaxDeleteDepartTask() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _taskIds[] = taskIds.split(",");
        	for(String _taskId: _taskIds) {
        		departTaskDAO.DeleteDepartTask(Integer.parseInt(_taskId));
        	}
        	success = true;
        	message = _taskIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询DepartTask信息*/
    public String FrontQueryDepartTask() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        if(executeState == null) executeState = "";
        List<DepartTask> departTaskList = departTaskDAO.QueryDepartTaskInfo(departmentObj, title, publishDate, executeState, page);
        /*计算总的页数和总的记录数*/
        departTaskDAO.CalculateTotalPageAndRecordNumber(departmentObj, title, publishDate, executeState);
        /*获取到总的页码数目*/
        totalPage = departTaskDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departTaskDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("departTaskList",  departTaskList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("departmentObj", departmentObj);
        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("title", title);
        ctx.put("publishDate", publishDate);
        ctx.put("executeState", executeState);
        return "front_query_view";
    }

    /*查询要修改的DepartTask信息*/
    public String FrontShowDepartTaskQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键taskId获取DepartTask对象*/
        DepartTask departTask = departTaskDAO.GetDepartTaskByTaskId(taskId);

        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("departTask",  departTask);
        return "front_show_view";
    }

    /*删除DepartTask信息*/
    public String DeleteDepartTask() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            departTaskDAO.DeleteDepartTask(taskId);
            ctx.put("message", "DepartTask删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "DepartTask删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryDepartTaskOutputToExcel() { 
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        if(executeState == null) executeState = "";
        List<DepartTask> departTaskList = departTaskDAO.QueryDepartTaskInfo(departmentObj,title,publishDate,executeState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "DepartTask信息记录"; 
        String[] headers = { "作业id","作业班级","作业标题","发布日期","执行状态","进度评价"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<departTaskList.size();i++) {
        	DepartTask departTask = departTaskList.get(i); 
        	dataset.add(new String[]{departTask.getTaskId() + "",departTask.getDepartmentObj().getDepartmentName(),
departTask.getTitle(),departTask.getPublishDate(),departTask.getExecuteState(),departTask.getEvaluate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"DepartTask.xls");//filename是下载的xls的名，建议最好用英文 
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
