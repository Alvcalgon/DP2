<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="proclaim/chapter/edit.do" modelAttribute="proclaim" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="publishedMoment" />
	<form:hidden path="chapter" />
	
	<acme:textarea code="proclaim.text" path="text"/>
	
	<!-- Buttons -->
	<acme:submit name="save" code="proclaim.save" onclick="return confirm('<spring:message code="proclaim.confirm.save" />')" />
	<acme:cancel code="proclaim.cancel" url="welcome/index.do" />
</form:form>