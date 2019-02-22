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


<form:form action="float/brotherhood/edit.do" modelAttribute="paradeFloat">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="brotherhood" />

	<acme:textbox code="float.title" path="title"/>
		
	<acme:textarea code="float.description" path="description" />
	
	<acme:textarea code="float.pictures" path="pictures" />

	<acme:submit name="save" code="float.save"/>	

	<acme:cancel url="float/list.do?brotherhoodId=${paradeFloat.brotherhood.id}" code="float.cancel" />
	
<%-- 	<jstl:if test="${float.id != 0 && float.brotherhood == owner}"> --%>
	<jstl:if test="${paradeFloat.id != 0 }"> 
		<input type="submit" name="delete" value="<spring:message code="float.delete" />" />
	</jstl:if>

</form:form>
