<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

<c:import url="/WEB-INF/view/includes/header.jsp"/>

<%-- Only display if there is article --%>
<c:if test="${not empty article}">

	<ul class="list-group my-2 mb-5">
		<li class="list-group-item text-dark py-3 bg-light">
			<h5 class="d-flex justify-content-between float-right">
				<%-- Display Category --%>
				<span class="mx-1 badge badge-secondary">
					<span class="mx-1 fas fa-tag"></span>
					<c:out value="${article.category.str}"/>
				</span>
			</h5>
			<div class="d-flex justify-content-between">
				<%-- Display Title --%>
				<h3 class="mb-1 text-left"><span class="mx-1 fas fa-book text-info"></span><c:out value="${article.title}"/></h3>
			</div>
			
			<div class="d-flex justify-content-between">
				<p class="mr-2 mb-0">
					Resolved
					<span class="fas fa-user-check"></span>
					<%-- Display User who resolved the ticket's name --%>
					<c:out value="${article.resolvedBy.firstName} ${article.resolvedBy.lastName}"/>
					<span class="mx-1 fas fa-calendar-alt hidden-sm"></span>
					<%-- Display date it was reported on using custom date format taglib --%>
					<customtags:date date="${article.resolvedOn}" />
				</p>
			</div>
		</li>

		<%-- Display category based issue details --%>
		<c:if test="${not empty article.issueDetails}">
			<li class="list-group-item">
				<c:forEach var="issueDetail" items="${article.issueDetails}">
					<h5 class="mb-1"><c:out value="${issueDetail.question}"/></h5>
					<p><c:out value="${issueDetail.response}"/></p>
				</c:forEach>
			</li>
		</c:if>

		<%-- Display description --%>
		<li class="list-group-item">
			<h5 class="mb-1">Description</h5>
			<pre><c:out value="${article.description}"/></pre>
		</li>

		<%-- Display resolution details --%>
		<li class="list-group-item">
			<h5 class="mb-1">Resolution Details<span class="mx-1 fas fas fa-check text-success"></span></h5>
			<pre><c:out value="${article.resolutionDetails}"/></pre>
		</li>

		<%-- Only display to staff --%>
		<c:if test="${user.role == Role.STAFF}">
			<li class="list-group-item nopadding">
				<form class="my-2 my-lg-0" method="POST" action="Ticket?ticketID=${article.ticketID}">
					<div class="text-center py-3">
						<h2 class="mb-1">Actions</h2>
						<input type="hidden" name="reportedBy" value="${supportTicket.reportedBy.userID}">
						<input type="hidden" name="action" value="removeKnowledge">
						<input type="hidden" name="redirection" value="true">
						<button class="btn btn-lg btn-danger m-1" type="submit">Remove Knowledge</button>
					</div>
				</form>
			</li>
		</c:if>
	</ul>

</c:if>

<c:import url="/WEB-INF/view/includes/footer.jsp"/>

<%
HttpServletResponse httpResponse = (HttpServletResponse)response;

httpResponse.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
httpResponse.setHeader("Pragma","no-cache"); 
httpResponse.setDateHeader ("Expires", 0); 
%>