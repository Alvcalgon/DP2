<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="inceptionRecord/brotherhood/edit.do" modelAttribute="inceptionRecord">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="inceptionRecord.title" path="title" />
	<acme:textarea code="inceptionRecord.text" path="text" />
	<acme:textarea code="inceptionRecord.photos" path="photos" />

	<acme:submit name="save" code="inceptionRecord.save"/>
	
	<jstl:if test="${existHistory}">
		<acme:cancel url="history/display.do?brotherhoodId=${brotherhoodId}" code="inceptionRecord.cancel"/>
	</jstl:if>
	
	<jstl:if test="${!existHistory}">
		<acme:cancel url="actor/display.do" code="inceptionRecord.cancel"/>
	</jstl:if>
	<br />
</form:form>
