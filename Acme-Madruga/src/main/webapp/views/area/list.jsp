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

<display:table name="areas" id="row"
	requestURI="${requestURI }" class="displaytag" pagesize="5">

	<display:column>
		<a href="request/brotherhood,member/display.do?requestId=${row.id}"><spring:message
				code="request.display" /></a>
	</display:column>

	<security:authorize access="hasRole('MEMBER')">
		<display:column>
			<a href="request/member/delete.do?requestId=${row.id}"><spring:message
					code="request.cancel" /></a>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="request/brotherhood/reject.do?requestId=${row.id}"><spring:message
					code="request.reject" /></a>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="request/brotherhood/accept.do?requestId=${row.id}"><spring:message
					code="request.accept" /></a>
		</display:column>
	</security:authorize>

	<display:column	property="procession.title" titleKey="request.procession" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column	property="member.name" titleKey="request.member" />
	</security:authorize>

</display:table>

