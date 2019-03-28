<%--
 * action-2.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



	<strong><spring:message code="segment.gps.origin"/>:</strong>
		<jstl:out value="${segment.origin.latitude}"/>, <jstl:out value="${segment.origin.longitude}"/>
	<br/>

	<strong><spring:message code="segment.date.origin"/>:</strong>
		<jstl:out value="${segment.reachingOrigin}"/>
	<br/>
	
	<strong><spring:message code="segment.gps.destination"/>:</strong>
		<jstl:out value="${segment.destination.latitude}"/>, <jstl:out value="${segment.destination.longitude}"/>
	<br/>

	<strong><spring:message code="segment.date.destination"/>:</strong>
		<jstl:out value="${segment.reachingDestination}"/>
	<br/>
	
	
	<a href="parade/display.do?paradeId=${paradeId}">
		<spring:message code="segment.back" />
	</a>
	
