package com.chengxusheji.action;

 
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.chengxusheji.dao.AdminDAO;
import com.chengxusheji.dao.DepartChargeDAO;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Admin;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller @Scope("prototype")
public class LoginAction extends ActionSupport {
 

	@Resource AdminDAO adminDAO; 
	@Resource StudentDAO studentDAO;
	@Resource DepartChargeDAO departChargeDAO;

	private Admin admin;

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	private String userName;  //前台用户名
	private String password; //登录密码
	private String identify; //用户身份
	
	
	  
	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	} 
	public void setPassword(String password) {
		this.password = password;
	}

	/* 验证用户登录 */
	public String CheckFrontLogin() { 
		ActionContext ctx = ActionContext.getContext();
		/*
		if (!userInfoDAO.CheckLogin(userName,password)) {
			ctx.put("error",  java.net.URLEncoder.encode(userInfoDAO.getErrMessage()));
			return "error";
		}*/
		
		ctx.getSession().put("user_name", userName);
		return "main_user_view"; 
	}
	
	/*退出登陆*/
	public String LoginOut() {
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().remove("user_name");  
		 
		return "front_view";
	}

	
	
	/* 验证用户登录 */
	public void CheckLogin() throws IOException, JSONException { 
		ActionContext ctx = ActionContext.getContext();
		boolean success = true;
		String msg = "";
		if(identify.equals("admin")) {
			if (!adminDAO.CheckLogin(admin)) {
				msg = adminDAO.getErrMessage();
				success = false; 
			} 
			if(success) {
				ctx.getSession().put("username", admin.getUsername()); 
			} 
		} else if(identify.equals("charge")) {
			if (!departChargeDAO.CheckLogin(admin)) {
				msg = departChargeDAO.getErrMessage();
				success = false; 
			} 
			if(success) {
				ctx.getSession().put("charge", admin.getUsername()); 
				String departmentNo = departChargeDAO.GetDepartChargeByChargeUserName(admin.getUsername()).getDepartmentObj().getDepartmentNo();
				ctx.getSession().put("departmentNo",departmentNo); 
			} 
		} else if(identify.equals("student")) {
			if (!studentDAO.CheckLogin(admin)) {
				msg = studentDAO.getErrMessage();
				success = false; 
			} 
			if(success) {
				ctx.getSession().put("student", admin.getUsername()); 
				String departmentNo = studentDAO.GetStudentByStudentNo(admin.getUsername()).getDepartmentObj().getDepartmentNo();
				ctx.getSession().put("departmentNo",departmentNo); 
			} 
		}
		
		HttpServletResponse response=ServletActionContext.getResponse();   
        response.setContentType("text/json;charset=UTF-8");  
        PrintWriter out = response.getWriter();  
        //将要被返回到客户端的对象   
        JSONObject json=new JSONObject();   
        json.accumulate("success", success);
        json.accumulate("msg", msg);
        out.println(json.toString());  
//      因为JSON数据在传递过程中是以普通字符串形式传递的，所以我们也可以手动拼接符合JSON语法规范的字符串输出到客户端  
//      以下这两句的作用与38-46行代码的作用是一样的，将向客户端返回一个User对象，和一个success字段  
//      String jsonString="{\"user\":{\"id\":\"123\",\"name\":\"JSONActionGeneral\",\"say\":\"Hello , i am a action to print a json!\",\"password\":\"JSON\"},\"success\":true}";  
//      out.println(jsonString);  
        out.flush();  
        out.close();  
	}
	

}
