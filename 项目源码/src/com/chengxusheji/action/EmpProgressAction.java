package com.chengxusheji.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.chengxusheji.dao.DepartChargeDAO;
import com.chengxusheji.dao.EmpProgressDAO;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.dao.StudentTaskDAO;
import com.chengxusheji.domain.EmpProgress;
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.StudentTask;
import com.chengxusheji.utils.ExportExcelUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@Controller @Scope("prototype")
public class EmpProgressAction extends ActionSupport {

/*EmpProgress类字段progressFile参数接收*/
	 private File progressFileFile;
	 private String progressFileFileFileName;
	 private String progressFileFileContentType;
	 public File getProgressFileFile() {
		return progressFileFile;
	}
	public void setProgressFileFile(File progressFileFile) {
		this.progressFileFile = progressFileFile;
	}
	public String getProgressFileFileFileName() {
		return progressFileFileFileName;
	}
	public void setProgressFileFileFileName(String progressFileFileFileName) {
		this.progressFileFileFileName = progressFileFileFileName;
	}
	public String getProgressFileFileContentType() {
		return progressFileFileContentType;
	}
	public void setProgressFileFileContentType(String progressFileFileContentType) {
		this.progressFileFileContentType = progressFileFileContentType;
	}
    /*界面层需要查询的属性: 所在作业*/
    private StudentTask taskObj;
    public void setTaskObj(StudentTask taskObj) {
        this.taskObj = taskObj;
    }
    public StudentTask getTaskObj() {
        return this.taskObj;
    }

    /*界面层需要查询的属性: 上传的学生*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
    }

    /*界面层需要查询的属性: 上传时间*/
    private String addTime;
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getAddTime() {
        return this.addTime;
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

    private int progressId;
    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }
    public int getProgressId() {
        return progressId;
    }

