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
import com.chengxusheji.dao.StudentTaskDAO;
import com.chengxusheji.domain.StudentTask;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.dao.DepartChargeDAO;
import com.chengxusheji.domain.DepartCharge;
@Controller @Scope("prototype")
public class StudentTaskAction extends ActionSupport {

/*StudentTask类字段taskFile参数接收*/
	 private File taskFileFile;
	 private String taskFileFileFileName;
	 private String taskFileFileContentType;
	 public File getTaskFileFile() {
		return taskFileFile;
	}
	public void setTaskFileFile(File taskFileFile) {
		this.taskFileFile = taskFileFile;
	}
	public String getTaskFileFileFileName() {
		return taskFileFileFileName;
	}
	public void setTaskFileFileFileName(String taskFileFileFileName) {
		this.taskFileFileFileName = taskFileFileFileName;
	}
	public String getTaskFileFileContentType() {
		return taskFileFileContentType;
	}
	public void setTaskFileFileContentType(String taskFileFileContentType) {
		this.taskFileFileContentType = taskFileFileContentType;
	}
    /*界面层需要查询的属性: 作业标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 作业学生*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
    }

    /*界面层需要查询的属性: 作业发布日期*/
    private String taskDate;
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
    public String getTaskDate() {
        return this.taskDate;
    }

