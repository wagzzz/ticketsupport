<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

<c:import url="/WEB-INF/view/includes/header.jsp"/>

<div class="my-2">
	<h1 class="text-center"><strong>Knowledge Base</strong></h1>
</div>

<form class="form-sort" method="POST" action="KnowledgeBase">
	<div class="input-group">
		<%-- Sets options to selected if they were used to sort the tickets --%>
		<select name="categorySelect" class="custom-select">
			<option value="all" <c:if test="${param['categorySelect'] == 'all'}">selected</c:if>>All Categories</option>
			<option value="network" <c:if test="${param['categorySelect'] == 'network'}">selected</c:if>>Network</option>
			<option value="software" <c:if test="${param['categorySelect'] == 'software'}">selected</c:if>>Software</option>
			<option value="hardware" <c:if test="${param['categorySelect'] == 'hardware'}">selected</c:if>>Hardware</option>
			<option value="account" <c:if test="${param['categorySelect'] == 'account'}">selected</c:if>>Account</option>
			<option value="email" <c:if test="${param['categorySelect'] == 'email'}">selected</c:if>>Email</option>
		</select>
		<select name="orderSelect" class="custom-select">
			<option value="newest" <c:if test="${param['orderSelect'] == 'newest'}">selected</c:if>>Newest Resolved</option>
			<option value="oldest" <c:if test="${param['orderSelect'] == 'oldest'}">selected</c:if>>Oldest Resolved</option>
		</select>
		<div class="input-group-append">
			<button class="btn btn-outline-info" type="submit">Sort</button>
		</div>
	</div>
</form>

<%-- Only display if their are tickets --%>
<c:if test="${not empty knowledgeBase}">
	<ul class="list-group my-2 mb-5">

		<%-- Iterate through knowledgeBase --%>
		<c:forEach var="article" items="${knowledgeBase}">
			<li class="list-group-item list-group-item-action text-dark py-3">
				<%-- Link to Article Controller passing ticketID as a parameter --%>
				<a class="nounderline text-dark" href="Article?articleID=${article.ticketID}">
				
					<div class="d-flex justify-content-between float-right">
						<h5 class="d-flex justify-content-between float-right">
							<%-- Display category --%>
							<span class="mx-1 badge badge-secondary">
								<span class="mx-1 fas fa-tag"></span>
								<c:out value="${article.category.str}"/>
							</span>
						</h5>
					</div>
					<%-- Display title --%>
					<div class="d-flex justify-content-between">
						<h3 class="mb-1 text-left"><span class="mx-1 fas fa-book text-info"></span><c:out value="${article.title}"/></h3>
					</div>
					<%-- Display user's name and date resolved --%>
					<div class="d-flex justify-content-between">
						<p class="mr-2 mb-0">
							Resolved
							<span class="mx-1 fas fa-user-check"></span>
							<c:out value="${article.resolvedBy.firstName} ${article.resolvedBy.lastName}"/>
							<span class="mx-1 fas fa-calendar-alt"></span>
							<customtags:date date="${article.resolvedOn}" />
						</p>
					</div>
				</a>
			</li>
		</c:forEach>
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