    /*要删除的记录主键集合*/
    private String progressIds;
    public String getProgressIds() {
		return progressIds;
	}
	public void setProgressIds(String progressIds) {
		this.progressIds = progressIds;
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
    @Resource EmpProgressDAO empProgressDAO;

    @Resource StudentTaskDAO studentTaskDAO;
    @Resource StudentDAO studentDAO;
    @Resource DepartChargeDAO departChargeDAO;
    /*待操作的EmpProgress对象*/
    private EmpProgress empProgress;
    public void setEmpProgress(EmpProgress empProgress) {
        this.empProgress = empProgress;
    }
    public EmpProgress getEmpProgress() {
        return this.empProgress;
    }

    /*ajax添加EmpProgress信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddEmpProgress() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            StudentTask taskObj = studentTaskDAO.GetStudentTaskByTaskId(empProgress.getTaskObj().getTaskId());
            empProgress.setTaskObj(taskObj);
            Student studentObj = studentDAO.GetStudentByStudentNo(empProgress.getStudentObj().getStudentNo());
            empProgress.setStudentObj(studentObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String progressFileFileName = ""; 
       	 	if(progressFileFile != null) {
       	 		InputStream is = new FileInputStream(progressFileFile);
       			 
       			String extFileName = this.getProgressFileFileFileName().substring(this.getProgressFileFileFileName().lastIndexOf("."));
       
       			progressFileFileName = UUID.randomUUID().toString() +  extFileName;
       			 
       			File file = new File(path, progressFileFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(progressFileFile != null)
            	empProgress.setProgressFile("upload/" + progressFileFileName);
            else {
            	 message = "请上传进度文件!";
                 writeJsonResponse(success, message);
                 return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            empProgress.setAddTime(sdf.format(new java.util.Date()));
            empProgress.setEvalateContent("--");
            empProgress.setEvaluate("--");
            
            empProgressDAO.AddEmpProgress(empProgress);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "EmpProgress添加失败!";
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

    /*查询EmpProgress信息*/
    public void ajaxQueryEmpProgress() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(addTime == null) addTime = "";
        if(rows != 0) empProgressDAO.setRows(rows);
        List<EmpProgress> empProgressList = empProgressDAO.QueryEmpProgressInfo(taskObj, studentObj, addTime, page);
        /*计算总的页数和总的记录数*/
        empProgressDAO.CalculateTotalPageAndRecordNumber(taskObj, studentObj, addTime);
        /*获取到总的页码数目*/
        totalPage = empProgressDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = empProgressDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(EmpProgress empProgress:empProgressList) {
			JSONObject jsonEmpProgress = empProgress.getJsonObject();
			jsonArray.put(jsonEmpProgress);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询EmpProgress信息*/
    public void ajaxQueryAllEmpProgress() throws IOException, JSONException {
        List<EmpProgress> empProgressList = empProgressDAO.QueryAllEmpProgressInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(EmpProgress empProgress:empProgressList) {
			JSONObject jsonEmpProgress = new JSONObject();
			jsonEmpProgress.accumulate("progressId", empProgress.getProgressId());
			jsonEmpProgress.accumulate("progressId", empProgress.getProgressId());
			jsonArray.put(jsonEmpProgress);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的EmpProgress信息*/
    public void ajaxModifyEmpProgressQuery() throws IOException, JSONException {
        /*根据主键progressId获取EmpProgress对象*/
        EmpProgress empProgress = empProgressDAO.GetEmpProgressByProgressId(progressId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonEmpProgress = empProgress.getJsonObject(); 
		out.println(jsonEmpProgress.toString()); 
		out.flush();
		out.close();
    };
    
    
   
    
    /*查询要学生是否是负责的老师班级的*/
    public void  ajaxCheckEmpDepart() throws IOException, JSONException {
    	Student studentObj = studentDAO.GetStudentByStudentNo(empProgress.getStudentObj().getStudentNo());
    	ActionContext ctx = ActionContext.getContext();
    	String chargeUserName = ctx.getSession().get("charge").toString();
    	String chargeDepartmentNo = departChargeDAO.GetDepartChargeByChargeUserName(chargeUserName).getDepartmentObj().getDepartmentNo();

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		boolean success = studentObj.getDepartmentObj().getDepartmentNo().equals(chargeDepartmentNo);
        writeJsonResponse(success, ""); 
    };

    /*更新修改EmpProgress信息*/
    public void ajaxModifyEmpProgress() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            StudentTask taskObj = studentTaskDAO.GetStudentTaskByTaskId(empProgress.getTaskObj().getTaskId());
            empProgress.setTaskObj(taskObj);
            Student studentObj = studentDAO.GetStudentByStudentNo(empProgress.getStudentObj().getStudentNo());
            empProgress.setStudentObj(studentObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String progressFileFileName = ""; 
       	 	if(progressFileFile != null) {
       	 		InputStream is = new FileInputStream(progressFileFile);
       			String extFileName = this.getProgressFileFileFileName().substring(this.getProgressFileFileFileName().lastIndexOf("."));
       	        progressFileFileName = UUID.randomUUID().toString() +  extFileName;
       			 
       			File file = new File(path, progressFileFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       			empProgress.setProgressFile("upload/" + progressFileFileName);
       	 	}
            empProgressDAO.UpdateEmpProgress(empProgress);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "EmpProgress修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除EmpProgress信息*/
    public void ajaxDeleteEmpProgress() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _progressIds[] = progressIds.split(",");
        	for(String _progressId: _progressIds) {
        		empProgressDAO.DeleteEmpProgress(Integer.parseInt(_progressId));
        	}
        	success = true;
        	message = _progressIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询EmpProgress信息*/
    public String FrontQueryEmpProgress() {
        if(page == 0) page = 1;
        if(addTime == null) addTime = "";
        List<EmpProgress> empProgressList = empProgressDAO.QueryEmpProgressInfo(taskObj, studentObj, addTime, page);
        /*计算总的页数和总的记录数*/
        empProgressDAO.CalculateTotalPageAndRecordNumber(taskObj, studentObj, addTime);
        /*获取到总的页码数目*/
        totalPage = empProgressDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = empProgressDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("empProgressList",  empProgressList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("taskObj", taskObj);
        List<StudentTask> studentTaskList = studentTaskDAO.QueryAllStudentTaskInfo();
        ctx.put("studentTaskList", studentTaskList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("addTime", addTime);
        return "front_query_view";
    }

    /*查询要修改的EmpProgress信息*/
    public String FrontShowEmpProgressQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键progressId获取EmpProgress对象*/
        EmpProgress empProgress = empProgressDAO.GetEmpProgressByProgressId(progressId);

        List<StudentTask> studentTaskList = studentTaskDAO.QueryAllStudentTaskInfo();
        ctx.put("studentTaskList", studentTaskList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("empProgress",  empProgress);
        return "front_show_view";
    }

    /*删除EmpProgress信息*/
    public String DeleteEmpProgress() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            empProgressDAO.DeleteEmpProgress(progressId);
            ctx.put("message", "EmpProgress删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "EmpProgress删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryEmpProgressOutputToExcel() { 
        if(addTime == null) addTime = "";
        List<EmpProgress> empProgressList = empProgressDAO.QueryEmpProgressInfo(taskObj,studentObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "EmpProgress信息记录"; 
        String[] headers = { "进度id","所在作业","上传进度文件","上传的学生","上传时间","评价"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<empProgressList.size();i++) {
        	EmpProgress empProgress = empProgressList.get(i); 
        	dataset.add(new String[]{empProgress.getProgressId() + "",empProgress.getTaskObj().getTitle(),
empProgress.getProgressFile(),empProgress.getStudentObj().getName(),
empProgress.getAddTime(),empProgress.getEvaluate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"EmpProgress.xls");//filename是下载的xls的名，建议最好用英文 
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
