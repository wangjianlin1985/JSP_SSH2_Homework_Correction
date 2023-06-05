var news_manage_tool = null; 
$(function () { 
	initNewsManageTool(); //建立News管理对象
	news_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#news_manage").datagrid({
		url : 'News/News_ajaxQueryNews.action',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "newsId",
		sortOrder : "desc",
		toolbar : "#news_manage_tool",
		columns : [[
			{
				field : "newsId",
				title : "新闻id",
				width : 70,
			},
			{
				field : "title",
				title : "新闻标题",
				width : 140,
			},
			{
				field : "publishTime",
				title : "发布时间",
				width : 140,
			},
		]],
	});

	$("#newsEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "关闭",
			iconCls : "icon-redo",
			handler : function () {
				$("#newsEditDiv").dialog("close");
				$("#newsEditForm").form("reset"); 
			},
		}],
	});
});

function initNewsManageTool() {
	news_manage_tool = {
		init: function() {
		},
		reload : function () {
			$("#news_manage").datagrid("reload");
		},
		redo : function () {
			$("#news_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#news_manage").datagrid("options").queryParams;
			queryParams["title"] = $("#title").val();
			queryParams["publishTime"] = $("#publishTime").val();
			$("#news_manage").datagrid("options").queryParams=queryParams; 
			$("#news_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#newsQueryForm").form({
			    url:"News/News_queryNewsOutputToExcel.action",
			});
			//提交表单
			$("#newsQueryForm").submit();
		},
		remove : function () {
			var rows = $("#news_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var newsIds = [];
						for (var i = 0; i < rows.length; i ++) {
							newsIds.push(rows[i].newsId);
						}
						$.ajax({
							type : "POST",
							url : "News/News_ajaxDeleteNews.action",
							data : {
								newsIds : newsIds.join(","),
							},
							beforeSend : function () {
								$("#news_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#news_manage").datagrid("loaded");
									$("#news_manage").datagrid("load");
									$("#news_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#news_manage").datagrid("loaded");
									$("#news_manage").datagrid("load");
									$("#news_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#news_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "News/News_ajaxModifyNewsQuery.action",
					type : "post",
					data : {
						newsId : rows[0].newsId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (news, response, status) {
						$.messager.progress("close");
						if (news) { 
							$("#newsEditDiv").dialog("open");
							$("#news_newsId_edit").val(news.newsId);
							$("#news_newsId_edit").validatebox({
								required : true,
								missingMessage : "请输入新闻id",
								editable: false
							});
							$("#news_title_edit").val(news.title);
							$("#news_title_edit").validatebox({
								required : true,
								missingMessage : "请输入新闻标题",
							});
							$("#news_content_edit").val(news.content);
							$("#news_content_edit").validatebox({
								required : true,
								missingMessage : "请输入新闻内容",
							});
							$("#news_publishTime_edit").val(news.publishTime);
							$("#news_publishTime_edit").validatebox({
								required : true,
								missingMessage : "请输入发布时间",
							});
						} else {
							$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
