<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="area/brotherhood/edit.do" modelAttribute="area">
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	
	<input type="hidden" name="brotherhoodId" value="${brotherhoodId}"/>
	
	<acme:textbox code="area.name" path="name"/>
	<br>
	
	<acme:textbox code="area.pictures" path="pictures"/>
	<br>
	
	
	<!-- Buttons -->
	
	<acme:submit name="save" code="area.save"/>
	<acme:cancel url="/enrolment/brotherhood/listMemberRequest.do" code="area.cancel"/>
	<br />

</form:form>