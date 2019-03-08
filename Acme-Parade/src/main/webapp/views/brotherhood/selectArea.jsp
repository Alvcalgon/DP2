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


<form:form action="brotherhood/brotherhood/selectArea.do" modelAttribute="brotherhoodForm">
	 <form:label path="area">
		<spring:message code="brotherhood.areas"/>:
	</form:label>
	<form:select path="area" multiple="false" size="1">
		<form:option label="----" value="0"/>
		<form:options items="${areas}" itemLabel="name" itemValue="id"/>
	</form:select>
	<form:errors cssClass="error" path="area"/>
	<br/>
	
	<acme:submit name="save" code="area.save" />
	<acme:cancel url="/enrolment/brotherhood/listMemberRequest.do"
			code="area.cancel" />
		<br />
		

</form:form>


