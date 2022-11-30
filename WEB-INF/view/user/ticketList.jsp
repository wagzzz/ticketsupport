<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

<%-- Import header jsp --%>
<c:import url="/WEB-INF/view/includes/header.jsp"/>

<div class="my-2">
	<h1 class="text-center"><strong>Support Tickets</strong></h1>
</div>

<form class="form-sort" method="POST" action="TicketList">
	<div class="input-group">
		<%-- Sets option to selected if it was used to sort the tickets. Defaults to first option if wasn't sorted --%>
		<select name="categorySelect" class="custom-select">
			<option value="all" <c:if test="${param['categorySelect'] == 'all'}">selected</c:if>>All Categories</option>
			<option value="network" <c:if test="${param['categorySelect'] == 'network'}">selected</c:if>>Network</option>
			<option value="software" <c:if test="${param['categorySelect'] == 'software'}">selected</c:if>>Software</option>
			<option value="hardware" <c:if test="${param['categorySelect'] == 'hardware'}">selected</c:if>>Hardware</option>
			<option value="account" <c:if test="${param['categorySelect'] == 'account'}">selected</c:if>>Account</option>
			<option value="email" <c:if test="${param['categorySelect'] == 'email'}">selected</c:if>>Email</option>
		</select>
		<select name="stateSelect" class="custom-select">
			<option value="all" <c:if test="${param['stateSelect'] == 'all'}">selected</c:if>>All States</option>
			<option value="new" <c:if test="${param['stateSelect'] == 'new'}">selected</c:if>>New</option>
			<option value="in progress" <c:if test="${param['stateSelect'] == 'in progress'}">selected</c:if>>In Progress</option>
			<option value="completed" <c:if test="${param['stateSelect'] == 'completed'}">selected</c:if>>Completed</option>
			<option value="resolved" <c:if test="${param['stateSelect'] == 'resolved'}">selected</c:if>>Resolved</option>
		</select>
		<select name="orderSelect" class="custom-select">
			<option value="newest" <c:if test="${param['orderSelect'] == 'newest'}">selected</c:if>>Newest Reported</option>
			<option value="oldest" <c:if test="${param['orderSelect'] == 'oldest'}">selected</c:if>>Oldest Reported</option>
		</select>
		<div class="input-group-append">
			<button class="btn btn-outline-info" type="submit">Sort</button>
		</div>
	</div>
</form>

<%-- Only display if their are tickets --%>
<c:if test="${not empty tickets}">
	<ul class="list-group my-2 mb-5">

		<%-- Iterate through tickets list --%>
		<c:forEach var="ticket" items="${tickets}">
		
			<li class="list-group-item list-group-item-action text-dark py-3">
				<%-- Link to Ticket Controller passing ticketID as a parameter --%>
				<a class="nounderline text-dark" href="Ticket?ticketID=${ticket.ticketID}">
					<div class="d-flex justify-content-between">
						<%-- Display TicketID --%>
						<h5>Ticket <c:out value="${ticket.ticketID}"/></h5>
						<h5 class="d-flex justify-content-between">

							<%-- Add class based on ticket state to add different colours --%>
							<c:set var="state" value="${ticket.state}"/>
							<c:choose>
								<c:when test="${ticket.state == State.NEW}">
									<c:set var="stateClass" value="badge-danger"/>
								</c:when>
								<c:when test="${ticket.state == State.INPROGRESS}">
									<c:set var="stateClass" value="badge-progress"/>
								</c:when>
								<c:when test="${ticket.state == State.COMPLETED}">
									<c:set var="stateClass" value="badge-primary"/>
								</c:when>
								<c:when test="${ticket.state == State.RESOLVED}">
									<c:set var="stateClass" value="badge-success"/>
								</c:when>
							</c:choose>
							<%-- Display state --%>
							<span class="state mx-1 badge ${stateClass}">
								<span class="mx-1 fas fa-tasks"></span>
								<c:out value="${ticket.state.str}"/>
							</span>
							<%-- Display category --%>
							<span class="mx-1 badge badge-secondary">
								<span class="mx-1 fas fa-tag"></span>
								<c:out value="${ticket.category.str}"/>
							</span>
						</h5>
					</div>
					<%-- Display title --%>
					<h3 class="mb-1 text-left"><c:out value="${ticket.title}"/></h3>
					<div class="d-flex justify-content-between">
						<p class="mr-2 mb-0">
							Reported
							<span class=" fas fa-user"></span>
							<%-- Display user's name and date reported --%>
							<c:out value="${ticket.reportedBy.firstName} ${ticket.reportedBy.lastName}"/>
							<span class="mx-1 fas fa-calendar-alt"></span>
							<customtags:date date="${ticket.reportedOn}"/>
						</p>
						<%-- Display user's name and date resolved if ticket has a resolvedOn date --%>
						<c:if test="${not empty ticket.resolvedOn}">
							<p class="mr-2 mb-0">
								Resolved
								<span class=" fas fa-user-check"></span>
								<c:out value="${ticket.resolvedBy.firstName} ${ticket.resolvedBy.lastName}"/>
								<span class="mx-1 fas fa-calendar-alt"></span>
								<customtags:date date="${ticket.resolvedOn}" />
							</p>
						</c:if>
					</div>
				</a>
			</li>
		</c:forEach>
	</ul>
</c:if>

<%-- Import footer jsp --%>
<c:import url="/WEB-INF/view/includes/footer.jsp"/>

<%-- Prevent caching to prevent issue like a user logging out then pressing back
	 still being able to see a restricted page --%>

<%
HttpServletResponse httpResponse = (HttpServletResponse)response;

httpResponse.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
httpResponse.setHeader("Pragma","no-cache"); 
httpResponse.setDateHeader ("Expires", 0); 
%>