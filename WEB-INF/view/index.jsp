<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Import header jsp file --%>
<c:import url="/WEB-INF/view/includes/header.jsp"/>

<%-- Login Form --%>
<div class="form-container">
	<form class="form-login" method="POST" action="Login" onSubmit="return validateLogin();">
		<label for="email" class="sr-only">Email address</label>
		<input type="email" id="email" name="email" class="form-control" placeholder="Email address" minlength="19" maxlength="19" required autofocus>
		<label for="password" class="sr-only">Password</label>
		<input type="password" id="password" name="password" class="form-control" placeholder="Password" minlength="8" required>
		<button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
	</form>
</div>

<%-- Import footer jsp file --%>
<c:import url="/WEB-INF/view/includes/footer.jsp"/>