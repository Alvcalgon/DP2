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
	<h2><a href="request/member/create.do?processionId=${procession.id}"><spring:message code="procession.request" /></a></h2> 
	</jstl:if>
	</security:authorize>
	

	<strong><spring:message code="procession.brotherhood"/>:</strong>
		<jstl:out value="${brotherhood.title}"/>
	<br/>
	

	<strong><spring:message code="procession.title"/>:</strong>
		<jstl:out value="${procession.title}"/>
	<br/>
	
	<strong><spring:message code="procession.description"/>:</strong>
		<jstl:out value="${procession.description}"/>
	<br/>
	
		<strong> <spring:message code="procession.moment" />: </strong>
	
	<spring:message code="procession.formatMoment1" var="formatMoment"/>
		<fmt:formatDate value="${procession.moment}" pattern="${formatMoment}"/>
	
	
	<security:authorize access="hasRole('BROTHERHOOD')">		
 	<jstl:if test="${owner}">
 		<strong><spring:message code="procession.finalMode"/>:</strong>
			<jstl:out value="${procession.isFinalMode}"/>
		<br/>
 	
 	</jstl:if>
 	</security:authorize>
 	
 	
 	<br/><br/>
<jstl:if test="${ not empty floats}">	
<fieldset name="">
<strong><spring:message	code="procession.floats" /></strong>
	<display:table pagesize="5" class="displaytag" name="procession.floats" requestURI="${requestURI}" id="row">

		<display:column>	
			<a href="float/display.do?floatId=${row.id}">
				<spring:message	code="procession.display" />			
			</a>
		</display:column>
		
		<security:authorize access="hasRole('BROTHERHOOD')">		
	<jstl:if test="${principal == row.brotherhood}">	 
		<display:column >
			<a href="float/brotherhood/edit.do?floatId=${row.id}">
				<spring:message	code="procession.edit" />
			</a>
		</display:column>
	</jstl:if>
		</security:authorize>
	
		<display:column property="title" titleKey="procession.title" />	
		
		<display:column property="brotherhood.title" titleKey="procession.brotherhood" />
	
	</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">		
	<jstl:if test="${principal == row.brotherhood}">	 
	<a href="float/brotherhood/create.do"><spring:message code="float.create"/></a>
	</jstl:if>
		</security:authorize>
	
	</fieldset>
			</jstl:if>
		<jstl:if test="${brotherhood==principal && !procession.isFinalMode }">

	<p style="color:blue;"><spring:message code="procession.info"/></p>
</jstl:if>		
			
	<a href="procession/list.do?brotherhoodId=${brotherhood.id}">
		<spring:message	code="procession.back" />			
	</a>
	
	