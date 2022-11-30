<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

<%-- Import headerChild jsp --%>
<c:import url="/WEB-INF/view/includes/headerChild.jsp"/>
<%-- If no suggestedArticles then add div with id of empty --%>
<c:if test="${empty suggestedArticles}">
	<div id="empty"></div>
</c:if>

<%-- Only display if their are articles --%>
<c:if test="${not empty suggestedArticles}">

	<div class="my-2">
		<h6 class="text-left">Suggested Articles</h6>
	</div>

	<ul class="list-group my-2 mb-5 ">

		<%-- Iterate through suggestedArticles --%>
		<c:forEach var="article" items="${suggestedArticles}">
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