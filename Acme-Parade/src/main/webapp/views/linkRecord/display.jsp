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


	<strong><spring:message code="linkRecord.title"/>:</strong>
		<jstl:out value="${linkRecord.title}"/>
	<br/>
	
	<strong><spring:message code="linkRecord.text"/>:</strong>
		<jstl:out value="${linkRecord.text}"/>
	<br/>
	
	<strong><spring:message code="linkRecord.brotherhood"/>:</strong>
		<a href="actor/display.do?actorId=${linkRecord.brotherhood.id}"><jstl:out value="${linkRecord.brotherhood.name}"/></a>
	<br/>
	
	<!-- Links -->	

	<a href="history/display.do?brotherhoodId=${brotherhoodId}">
		<spring:message	code="linkRecord.back" />			
	</a>
