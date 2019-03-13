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

<form:form action="segment/brotherhood/edit.do" modelAttribute="segmentForm">
	<form:hidden path="paradeId"/>
	<form:hidden path="id"/>	
	
	<acme:textbox code="segment.date.origin" path="reachingOrigin" placeholder="dd/MM/yyyy hh:mm"/><br/>
	
	<acme:textbox code="segment.latitude.origin" path="originLatitude"/><br/>
	
	<acme:textbox code="segment.longitude.origin" path="originLongitude" placeholder="dd/MM/yyyy hh:mm"/><br/>
	
	<acme:textbox code="segment.date.destination" path="reachingDestination"/><br/>
	
	<acme:textbox code="segment.latitude.destination" path="destinationLatitude"/><br/>
	
	<acme:textbox code="segment.longitude.destination" path="destinationLongitude"/><br/>
	
	
	<acme:submit name="save" code="segment.save"/>	
	
	<jstl:if test="${segmentForm.id != 0}">
		<acme:submit name="delete" code="segment.delete"/>	
	</jstl:if>
	<acme:cancel url="parade/display.do?paradeId=${paradeId}" code="segment.cancel"/>
	<br />
</form:form>
