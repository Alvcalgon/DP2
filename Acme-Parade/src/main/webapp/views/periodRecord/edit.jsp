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

<form:form action="periodRecord/brotherhood/edit.do" modelAttribute="periodRecord">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="periodRecord.title" path="title" />
	<acme:textarea code="periodRecord.text" path="text" />
	<acme:textbox code="periodRecord.startYear" path="startYear" />
	<acme:textbox code="periodRecord.endYear" path="endYear" />
	<acme:textarea code="periodRecord.photos" path="photos" />
	

	<acme:submit name="save" code="periodRecord.save"/>	

	<acme:cancel url="history/display.do?brotherhoodId=${brotherhoodId}" code="periodRecord.cancel"/>

	<jstl:if test="${periodRecord.id != 0}"> 		
		<acme:submit name="delete" code="periodRecord.delete" />
	</jstl:if>
	


</form:form>
