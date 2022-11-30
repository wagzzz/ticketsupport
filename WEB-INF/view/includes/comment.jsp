<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

 <%-- Check if comment exists --%>
<c:if test="${not empty comment}">
	<li class="list-group-item">
		<div class="d-flex justify-content-between">
			<h6>
				<%-- Output if comment was created by Staff Role --%>
				<c:if test="${comment.createdBy.role == Role.STAFF}">
					<strong class="mr-1 text-primary">IT STAFF</strong>
				</c:if>
				<%-- Output comment author's name --%>
				<c:out value="${comment.createdBy.firstName} ${comment.createdBy.lastName}"/>
			</h6>
			<%-- Custom JSP tag to output comment date --%>
			<small class="text-muted"><customtags:date date="${comment.createdOn}" /></small>
		</div>
		<%-- Output comment text --%>
		<pre><c:out value="${comment.commentText}"/></pre>
	</li>
</c:if> 