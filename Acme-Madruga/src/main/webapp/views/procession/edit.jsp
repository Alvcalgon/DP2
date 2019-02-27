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

<form:form action="procession/brotherhood/edit.do" modelAttribute="procession">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isFinalMode"/>
	<form:hidden path = "ticker"/>
	
	
	<acme:textbox code="procession.title" path="title"/>
	
	<acme:textarea code="procession.description" path="description" />
	
	
	<jstl:if test="${procession.isFinalMode}">
	<form:label path="moment" >
		<spring:message code="procession.moment" />:
	</form:label>
	<form:input path="moment"  readonly="true" placeholder="dd/MM/yyyy hh:mm"/>
	<form:errors cssClass="error" path="moment" />
	<p/>
	</jstl:if>
	
		
	<jstl:if test="${!procession.isFinalMode}">
	<form:label path="moment" >
		<spring:message code="procession.moment" />:
	</form:label>
	<form:input path="moment"  placeholder="dd/MM/yyyy hh:mm"/>
	<form:errors cssClass="error" path="moment" />
	<p/>
	</jstl:if>
	
	<form:label path="floats">
		<spring:message code="procession.floats"/>:
	</form:label>
	<form:select path="floats" multiple="true" size="5">
		<form:options items="${floats}" itemLabel="title" itemValue="id"/>
	</form:select>
	<form:errors cssClass="error" path="floats"/>
	<br/>
	

	<input type="submit" name="save" value="<spring:message code="procession.save" />" />
	<jstl:if test="${procession.id != 0}">
		<input type="submit" name="delete" value="<spring:message code="procession.delete" />" onclick="return confirm('<spring:message code="procession.confirm.delete" />')" />
	</jstl:if>
	<input type="button" name="cancel"	value="<spring:message code="procession.cancel"/>" onclick="javascript: relativeRedir('procession/list.do?brotherhoodId=${owner.id}');" />
	<br />
</form:form>
