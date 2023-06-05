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
import com.chengxusheji.dao.DepartChargeDAO;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.DepartCharge;
import com.chengxusheji.domain.Student;
import com.chengxusheji.dao.DepartmentDAO;
import com.chengxusheji.domain.Department;
@Controller @Scope("prototype")
public class StudentAction extends ActionSupport {

/*Student类字段studentPhoto参数接收*/
	 private File studentPhotoFile;
	 private String studentPhotoFileFileName;
	 private String studentPhotoFileContentType;
	 public File getStudentPhotoFile() {
		return studentPhotoFile;
	}
	public void setStudentPhotoFile(File studentPhotoFile) {
		this.studentPhotoFile = studentPhotoFile;
	}
	public String getStudentPhotoFileFileName() {
		return studentPhotoFileFileName;
	}
	public void setStudentPhotoFileFileName(String studentPhotoFileFileName) {
		this.studentPhotoFileFileName = studentPhotoFileFileName;
	}
	public String getStudentPhotoFileContentType() {
		return studentPhotoFileContentType;
	}
	public void setStudentPhotoFileContentType(String studentPhotoFileContentType) {
		this.studentPhotoFileContentType = studentPhotoFileContentType;
	}
    /*界面层需要查询的属性: 学生编号*/
    private String studentNo;
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public String getStudentNo() {
        return this.studentNo;
    }

    /*界面层需要查询的属性: 所在班级*/
    private Department departmentObj;
    public void setDepartmentObj(Department departmentObj) {
        this.departmentObj = departmentObj;
    }
    public Department getDepartmentObj() {
        return this.departmentObj;
    }

