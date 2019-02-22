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

<display:table pagesize="5" class="displaytag" name="floats" requestURI="${requestURI}" id="row">

	<display:column>	
		<a href="float/display.do?floatId=${row.id}">
			<spring:message	code="float.display" />			
		</a>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
<%-- 	<jstl:if test="${principalId == row.brotherhood.id }">	 --%>
	<display:column >
			<a href="float/brotherhood/edit.do?floatId=${row.id}">
				<spring:message	code="float.edit" />
			</a>
		</display:column>
<%-- 	</jstl:if>  --%>
	</security:authorize>
	
	<display:column property="title" titleKey="float.title" />	
	
	<display:column property="brotherhood.title" titleKey="float.brotherhood" />

</display:table>
