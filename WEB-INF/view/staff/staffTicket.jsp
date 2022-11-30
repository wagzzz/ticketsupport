<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>

<c:import url="/WEB-INF/view/includes/header.jsp"/>

<%-- Only display if there is ticket --%>
<c:if test="${not empty supportTicket}">

	<ul class="list-group my-2">
		<li class="list-group-item text-dark py-3">
			<div class="d-flex justify-content-between">
				<%-- Display TicketID --%>
				<h5>Ticket <c:out value="${supportTicket.ticketID}"/></h5>
				<h5 class="d-flex justify-content-between">

					<%-- Add class based on ticket state to add different colours --%>
					<c:choose>
						<c:when test="${supportTicket.state == State.NEW}">
							<c:set var="stateClass" value="badge-danger"/>
						</c:when>
						<c:when test="${supportTicket.state == State.INPROGRESS}">
							<c:set var="stateClass" value="badge-progress"/>
						</c:when>
						<c:when test="${supportTicket.state == State.COMPLETED}">
							<c:set var="stateClass" value="badge-primary"/>
						</c:when>
						<c:when test="${supportTicket.state == State.RESOLVED}">
							<c:set var="stateClass" value="badge-success"/>
						</c:when>
					</c:choose>

					<%-- Display State --%>
					<span class="state mx-1 badge ${stateClass}">
						<span class="mx-1 fas fa-tasks"></span>
						<c:out value="${supportTicket.state.str}"/>
					</span>
					<%-- Display Category --%>
					<span class="mx-1 badge badge-secondary">
						<span class="mx-1 fas fa-tag"></span>
						<c:out value="${supportTicket.category.str}"/>
					</span>
				</h5>
			</div>

			<%-- Display Title --%>
			<h3 class="mb-1 text-left"><c:out value="${supportTicket.title}"/></h3>
			<div class="d-flex justify-content-between">
				<p class="mr-2 mb-0">
					Reported
					<span class=" fas fa-user"></span>
					<%-- Display User who reported the ticket's name --%>
					<c:out value="${supportTicket.reportedBy.firstName} ${supportTicket.reportedBy.lastName}"/>
					<span class="mx-1 fas fa-calendar-alt"></span>
					<%-- Display date it was reported on using custom date format taglib --%>
					<customtags:date date="${supportTicket.reportedOn}" />
				</p>
				<c:if test="${not empty supportTicket.resolvedOn}">
					<p class="mr-2 mb-0">
						Resolved
						<span class=" fas fa-user-check"></span>
						<%-- Display User who resolved the ticket's name --%>
						<c:out value="${supportTicket.resolvedBy.firstName} ${supportTicket.resolvedBy.lastName}"/>
						<span class="mx-1 fas fa-calendar-alt"></span>
						<%-- Display date it was reported on using custom date format taglib --%>
						<customtags:date date="${supportTicket.resolvedOn}" />
					</p>
				</c:if>
			</div>
		</li>

		<%-- Display category based issue details --%>
		<c:if test="${not empty supportTicket.issueDetails}">
			<li class="list-group-item">
				<c:forEach var="issueDetail" items="${supportTicket.issueDetails}">
					<h5 class="mb-1"><c:out value="${issueDetail.question}"/></h5>
					<p><c:out value="${issueDetail.response}"/></p>
				</c:forEach>
			</li>
		</c:if>

		<%-- Display description --%>
		<li class="list-group-item">
			<h5 class="mb-1">Description</h5>
			<pre><c:out value="${supportTicket.description}"/></pre>
		</li>

		<li class="list-group-item nopadding">
			<%-- Resolution or Solution text box and offer staff actions to modify the ticket --%>
			<form method="POST" action="Ticket?ticketID=${supportTicket.ticketID}">
				<c:if test="${not empty supportTicket.resolutionDetails}">
					<div class="list-group-item">
						<h5 class="mb-1">Resolution Details</h5>
						<pre><c:out value="${supportTicket.resolutionDetails}"/></pre>
					</div>
				</c:if>
				<c:if test="${empty supportTicket.resolutionDetails && supportTicket.state == State.INPROGRESS}">
					<div class="list-group-item">
						<div class="form-group">
							<label class="h3"><span class="mx-1 far fa-edit"></span>Solution</label>
							<textarea minlength="3" maxlength="20000" name="solution" class="form-control" id="soltuion" rows="3" placeholder="Solution..." required></textarea>
						</div>
					</div>
				</c:if>
				<div class="list-group-item text-dark py-3">
					<div class="text-center">
						<h2 class="mb-1">Actions</h2>

						<%-- Decide which actions should be available to staff --%>
						<input type="hidden" name="reportedBy" value="${supportTicket.reportedBy.userID}">
						
						<c:if test="${supportTicket.state == State.NEW}">
							<input type="hidden" name="action" value="startWork">
							<button class="btn btn-lg btn-progress m-1" type="submit">Start Work</button>
						</c:if>

						<c:if test="${supportTicket.state == State.INPROGRESS}">
							<input type="hidden" name="action" value="submitSolution">
							<button class="btn btn-lg btn-primary m-1" type="submit">Submit Solution</button>
						</c:if>

						<c:if test="${(supportTicket.state == State.COMPLETED || supportTicket.state == State.RESOLVED) && not supportTicket.knowledgeBase}">
							<input type="hidden" name="action" value="addKnowledge">
							<button class="btn btn-lg btn-success m-1" type="submit">Add Knowledge</button>
						</c:if>

						<c:if test="${supportTicket.knowledgeBase}">
							<input type="hidden" name="action" value="removeKnowledge">
							<button class="btn btn-lg btn-danger m-1" type="submit">Remove Knowledge</button>
						</c:if>
					</div>
				</div>
			</form>
		</li>
	</ul>

	<h3 class="text-center m-3">Discussion</h3>
	<ul class="list-group my-2 mb-5">
		<%-- Iterate through comments list --%>
		<c:forEach var="comment" items="${supportTicket.comments}">
			<%-- Use Include directive for comment jsp so it can use the jstl c:forEach variable --%>
			<%@ include file="/WEB-INF/view/includes/comment.jsp" %>
		</c:forEach>

		<%-- Allow commenting unless resolved --%>
		<c:if test="${supportTicket.state != State.RESOLVED}">
			<li class="list-group-item">
				<form class="my-2 my-lg-0" method="POST" action="Ticket?ticketID=${supportTicket.ticketID}">
					<div class="form-group">
						<label class="h5">Comment<span class="mx-1 far fa-comment"></span></label>
						<textarea minlength="3" maxlength="20000" name="commentText" class="form-control" id="exampleFormControlTextarea1" rows="3" placeholder="Comment text..." required></textarea>
					</div>
					<input type="hidden" name="action" value="comment">
					<input type="hidden" name="reportedBy" value="${supportTicket.reportedBy.userID}">
					<input type="hidden" name="reportedBy" value="${supportTicket.resolvedBy.userID}">
					<button class="btn btn-success my-2 my-sm-0 m-2 float-right" type="submit">Post</button>
				</form>
			</li>
		</c:if>
	</ul>

</c:if>

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