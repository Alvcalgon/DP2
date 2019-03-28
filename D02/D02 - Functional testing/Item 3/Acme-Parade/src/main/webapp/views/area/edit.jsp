
<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


	<form:form action="area/administrator/edit.do" 	modelAttribute="area">
		<form:hidden path="id" />
		<form:hidden path="version" />
		
		<acme:textbox code="area.name" path="name"/>
	<p style="color:blue;"><spring:message code="area.info.pictures"/></p>
		<acme:textarea code="area.pictures" path="pictures" />

		<br />

		<!-- Buttons -->

		<acme:submit name="save" code="area.save"/>	
		
		<acme:cancel url="area/administrator/list.do" code="area.cancel"/>
			
		<jstl:if test="${isEmpty==true && area.id!=0}">
			<acme:submit name="delete" code="area.delete" />
		</jstl:if>
	</form:form>


