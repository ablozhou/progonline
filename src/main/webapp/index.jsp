<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>">
    <meta author="zhh"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>在线编程</title>
    <!--[if lt IE 9]>
    <script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
    <![endif]-->
    </head>
<body style="margin:0 auto; width:960px; padding:10px;text-align:center">

<h1>在线编程</h1>
<div>仅支持Java 语言Test类, Linux 或 Mac操作系统下的命令和Java编程. Windows系统没有测试</div>
<div id="result" style="margin:10px;padding:10px;text-align:center">
  
<textarea id="cmd" name="cmd" type="textarea" cols=100 rows=10 placeholder=""  
style="width:100%; padding:10px">public class Test{

    public static void main(String[] args) {

        System.out.println("你好, 中国！");
    }

}</textarea>

<input id="submitBtn" type="submit" style="height:30px;width:50px;margin:10px;font-size=20px" value="命令">
<input id="programBtn" type="submit" style="height:30px;width:50px;margin:10px;font-size=20px" value="编程">

<textarea id="output" name="output" type="textarea" cols=100 rows=10 style="width:100%;"></textarea> 
</div>
<div >Copyright © 2016 ZHH abloz.com
</div>
<script src="js/jquery-1.12.0.min.js"></script>
<script>
  
    $('#submitBtn').click(function (e) {
        window.localStorage.clear();
        e.preventDefault();
        $.ajax({
            url: 'cmd',
            type: 'POST',
            data: $('#cmd').serialize(),
            success: function (res) {               
                $('#output').val(res.replace(/\\n/g,"\n"));
            },
            error: function(err) {
                alert("error: "+err);
            }
        });
    });
    $('#programBtn').click(function (e) {
        window.localStorage.clear();
        e.preventDefault();
        $.ajax({
            url: 'program',
            type: 'POST',
            data: $('#cmd').serialize(),
            success: function (res) {               
                $('#output').val(res.replace(/\\n/g,"\n"));
            },
            error: function(err) {
                alert("error: "+err);
            }
        });
    });
</script>
<SCRIPT>
    if (!$.support.leadingWhitespace) {
        alert("浏览器版本太低，请下载chrome或者IE10以上版本浏览器")
    }


</SCRIPT>
</body>
</html>