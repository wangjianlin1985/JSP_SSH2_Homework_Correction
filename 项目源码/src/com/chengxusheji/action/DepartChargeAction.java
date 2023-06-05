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
import com.chengxusheji.domain.DepartCharge;
import com.chengxusheji.dao.DepartmentDAO;
import com.chengxusheji.domain.Department;
@Controller @Scope("prototype")
public class DepartChargeAction extends ActionSupport {

    /*界面层需要查询的属性: 用户名*/
    private String chargeUserName;
    public void setChargeUserName(String chargeUserName) {
        this.chargeUserName = chargeUserName;
    }
    public String getChargeUserName() {
        return this.chargeUserName;
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
    private String chargeUserNames;
    public String getChargeUserNames() {
		return chargeUserNames;
	}
	public void setChargeUserNames(String chargeUserNames) {
		this.chargeUserNames = chargeUserNames;
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
    @Resource DepartChargeDAO departChargeDAO;

    @Resource DepartmentDAO departmentDAO;
    /*待操作的DepartCharge对象*/
    private DepartCharge departCharge;
    public void setDepartCharge(DepartCharge departCharge) {
        this.departCharge = departCharge;
    }
    public DepartCharge getDepartCharge() {
        return this.departCharge;
    }

    /*ajax添加DepartCharge信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddDepartCharge() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        /*验证用户名是否已经存在*/
        String chargeUserName = departCharge.getChargeUserName();
        DepartCharge db_departCharge = departChargeDAO.GetDepartChargeByChargeUserName(chargeUserName);
        if(null != db_departCharge) {
        	message = "该用户名已经存在!";
        	writeJsonResponse(success, message);
        	return ;
        }
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(departCharge.getDepartmentObj().getDepartmentNo());
            departCharge.setDepartmentObj(departmentObj);
            departChargeDAO.AddDepartCharge(departCharge);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "DepartCharge添加失败!";
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

    /*查询DepartCharge信息*/
    public void ajaxQueryDepartCharge() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(chargeUserName == null) chargeUserName = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(rows != 0) departChargeDAO.setRows(rows);
        List<DepartCharge> departChargeList = departChargeDAO.QueryDepartChargeInfo(chargeUserName, departmentObj, name, birthday, page);
        /*计算总的页数和总的记录数*/
        departChargeDAO.CalculateTotalPageAndRecordNumber(chargeUserName, departmentObj, name, birthday);
        /*获取到总的页码数目*/
        totalPage = departChargeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departChargeDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(DepartCharge departCharge:departChargeList) {
			JSONObject jsonDepartCharge = departCharge.getJsonObject();
			jsonArray.put(jsonDepartCharge);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询DepartCharge信息*/
    public void ajaxQueryAllDepartCharge() throws IOException, JSONException {
        List<DepartCharge> departChargeList = departChargeDAO.QueryAllDepartChargeInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(DepartCharge departCharge:departChargeList) {
			JSONObject jsonDepartCharge = new JSONObject();
			jsonDepartCharge.accumulate("chargeUserName", departCharge.getChargeUserName());
			jsonDepartCharge.accumulate("name", departCharge.getName());
			jsonArray.put(jsonDepartCharge);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的DepartCharge信息*/
    public void ajaxModifyDepartChargeQuery() throws IOException, JSONException {
        /*根据主键chargeUserName获取DepartCharge对象*/
        DepartCharge departCharge = departChargeDAO.GetDepartChargeByChargeUserName(chargeUserName);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonDepartCharge = departCharge.getJsonObject(); 
		out.println(jsonDepartCharge.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改DepartCharge信息*/
    public void ajaxModifyDepartCharge() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Department departmentObj = departmentDAO.GetDepartmentByDepartmentNo(departCharge.getDepartmentObj().getDepartmentNo());
            departCharge.setDepartmentObj(departmentObj);
            departChargeDAO.UpdateDepartCharge(departCharge);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "DepartCharge修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除DepartCharge信息*/
    public void ajaxDeleteDepartCharge() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _chargeUserNames[] = chargeUserNames.split(",");
        	for(String _chargeUserName: _chargeUserNames) {
        		departChargeDAO.DeleteDepartCharge(_chargeUserName);
        	}
        	success = true;
        	message = _chargeUserNames.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询DepartCharge信息*/
    public String FrontQueryDepartCharge() {
        if(page == 0) page = 1;
        if(chargeUserName == null) chargeUserName = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        List<DepartCharge> departChargeList = departChargeDAO.QueryDepartChargeInfo(chargeUserName, departmentObj, name, birthday, page);
        /*计算总的页数和总的记录数*/
        departChargeDAO.CalculateTotalPageAndRecordNumber(chargeUserName, departmentObj, name, birthday);
        /*获取到总的页码数目*/
        totalPage = departChargeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departChargeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("departChargeList",  departChargeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("chargeUserName", chargeUserName);
        ctx.put("departmentObj", departmentObj);
        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("name", name);
        ctx.put("birthday", birthday);
        return "front_query_view";
    }

    /*查询要修改的DepartCharge信息*/
    public String FrontShowDepartChargeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键chargeUserName获取DepartCharge对象*/
        DepartCharge departCharge = departChargeDAO.GetDepartChargeByChargeUserName(chargeUserName);

        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();
        ctx.put("departmentList", departmentList);
        ctx.put("departCharge",  departCharge);
        return "front_show_view";
    }

    /*删除DepartCharge信息*/
    public String DeleteDepartCharge() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            departChargeDAO.DeleteDepartCharge(chargeUserName);
            ctx.put("message", "DepartCharge删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "DepartCharge删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryDepartChargeOutputToExcel() { 
        if(chargeUserName == null) chargeUserName = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        List<DepartCharge> departChargeList = departChargeDAO.QueryDepartChargeInfo(chargeUserName,departmentObj,name,birthday);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "DepartCharge信息记录"; 
        String[] headers = { "用户名","所在班级","姓名","性别","出生日期","邮箱地址"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<departChargeList.size();i++) {
        	DepartCharge departCharge = departChargeList.get(i); 
        	dataset.add(new String[]{departCharge.getChargeUserName(),departCharge.getDepartmentObj().getDepartmentName(),
departCharge.getName(),departCharge.getSex(),departCharge.getBirthday(),departCharge.getEmail()});
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
			response.setHeader("Content-disposition","attachment; filename="+"DepartCharge.xls");//filename是下载的xls的名，建议最好用英文 
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
