<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="position/administrator/edit.do" modelAttribute="positionForm">
	<form:hidden path="id" />

	<acme:textbox code="position.enName" path="en_name"/>
	<acme:textbox code="position.esName" path="es_name"/>

	<acme:submit name="save" code="position.save"/>	
	<jstl:if test="${positionForm.id != 0 && canBeDeleted}"> 
		<acme:submit name="delete" code="position.delete"/>
	</jstl:if>
	<acme:cancel url="position/administrator/list.do" code="position.cancel" />
</form:form>
