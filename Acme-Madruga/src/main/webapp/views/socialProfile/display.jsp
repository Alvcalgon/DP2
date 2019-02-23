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


	<strong><spring:message code="socialProfile.nick"/>:</strong>
		<jstl:out value="${socialProfile.nick}"/>
	<br/>

	<strong><spring:message code="socialProfile.socialNetwork"/>:</strong>
		<jstl:out value="${socialProfile.socialNetwork}"/>
	<br/>
	
	<strong><spring:message code="socialProfile.linkProfile"/>:</strong>
		<jstl:out value="${socialProfile.linkProfile}"/>
	<br/>
	<br/>
	<!-- Links -->	
	
	<a href="socialProfile/list.do?actorId=${actorId}">
	<spring:message	code="socialProfile.return" />			
</a>
