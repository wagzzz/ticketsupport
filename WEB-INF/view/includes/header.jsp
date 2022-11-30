<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Get context path for request of static resources --%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>IT Services Portal</title>
	<link href="${context}/css/images/favicon.ico" rel="icon" type="image/x-icon" />
	<link href="${context}/css/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
	<%-- Import styling --%>
	<link href="${context}/css/bootstrap.min.css" rel="stylesheet">
	<link href="${context}/css/fontawesome-all.min.css" rel="stylesheet">
	<link href="${context}/css/style.css" rel="stylesheet">
</head>
<body>
	<%-- Include title and nav bar on every page --%>
	<a class="nounderline" href="ServicePortal">
		<div class="jumbotron page-title">
			<h1>IT Services Portal</h1>
		</div>
	</a>
	<%-- Import nav bar --%>
	<c:import url="/WEB-INF/view/includes/nav.jsp"/>
	<div class="container">
	