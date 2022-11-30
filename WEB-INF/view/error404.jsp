<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Get context path --%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>IT Services Portal</title>
	<%-- Get css using context path --%>
	<link href="${context}/css/bootstrap.min.css" rel="stylesheet">
	<link href="${context}/css/style.css" rel="stylesheet">
</head>
<body>
	<div class="jumbotron page-title">
		<h1>IT Services Portal</h1>
	</div>

	<div class="container-fluid error-page-container">
		<canvas id="particle_canvas"></canvas>
		<div class="row error_page">
			<div class="my-5 col-md-12 text-center">
				<h1>404</h1>
				<h2>PAGE NOT FOUND</h2>
				<%-- Link to welcome page --%>
				<a href="${context}/" class="btn btn-info btn-lg">Take Me Home</a>
			</div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="${context}/js/particles.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="${context}/js/bootstrap.min.js"></script>
	<script src="${context}/js/validation.js"></script>
</body>
</html>