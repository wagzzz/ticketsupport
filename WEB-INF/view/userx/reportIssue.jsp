<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/includes/header.jsp"/>

<div class="text-center">
	<h1 class="text-center"><strong>Report Issue</strong></h1>
</div>

<form class="form-report" method="POST" action="Report">
	<div class="form-group">
		<label for="title">Title*</label>
		<%-- Load suggested articles into iFrame when user types into title --%>
		<input name="title" minlength="3" maxlength="200" type="text" class="form-control" id="title" placeholder="Title" required onkeyup="suggestArticles('${pageContext.request.contextPath}');">
  		<iframe class="container-fluid m-1" id="suggested-articles"></iframe>
	</div>

	<div class="form-group">
		<label>Category*</label>
		<select name="category" id="categorySelect" class="form-control" required>
			<option value="">Choose</option>
			<option value="network">Network</option>
			<option value="software">Software</option>
			<option value="hardware">Hardware</option>
			<option value="email">Email</option>
			<option value="account">Account</option>
		</select>
	</div>

	<%-- Get category based input fields --%>
	<c:import url="/WEB-INF/view/includes/network.jsp"/>
	<c:import url="/WEB-INF/view/includes/software.jsp"/>
	<c:import url="/WEB-INF/view/includes/hardware.jsp"/>
	<c:import url="/WEB-INF/view/includes/email.jsp"/>
	<c:import url="/WEB-INF/view/includes/account.jsp"/>

	<div class="form-group">
		<label>Problem Details*</label>
		<textarea name="description" minlength="3" maxlength="20000" class="form-control" id="details" rows="3" placeholder="Details..." required></textarea>
	</div>

	<button class="btn btn-lg btn-danger btn-block" type="submit">Report</button>
</form>

<c:import url="/WEB-INF/view/includes/footer.jsp"/>

<%
HttpServletResponse httpResponse = (HttpServletResponse)response;

httpResponse.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
httpResponse.setHeader("Pragma","no-cache"); 
httpResponse.setDateHeader ("Expires", 0); 
%>