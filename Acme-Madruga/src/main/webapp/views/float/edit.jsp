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


<form:form action="float/brotherhood/edit.do" modelAttribute="floatt">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="brotherhood" />

	<acme:textbox code="float.title" path="title"  value="${floatt.title}" placeholder=""/>
		
	<acme:textarea code="float.description" path="description" />
	
	<acme:textarea code="float.pictures" path="pictures" />

	<acme:submit name="save" code="float.save"/>	
	
	<jstl:if test="${notProcession && floatt.id!=0 && owner==floatt.brotherhood}"> 
		<input type="submit" name="delete" value="<spring:message code="float.delete" />" />
	</jstl:if>
	
		<input type="button" name="cancel"	value="<spring:message code="float.cancel"/>" onclick="javascript: relativeRedir('float/display.do?floatId=${floatt.id}');"/>

</form:form>


