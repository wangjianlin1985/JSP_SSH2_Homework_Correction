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
import com.chengxusheji.dao.DepartmentDAO;
import com.chengxusheji.domain.Department;
@Controller @Scope("prototype")
public class DepartmentAction extends ActionSupport {

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

    private String departmentNo;
    public void setDepartmentNo(String departmentNo) {
        this.departmentNo = departmentNo;
    }
    public String getDepartmentNo() {
        return departmentNo;
    }

    /*要删除的记录主键集合*/
    private String departmentNos;
    public String getDepartmentNos() {
		return departmentNos;
	}
	public void setDepartmentNos(String departmentNos) {
		this.departmentNos = departmentNos;
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
    @Resource DepartmentDAO departmentDAO;

    /*待操作的Department对象*/
    private Department department;
    public void setDepartment(Department department) {
        this.department = department;
    }
    public Department getDepartment() {
        return this.department;
    }

    /*ajax添加Department信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddDepartment() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        /*验证班级编号是否已经存在*/
        String departmentNo = department.getDepartmentNo();
        Department db_department = departmentDAO.GetDepartmentByDepartmentNo(departmentNo);
        if(null != db_department) {
        	message = "该班级编号已经存在!";
        	writeJsonResponse(success, message);
        	return ;
        }
        try {
            departmentDAO.AddDepartment(department);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Department添加失败!";
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

    /*查询Department信息*/
    public void ajaxQueryDepartment() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(rows != 0) departmentDAO.setRows(rows);
        List<Department> departmentList = departmentDAO.QueryDepartmentInfo(page);
        /*计算总的页数和总的记录数*/
        departmentDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = departmentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departmentDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Department department:departmentList) {
			JSONObject jsonDepartment = department.getJsonObject();
			jsonArray.put(jsonDepartment);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Department信息*/
    public void ajaxQueryAllDepartment() throws IOException, JSONException {
        List<Department> departmentList = departmentDAO.QueryAllDepartmentInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Department department:departmentList) {
			JSONObject jsonDepartment = new JSONObject();
			jsonDepartment.accumulate("departmentNo", department.getDepartmentNo());
			jsonDepartment.accumulate("departmentName", department.getDepartmentName());
			jsonArray.put(jsonDepartment);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Department信息*/
    public void ajaxModifyDepartmentQuery() throws IOException, JSONException {
        /*根据主键departmentNo获取Department对象*/
        Department department = departmentDAO.GetDepartmentByDepartmentNo(departmentNo);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonDepartment = department.getJsonObject(); 
		out.println(jsonDepartment.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Department信息*/
    public void ajaxModifyDepartment() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            departmentDAO.UpdateDepartment(department);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Department修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Department信息*/
    public void ajaxDeleteDepartment() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _departmentNos[] = departmentNos.split(",");
        	for(String _departmentNo: _departmentNos) {
        		departmentDAO.DeleteDepartment(_departmentNo);
        	}
        	success = true;
        	message = _departmentNos.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Department信息*/
    public String FrontQueryDepartment() {
        if(page == 0) page = 1;
        List<Department> departmentList = departmentDAO.QueryDepartmentInfo(page);
        /*计算总的页数和总的记录数*/
        departmentDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = departmentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = departmentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("departmentList",  departmentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        return "front_query_view";
    }

    /*查询要修改的Department信息*/
    public String FrontShowDepartmentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键departmentNo获取Department对象*/
        Department department = departmentDAO.GetDepartmentByDepartmentNo(departmentNo);

        ctx.put("department",  department);
        return "front_show_view";
    }

    /*删除Department信息*/
    public String DeleteDepartment() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            departmentDAO.DeleteDepartment(departmentNo);
            ctx.put("message", "Department删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Department删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryDepartmentOutputToExcel() { 
        List<Department> departmentList = departmentDAO.QueryDepartmentInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Department信息记录"; 
        String[] headers = { "班级编号","班级名称","成立日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<departmentList.size();i++) {
        	Department department = departmentList.get(i); 
        	dataset.add(new String[]{department.getDepartmentNo(),department.getDepartmentName(),department.getBornDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Department.xls");//filename是下载的xls的名，建议最好用英文 
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
