
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<form action="area/brotherhood/edit.do" method="post">

	<security:authorize access="hasRole('BROTHERHOOD')">
		
		<input type="hidden" name="brotherhoodId" value="${brotherhoodId}">
		
		<label for="areaSelectId"> <spring:message code="area.form" />
		</label>
		<select name="areaId" id="areaSelectId">
			<jstl:if test="${not empty areas}">
				<jstl:forEach var="rowArea" items="${areas}">
					<option value="${rowArea.id}">${rowArea.name}</option>
				</jstl:forEach>
			</jstl:if>
		</select>

	</security:authorize>
	
	<acme:submit name="save" code="area.save"/>
	<acme:cancel url="/enrolment/brotherhood/listMemberRequest.do" code="area.cancel"/>
	<br />
</form>


