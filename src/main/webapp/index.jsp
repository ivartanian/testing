<%--
  Created by IntelliJ IDEA.
  User: super
  Date: 9/21/15
  Time: 5:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Главная</title>
</head>
<body>

<form action="/friendly-parsing/parse" method="POST">

    <label for="site_url" >Введите сайт:</label>
    <input type="text" id="site_url" name="site_url" value="http://"/>
    <br>
    <label for="max_deep" >Глубина парсинга:</label>
    <input type="number" id="max_deep" name="max_deep" value="2"/>
    <br>
    <input type="submit" value="Парсить"/>

</form>

</body>
</html>
