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

<form:form action="miscellaneousRecord/brotherhood/edit.do" modelAttribute="miscellaneousRecord">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="miscellaneousRecord.title" path="title" />
	<acme:textarea code="miscellaneousRecord.text" path="text" />

	<acme:submit name="save" code="miscellaneousRecord.save"/>	

	<acme:cancel url="history/display.do?brotherhoodId=${brotherhoodId}" code="miscellaneousRecord.cancel"/>

	<jstl:if test="${miscellaneousRecord.id != 0}"> 		
		<acme:submit name="delete" code="miscellaneousRecord.delete" />
	</jstl:if>
	


</form:form>
