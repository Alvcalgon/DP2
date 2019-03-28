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

<form:form action="linkRecord/brotherhood/edit.do" modelAttribute="linkRecord">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="linkRecord.title" path="title" />
	<acme:textarea code="linkRecord.text" path="text" />
	
	<acme:selectMandatory path="brotherhood" code="linkRecord.brotherhood" items="${brotherhoods}" itemLabel="name" multiple="false" />
	

	<acme:submit name="save" code="linkRecord.save"/>	

	<acme:cancel url="history/display.do?brotherhoodId=${brotherhoodId}" code="linkRecord.cancel"/>

	<jstl:if test="${linkRecord.id != 0}"> 		
		<acme:submit name="delete" code="linkRecord.delete" />
	</jstl:if>
</form:form>
