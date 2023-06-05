<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<title>093绩效管理系统-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<script>location.href="login.jsp";</script>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>Department/Department_FrontQueryDepartment.action" target="OfficeMain">班级</a></li> 
			<li><a href="<%=basePath %>DepartCharge/DepartCharge_FrontQueryDepartCharge.action" target="OfficeMain">老师</a></li> 
			<li><a href="<%=basePath %>Student/Student_FrontQueryStudent.action" target="OfficeMain">学生</a></li> 
			<li><a href="<%=basePath %>DepartTask/DepartTask_FrontQueryDepartTask.action" target="OfficeMain">班级作业</a></li> 
			<li><a href="<%=basePath %>Journal/Journal_FrontQueryJournal.action" target="OfficeMain">学生日志</a></li> 
			<li><a href="<%=basePath %>StudentTask/StudentTask_FrontQueryStudentTask.action" target="OfficeMain">学生作业</a></li> 
			<li><a href="<%=basePath %>EmpProgress/EmpProgress_FrontQueryEmpProgress.action" target="OfficeMain">学生进度</a></li> 
			<li><a href="<%=basePath %>News/News_FrontQueryNews.action" target="OfficeMain">新闻</a></li> 
		</ul>
		<br />
	</div> 

	<div id="loginBar">
	  <%
	  	String user_name = (String)session.getAttribute("user_name");
	    if(user_name==null){
	  %>
	  <form action="<%=basePath %>login/login_CheckFrontLogin.action" style="height:25px;margin:0px 0px 2px 0px;" method="post">
		用户名：<input type=text name="userName" size="12"/>&nbsp;&nbsp;
		密码：<input type=password name="password" size="12"/>&nbsp;&nbsp;
		<input type=submit value="登录" />
	  </form>
	  <%} else { %>
	    <ul>
	    	<li><a href="<%=basePath %>UserInfo/UserInfo_SelfModifyUserInfoQuery.action?user_name=<%=user_name %>" target="OfficeMain">【修改个人信息】</a></li>
	    	<li><a href="<%=basePath %>login/login_LoginOut.action">【退出登陆】</a></li>
	    </ul>
	 <% } %>
	</div> 

	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>&nbsp;&nbsp;<a href="<%=basePath%>login.jsp"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
