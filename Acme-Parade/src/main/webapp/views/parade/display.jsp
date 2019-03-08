<%--
 * action-2.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<security:authorize access="hasRole('MEMBER')">
	<jstl:if test="${memberAutorize == true}">
	<h2><a href="request/member/create.do?paradeId=${parade.id}"><spring:message code="parade.request" /></a></h2> 
	</jstl:if>
	</security:authorize>
	

	<strong><spring:message code="parade.brotherhood"/>:</strong>
		<jstl:out value="${brotherhood.title}"/>
	<br/>
	

	<strong><spring:message code="parade.title"/>:</strong>
		<jstl:out value="${parade.title}"/>
	<br/>
	
	<strong><spring:message code="parade.description"/>:</strong>
		<jstl:out value="${parade.description}"/>
	<br/>
	
		<strong> <spring:message code="parade.moment" />: </strong>
	
	<spring:message code="parade.formatMoment1" var="formatMoment"/>
		<fmt:formatDate value="${parade.moment}" pattern="${formatMoment}"/>
	
	
	<security:authorize access="hasRole('BROTHERHOOD')">		
 	<jstl:if test="${owner}">
 		<strong><spring:message code="parade.finalMode"/>:</strong>
			<jstl:out value="${parade.isFinalMode}"/>
		<br/>
 	
 	</jstl:if>
 	</security:authorize>
 	
 	
 	<br/><br/>
<jstl:if test="${ not empty floats}">	
<fieldset name="">
<strong><spring:message	code="parade.floats" /></strong>
	<display:table pagesize="5" class="displaytag" name="parade.floats" requestURI="${requestURI}" id="row">

		<display:column>	
			<a href="float/display.do?floatId=${row.id}">
				<spring:message	code="parade.display" />			
			</a>
		</display:column>
		
		<security:authorize access="hasRole('BROTHERHOOD')">		
	<jstl:if test="${principal == row.brotherhood}">	 
		<display:column >
			<a href="float/brotherhood/edit.do?floatId=${row.id}">
				<spring:message	code="parade.edit" />
			</a>
		</display:column>
	</jstl:if>
		</security:authorize>
	
		<display:column property="title" titleKey="parade.title" />	
		
		<display:column property="brotherhood.title" titleKey="parade.brotherhood" />
	
	</display:table>
	
	</fieldset>
			</jstl:if>
		<jstl:if test="${brotherhood==principal && !parade.isFinalMode }">

	<p style="color:blue;"><spring:message code="parade.info"/></p>
</jstl:if>		

	
	