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

	<jstl:if test="${requestForm.status=='APPROVED'}">
		<%-- 	<form:label path="rowProcession">
			<spring:message code="request.rowProcession" />:
		</form:label>
		<form:select path="rowProcession" multiple="false" size="1">
			<jstl:forEach var="rowProcession" items="${positions.keySet()}">
				<form:option label="${positions.get(positionId)[0]}"
					value="${positionId}" />
			</jstl:forEach>
		</form:select>
		<form:errors cssClass="error" path="category" />
		<br /> --%>

		<form:label path="rowProcession">
			<spring:message code="request.rowProcession" />
		</form:label>
		<form:input path="rowProcession" />
		<form:errors cssClass="error" path="rowProcession" />
		<br />

		<form:label path="columnProcession">
			<spring:message code="request.columnProcession" />
		</form:label>
		<form:input path="columnProcession" />
		<form:errors cssClass="error" path="columnProcession" />
		<br />
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