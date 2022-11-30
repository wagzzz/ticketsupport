<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Header file for iFrame pages --%>

<%-- Get context path for request of static resources --%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
	<%-- Make child document affect parent --%>
	<base target="_parent">
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>IT Services Portal</title>
	<link href="${context}/css/bootstrap.min.css" rel="stylesheet">
	<link href="${context}/css/fontawesome-all.min.css" rel="stylesheet">
	<link href="${context}/css/style.css" rel="stylesheet">
</head>
<body>
	<%-- No title or nav bar --%>
	<div class="container-fluid">
	