    /*界面层需要查询的属性: 姓名*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*界面层需要查询的属性: 出生日期*/
    private String birthday;
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return this.birthday;
    }

    /*界面层需要查询的属性: 联系电话*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
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

    /*要删除的记录主键集合*/
    private String studentNos;
    public String getStudentNos() {
		return studentNos;
	}
	public void setStudentNos(String studentNos) {
		this.studentNos = studentNos;
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
    @Resource StudentDAO studentDAO;

    @Resource DepartmentDAO departmentDAO;
    @Resource DepartChargeDAO departChargeDAO;
    /*待操作的Student对象*/
    private Student student;
    public void setStudent(Student student) {
        this.student = student;
    }
    public Student getStudent() {
        return this.student;
    }

    /*ajax添加Student信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddStudent() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        /*验证学生编号是否已经存在*/
        String studentNo = student.getStudentNo();
        Student db_student = studentDAO.GetStudentByStudentNo(studentNo);
        if(null != db_student) {
        	message = "该学生编号已经存在!";
        	writeJsonResponse(success, message);
        	return ;
        }
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(student.getDepartmentObj().getDepartmentNo());
            student.setDepartmentObj(departmentObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String studentPhotoFileName = ""; 
       	 	if(studentPhotoFile != null) {
       	 		InputStream is = new FileInputStream(studentPhotoFile);
       			String fileContentType = this.getStudentPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg")  || fileContentType.equals("image/pjpeg"))
       				studentPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				studentPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				message = "上传图片格式不正确!";
       				writeJsonResponse(success, message);
       				return ;
       			}
       			File file = new File(path, studentPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(studentPhotoFile != null)
            	student.setStudentPhoto("upload/" + studentPhotoFileName);
            else
            	student.setStudentPhoto("upload/NoImage.jpg");
            studentDAO.AddStudent(student);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Student添加失败!";
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

    /*查询Student信息*/
    public void ajaxQueryStudent() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        if(rows != 0) studentDAO.setRows(rows);
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo, departmentObj, name, birthday, telephone, page);
        /*计算总的页数和总的记录数*/
        studentDAO.CalculateTotalPageAndRecordNumber(studentNo, departmentObj, name, birthday, telephone);
        /*获取到总的页码数目*/
        totalPage = studentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = studentDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Student student:studentList) {
			JSONObject jsonStudent = student.getJsonObject();
			jsonArray.put(jsonStudent);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Student信息*/
    public void ajaxQueryAllStudent() throws IOException, JSONException {
        List<Student> studentList = studentDAO.QueryAllStudentInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Student student:studentList) {
			JSONObject jsonStudent = new JSONObject();
			jsonStudent.accumulate("studentNo", student.getStudentNo());
			jsonStudent.accumulate("name", student.getName());
			jsonArray.put(jsonStudent);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }
    
    /*查询Student信息*/
    public void ajaxChargeQueryAllStudent() throws IOException, JSONException {
    	DepartCharge departCharge = departChargeDAO.GetDepartChargeByChargeUserName(ActionContext.getContext().getSession().get("charge").toString());
    	String departmentNo = departCharge.getDepartmentObj().getDepartmentNo();
        List<Student> studentList = studentDAO.QueryAllStudentInfo(departmentNo);       
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Student student:studentList) {
			JSONObject jsonStudent = new JSONObject();
			jsonStudent.accumulate("studentNo", student.getStudentNo());
			jsonStudent.accumulate("name", student.getName());
			jsonArray.put(jsonStudent);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    } 

    /*查询要修改的Student信息*/
    public void ajaxModifyStudentQuery() throws IOException, JSONException {
        /*根据主键studentNo获取Student对象*/
        Student student = studentDAO.GetStudentByStudentNo(studentNo);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonStudent = student.getJsonObject(); 
		out.println(jsonStudent.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Student信息*/
    public void ajaxModifyStudent() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(student.getDepartmentObj().getDepartmentNo());
            student.setDepartmentObj(departmentObj);
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String studentPhotoFileName = ""; 
       	 	if(studentPhotoFile != null) {
       	 		InputStream is = new FileInputStream(studentPhotoFile);
       			String fileContentType = this.getStudentPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg") || fileContentType.equals("image/pjpeg"))
       				studentPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				studentPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				message = "上传图片格式不正确!";
       				writeJsonResponse(success, message);
       				return ;
       			}
       			File file = new File(path, studentPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       			student.setStudentPhoto("upload/" + studentPhotoFileName);
       	 	}
            studentDAO.UpdateStudent(student);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Student修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Student信息*/
    public void ajaxDeleteStudent() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _studentNos[] = studentNos.split(",");
        	for(String _studentNo: _studentNos) {
        		studentDAO.DeleteStudent(_studentNo);
        	}
        	success = true;
        	message = _studentNos.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Student信息*/
    public String FrontQueryStudent() {
        if(page == 0) page = 1;
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo, departmentObj, name, birthday, telephone, page);
        /*计算总的页数和总的记录数*/
        studentDAO.CalculateTotalPageAndRecordNumber(studentNo, departmentObj, name, birthday, telephone);
        /*获取到总的页码数目*/
        totalPage = studentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = studentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("studentList",  studentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("studentNo", studentNo);
        ctx.put("departmentObj", departmentObj);
        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("name", name);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        return "front_query_view";
    }

    /*查询要修改的Student信息*/
    public String FrontShowStudentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键studentNo获取Student对象*/
        Student student = studentDAO.GetStudentByStudentNo(studentNo);

        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("student",  student);
        return "front_show_view";
    }

    /*删除Student信息*/
    public String DeleteStudent() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            studentDAO.DeleteStudent(studentNo);
            ctx.put("message", "Student删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Student删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryStudentOutputToExcel() { 
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo,departmentObj,name,birthday,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Student信息记录"; 
        String[] headers = { "学生编号","所在班级","姓名","性别","出生日期","联系电话","学生照片"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<studentList.size();i++) {
        	Student student = studentList.get(i); 
        	dataset.add(new String[]{student.getStudentNo(),student.getDepartmentObj().getDepartmentName(),
student.getName(),student.getSex(),student.getBirthday(),student.getTelephone(),student.getStudentPhoto()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Student.xls");//filename是下载的xls的名，建议最好用英文 
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
