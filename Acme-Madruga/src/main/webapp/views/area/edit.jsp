
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

<security:authorize access="hasRole('ADMIN')">
	<form:form action="area/administrator/edit.do"
		modelAttribute="area">
		<form:hidden path="id" />
		<form:hidden path="version" />


		<form:label path="name">
			<spring:message code="area.name" />
		</form:label>
		<form:input path="name" />
		<form:errors cssClass="error" path="name" />
		<br />

		<form:label path="pictures">
			<spring:message code="area.pictures" />
		</form:label>
		<form:input path="pictures" />
		<form:errors cssClass="error" path="pictures" />

		<br />

		<!-- Buttons -->

		<input type="submit" name="save"
			value="<spring:message code="area.save"/>" />

		<input type="button" name="cancel"
			value="<spring:message code="area.cancel" />"
			onclick="javascript: relativeRedir('area/administrator/list.do');" />
			
		<jstl:if test="${isEmpty==true}">	
		<input type="submit" name="delete"
			value="<spring:message code="area.delete" />" />
		</jstl:if>
		
	</form:form>
</security:authorize>


<security:authorize access="hasRole('BROTHERHOOD')">
	<form action="area/brotherhood/edit.do" method="post">
		<input type="hidden" name="brotherhoodId" value="${brotherhoodId}">

		<label for="areaSelectId"> <spring:message code="area.form" />
		</label> <select name="areaId" id="areaSelectId">
			<jstl:if test="${not empty areas}">
				<jstl:forEach var="rowArea" items="${areas}">
					<option value="${rowArea.id}">${rowArea.name}</option>
				</jstl:forEach>
			</jstl:if>
		</select> <br /> <br />
		<acme:submit name="save" code="area.save" />
		<acme:cancel url="/enrolment/brotherhood/listMemberRequest.do"
			code="area.cancel" />
		<br />
	</form>
</security:authorize>