    /*界面层需要查询的属性: 负责的老师*/
    private DepartCharge chargeObj;
    public void setChargeObj(DepartCharge chargeObj) {
        this.chargeObj = chargeObj;
    }
    public DepartCharge getChargeObj() {
        return this.chargeObj;
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
    @Resource StudentTaskDAO studentTaskDAO;

    @Resource StudentDAO studentDAO;
    @Resource DepartChargeDAO departChargeDAO;
    /*待操作的StudentTask对象*/
    private StudentTask studentTask;
    public void setStudentTask(StudentTask studentTask) {
        this.studentTask = studentTask;
    }
    public StudentTask getStudentTask() {
        return this.studentTask;
    }

    /*ajax添加StudentTask信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddStudentTask() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(studentTask.getStudentObj().getStudentNo());
            studentTask.setStudentObj(studentObj);
            DepartCharge chargeObj = departChargeDAO.GetDepartChargeByChargeUserName(studentTask.getChargeObj().getChargeUserName());
            studentTask.setChargeObj(chargeObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String taskFileFileName = ""; 
       	 	if(taskFileFile != null) {
       	 		InputStream is = new FileInputStream(taskFileFile);
       	 		String extFileName = this.getTaskFileFileFileName().substring(this.getTaskFileFileFileName().lastIndexOf("."));
       			 
       			taskFileFileName = UUID.randomUUID().toString() + extFileName;
       			 
       			File file = new File(path, taskFileFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(taskFileFile != null)
            	studentTask.setTaskFile("upload/" + taskFileFileName);
            else {
            	 message = "请上传作业文件!";
                 writeJsonResponse(success, message);
                 return;
            }
            	 
            studentTaskDAO.AddStudentTask(studentTask);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "StudentTask添加失败!";
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

    /*查询StudentTask信息*/
    public void ajaxQueryStudentTask() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(taskDate == null) taskDate = "";
        if(rows != 0) studentTaskDAO.setRows(rows);
        List<StudentTask> studentTaskList = studentTaskDAO.QueryStudentTaskInfo(title, studentObj, taskDate, chargeObj, page);
        /*计算总的页数和总的记录数*/
        studentTaskDAO.CalculateTotalPageAndRecordNumber(title, studentObj, taskDate, chargeObj);
        /*获取到总的页码数目*/
        totalPage = studentTaskDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = studentTaskDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(StudentTask studentTask:studentTaskList) {
			JSONObject jsonStudentTask = studentTask.getJsonObject();
			jsonArray.put(jsonStudentTask);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询StudentTask信息*/
    public void ajaxQueryAllStudentTask() throws IOException, JSONException {
        List<StudentTask> studentTaskList = studentTaskDAO.QueryAllStudentTaskInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(StudentTask studentTask:studentTaskList) {
			JSONObject jsonStudentTask = new JSONObject();
			jsonStudentTask.accumulate("taskId", studentTask.getTaskId());
			jsonStudentTask.accumulate("title", studentTask.getTitle());
			jsonArray.put(jsonStudentTask);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }
    
    /*学生查询StudentTask信息*/
    public void ajaxStudentQueryAllStudentTask() throws IOException, JSONException {
    	ActionContext ctx = ActionContext.getContext();
    	String studentNo = ctx.getSession().get("student").toString();
        List<StudentTask> studentTaskList = studentTaskDAO.QueryAllStudentTaskInfo(studentNo);       
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(StudentTask studentTask:studentTaskList) {
			JSONObject jsonStudentTask = new JSONObject();
			jsonStudentTask.accumulate("taskId", studentTask.getTaskId());
			jsonStudentTask.accumulate("title", studentTask.getTitle());
			jsonArray.put(jsonStudentTask);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }
    

    /*查询要修改的StudentTask信息*/
    public void ajaxModifyStudentTaskQuery() throws IOException, JSONException {
        /*根据主键taskId获取StudentTask对象*/
        StudentTask studentTask = studentTaskDAO.GetStudentTaskByTaskId(taskId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonStudentTask = studentTask.getJsonObject(); 
		out.println(jsonStudentTask.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改StudentTask信息*/
    public void ajaxModifyStudentTask() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(studentTask.getStudentObj().getStudentNo());
            studentTask.setStudentObj(studentObj);
            DepartCharge chargeObj = departChargeDAO.GetDepartChargeByChargeUserName(studentTask.getChargeObj().getChargeUserName());
            studentTask.setChargeObj(chargeObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String taskFileFileName = ""; 
       	 	if(taskFileFile != null) {
       	 		InputStream is = new FileInputStream(taskFileFile);
       	 		String extFileName = this.getTaskFileFileFileName().substring(this.getTaskFileFileFileName().lastIndexOf("."));
  			 
       	 		 
       	 		taskFileFileName = UUID.randomUUID().toString() +  extFileName;
       			 
       			File file = new File(path, taskFileFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       			studentTask.setTaskFile("upload/" + taskFileFileName);
       	 	}
            studentTaskDAO.UpdateStudentTask(studentTask);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "StudentTask修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除StudentTask信息*/
    public void ajaxDeleteStudentTask() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _taskIds[] = taskIds.split(",");
        	for(String _taskId: _taskIds) {
        		studentTaskDAO.DeleteStudentTask(Integer.parseInt(_taskId));
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

    /*前台查询StudentTask信息*/
    public String FrontQueryStudentTask() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(taskDate == null) taskDate = "";
        List<StudentTask> studentTaskList = studentTaskDAO.QueryStudentTaskInfo(title, studentObj, taskDate, chargeObj, page);
        /*计算总的页数和总的记录数*/
        studentTaskDAO.CalculateTotalPageAndRecordNumber(title, studentObj, taskDate, chargeObj);
        /*获取到总的页码数目*/
        totalPage = studentTaskDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = studentTaskDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("studentTaskList",  studentTaskList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("title", title);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("taskDate", taskDate);
        ctx.put("chargeObj", chargeObj);
        List<DepartCharge> departChargeList = departChargeDAO.QueryAllDepartChargeInfo();
        ctx.put("departChargeList", departChargeList);
        return "front_query_view";
    }

    /*查询要修改的StudentTask信息*/
    public String FrontShowStudentTaskQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键taskId获取StudentTask对象*/
        StudentTask studentTask = studentTaskDAO.GetStudentTaskByTaskId(taskId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        List<DepartCharge> departChargeList = departChargeDAO.QueryAllDepartChargeInfo();
        ctx.put("departChargeList", departChargeList);
        ctx.put("studentTask",  studentTask);
        return "front_show_view";
    }

    /*删除StudentTask信息*/
    public String DeleteStudentTask() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            studentTaskDAO.DeleteStudentTask(taskId);
            ctx.put("message", "StudentTask删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "StudentTask删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryStudentTaskOutputToExcel() { 
        if(title == null) title = "";
        if(taskDate == null) taskDate = "";
        List<StudentTask> studentTaskList = studentTaskDAO.QueryStudentTaskInfo(title,studentObj,taskDate,chargeObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "StudentTask信息记录"; 
        String[] headers = { "作业id","作业标题","作业学生","作业文件","作业发布日期","负责的老师"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<studentTaskList.size();i++) {
        	StudentTask studentTask = studentTaskList.get(i); 
        	dataset.add(new String[]{studentTask.getTaskId() + "",studentTask.getTitle(),studentTask.getStudentObj().getName(),
studentTask.getTaskFile(),studentTask.getTaskDate(),studentTask.getChargeObj().getName()
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
			response.setHeader("Content-disposition","attachment; filename="+"StudentTask.xls");//filename是下载的xls的名，建议最好用英文 
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
