<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Display any messages stored in the session --%>

<%-- Check message exists --%>
<c:if test="${not empty successMessage}">
	<div class="alert alert-success alert-dismissible fade show text-center" role="alert"> 
		<%-- Output the message --%>
		<c:out value="${successMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${not empty warningMessage}">
	<div class="alert alert-warning alert-dismissible fade show text-center" role="alert"> 
		<c:out value="${warningMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${not empty errorMessage}">
	<div class="alert alert-danger alert-dismissible fade show text-center" role="alert"> 
		<c:out value="${errorMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${not empty infoMessage}">
	<div class="alert alert-info alert-dismissible fade show text-center" role="alert"> 
		<c:out value="${infoMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${not empty progressMessage}">
	<div class="alert alert-progress alert-dismissible fade show text-center" role="alert"> 
		<c:out value="${progressMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${not empty primaryMessage}">
	<div class="alert alert-primary alert-dismissible fade show text-center" role="alert"> 
		<c:out value="${primaryMessage}" />
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>