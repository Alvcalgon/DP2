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

<form:form action="socialProfile/administrator,customer,handyWorker,referee,sponsor/edit.do" modelAttribute="socialProfile" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="actor"/>
	
	<form:label path="nick">
		<spring:message code="socialProfile.nick"/>
	</form:label>
	<form:input path="nick"/>
	<form:errors cssClass="error" path="nick"/>
	<br />
	
	<form:label path="socialNetworkName">
		<spring:message code="socialProfile.socialNetworkName"/>
	</form:label>
	<form:input path="socialNetworkName"/>
	<form:errors cssClass="error" path="socialNetworkName"/>
	<br />
	
	<form:label path="profileLink">
		<spring:message code="socialProfile.profileLink"/>
	</form:label>
	<form:input path="profileLink"/>
	<form:errors cssClass="error" path="profileLink"/>
	<br />
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="socialProfile.save"/>" />
	
	<input type="button" name="cancel" value="<spring:message code="socialProfile.cancel" />" 
			onclick="javascript: relativeRedir('socialProfile/administrator,customer,handyWorker,referee,sponsor/list.do?actorId=${actorId}');" />

</form:form>