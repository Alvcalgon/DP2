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

<form:form action="parade/brotherhood/edit.do" modelAttribute="parade">
	<form:hidden path="id" />
	
	<acme:textbox code="parade.title" path="title"/>
	
	<acme:textarea code="parade.description" path="description" />
	
	
	<jstl:if test="${parade.isFinalMode}">
	<form:label path="moment" >
		<spring:message code="parade.moment" />:
	</form:label>
	<form:input path="moment"  readonly="true" placeholder="dd/MM/yyyy hh:mm"/>
	<form:errors cssClass="error" path="moment" />
	<p/>
	</jstl:if>
	
		
	<jstl:if test="${!parade.isFinalMode}">
	<form:label path="moment" >
		<spring:message code="parade.moment" />:
	</form:label>
	<form:input path="moment"  placeholder="dd/MM/yyyy hh:mm"/>
	<form:errors cssClass="error" path="moment" />
	<p/>
	</jstl:if>
	
	
	<acme:selectMandatory items="${floats}" multiple="true" 
		 itemLabel="title" code="parade.floats" path="floats"/>
		 

	<acme:submit name="save" code="parade.save"/>	
	<jstl:if test="${parade.id != 0}">
		<acme:submit name="delete" code="parade.delete"/>	
	</jstl:if>
	<acme:cancel url="parade/list.do?brotherhoodId=${owner.id}" code="parade.cancel"/>
	<br />
</form:form>
