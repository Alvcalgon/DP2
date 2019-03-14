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


	<strong><spring:message code="legalRecord.title"/>:</strong>
		<jstl:out value="${legalRecord.title}"/>
	<br/>
	
	<strong><spring:message code="legalRecord.text"/>:</strong>
		<jstl:out value="${legalRecord.text}"/>
	<br/>
	
	<strong><spring:message code="legalRecord.name"/>:</strong>
		<jstl:out value="${legalRecord.name}"/>
	<br/>
	
	<strong><spring:message code="legalRecord.vatNumber"/>:</strong>
		<jstl:out value="${legalRecord.vatNumber}"/>
	<br/>
	
	<strong><spring:message code="legalRecord.laws"/>:</strong>
		<jstl:out value="${legalRecord.laws}"/>
	<br/>
	
	<!-- Links -->	

	<a href="history/display.do?brotherhoodId=${brotherhoodId}">
		<spring:message	code="legalRecord.back" />			
	</a>
