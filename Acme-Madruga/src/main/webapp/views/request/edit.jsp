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

<form:form action="request/brotherhood/edit.do" modelAttribute="request" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="status"/>
	<form:hidden path="member"/>
	<form:hidden path="procession"/>
	
	<form:label path="rowProcession">
		<spring:message code="request.rowProcession"/>
	</form:label>
	<form:input path="rowProcession"/>
	<form:errors cssClass="error" path="rowProcession"/>
	<br />
	
	<form:label path="columnProcession">
		<spring:message code="request.columnProcession"/>
	</form:label>
	<form:input path="columnProcession"/>
	<form:errors cssClass="error" path="columnProcession"/>
	<br />
	
	<form:label path="reasonWhy">
		<spring:message code="request.reasonWhy"/>
	</form:label>
	<form:input path="reasonWhy"/>
	<form:errors cssClass="error" path="reasonWhy"/>
	<br />
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="request.save"/>" />
	
	<input type="button" name="cancel" value="<spring:message code="request.cancel" />" 
			onclick="javascript: relativeRedir('socialProfile/list.do');" />

</form:form>