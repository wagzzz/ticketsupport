<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Remove messages from session once they have been displayed --%>
<c:remove var="successMessage"/>
<c:remove var="errorMessage"/>
<c:remove var="warningMessage"/>
<c:remove var="infoMessage"/>
<c:remove var="progressMessage"/>
<c:remove var="primaryMessage"/>
