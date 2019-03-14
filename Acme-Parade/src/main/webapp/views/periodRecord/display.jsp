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


	<strong><spring:message code="periodRecord.title"/>:</strong>
		<jstl:out value="${periodRecord.title}"/>
	<br/>
	
	<strong><spring:message code="periodRecord.text"/>:</strong>
		<jstl:out value="${periodRecord.text}"/>
	<br/>
	
	<strong><spring:message code="periodRecord.startYear"/>:</strong>
		<jstl:out value="${periodRecord.startYear}"/>
	<br/>
	
	<strong><spring:message code="periodRecord.endYear"/>:</strong>
		<jstl:out value="${periodRecord.endYear}"/>
	<br/>
	
	<jstl:if test="${not empty photos}">
		<strong><spring:message code="periodRecord.photos"/>:</strong>
		<ul>
			<jstl:forEach var="photo" items="${photos}">
				<img src="${photo}" alt="photo" height="125px" width="200px">				
			</jstl:forEach>
		</ul>
	</jstl:if>
	
	<!-- Links -->	

	<a href="history/display.do?brotherhoodId=${brotherhoodId}">
		<spring:message	code="periodRecord.back" />			
	</a>
