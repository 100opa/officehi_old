<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 경고</title>
</head>
<body>
<c:url value="/main" var="mainUrl" />
<c:url value="/main" var="mainUrl" />
<script>
	var msg = "<c:out value='${msg}'/>";
	var url = "<c:out value='${mainUrl}'/>";
	alert(msg);
	location.href = url;
</script>
</body>
</html>