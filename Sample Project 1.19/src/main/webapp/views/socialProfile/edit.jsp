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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="socialProfile/administrator,brotherhood,member/edit.do" modelAttribute="socialProfile" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="actor"/>
	
	<form:label path="nick">
		<spring:message code="socialProfile.nick"/>
	</form:label>
	<form:input path="nick"/>
	<form:errors cssClass="error" path="nick"/>
	<br />
	
	<form:label path="socialNetwork">
		<spring:message code="socialProfile.socialNetwork"/>
	</form:label>
	<form:input path="socialNetwork"/>
	<form:errors cssClass="error" path="socialNetwork"/>
	<br />
	
	<form:label path="linkProfile">
		<spring:message code="socialProfile.linkProfile"/>
	</form:label>
	<form:input path="linkProfile"/>
	<form:errors cssClass="error" path="linkProfile"/>
	<br />
	
	<!-- Buttons -->

	<acme:submit name="save" code="socialProfile.save"/>	
		
	<acme:cancel url="socialProfile/list.do?actorId=${actorId}" code="socialProfile.cancel"/>

</form:form>