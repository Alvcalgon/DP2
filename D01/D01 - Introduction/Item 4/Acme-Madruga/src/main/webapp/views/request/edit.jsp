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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="request/brotherhood/edit.do"
	modelAttribute="requestForm">
	<form:hidden path="id" />
	<form:hidden path="status" />
	<form:hidden path="member" />
	<form:hidden path="procession" />
	<form:hidden path="rowProcession" />
	<form:hidden path="columnProcession" />

	<jstl:if test="${requestForm.status=='APPROVED'}">
		<form:label path="positionProcession">
			<spring:message code="request.positionProcession" />:
		</form:label>
		<form:select path="positionProcession" multiple="false" size="1">
			<jstl:forEach var="position" items="${positions.keySet()}">
				<form:option label="${positions.get(position)[0]}, ${positions.get(position)[1]}"
					value="${position}" />
			</jstl:forEach>
		</form:select>
		<form:errors cssClass="error" path="positionProcession" />

		<br />
		
		<div>
			<acme:submit name="save" code="request.save" />
			&nbsp;
			<acme:cancel code="request.cancel" url="request/brotherhood/list.do" />
		</div>
	</jstl:if>

	<jstl:if test="${requestForm.status!='APPROVED'}">

		<acme:textarea code="request.reasonWhyText" path="reasonWhy" />

		<div>
			<acme:submit name="saveReject" code="request.save" />
			&nbsp;
			<acme:cancel code="request.cancel" url="request/brotherhood/list.do" />
		</div>
	</jstl:if>
	<br />

	<!-- Buttons -->



</form:form>