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
	
	<br/>
	<security:authorize access="hasRole('BROTHERHOOD')">		
 	<jstl:if test="${owner}">
 		<strong><spring:message code="parade.finalMode"/>:</strong>
			<jstl:out value="${parade.isFinalMode}"/>
		<br/>
		</jstl:if>
		<jstl:if test="${parade.status!=null}">
		<strong><spring:message code="parade.status"/>:</strong>
			<jstl:out value="${parade.status}"/>
		<br/>
 	 	</jstl:if>
 	<jstl:if test="${parade.reasonWhy!=null}">
		<strong><spring:message code="parade.reasonWhy"/>:</strong>
			<jstl:out value="${parade.reasonWhy}"/>
		<br/>
 	</jstl:if>
 	</security:authorize>
 	
 	<security:authorize access="hasRole('CHAPTER')">
 	
 	<jstl:if test="${parade.reasonWhy!=null}">
		<strong><spring:message code="parade.reasonWhy"/>:</strong>
			<jstl:out value="${parade.reasonWhy}"/>
		<br/>
 	</jstl:if>
 	</security:authorize>
 	
 	<br/><br/>
<jstl:if test="${ not empty floats}">	
<fieldset>
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
			
		<br/><br/>
		
		<jstl:if test="${ not empty segments}">	
	<fieldset>
<strong><spring:message	code="parade.segments" /></strong>
	<display:table pagesize="5" class="displaytag" name="parade.segments" requestURI="${requestURI}" id="rowSegments">

		<display:column>	
			<a href="segment/display.do?segmentId=${rowSegments.id}">
				<spring:message	code="parade.display" />			
			</a>
		</display:column>
		
		<security:authorize access="hasRole('BROTHERHOOD')">		
	<jstl:if test="${principal == row.brotherhood}">	 
		<display:column >
			<a href="segment/brotherhood/edit.do?segmentId=${row.id}">
				<spring:message	code="parade.edit" />
			</a>
		</display:column>
	</jstl:if>
		</security:authorize>
	
		<display:column property="origin.latitude" titleKey="parade.segment.origin.latitude" />
		<display:column property="origin.latitude" titleKey="parade.segment.origin.longitude" />
			
		<display:column property="destination.latitude" titleKey="parade.segment.destination.latitude" />
		<display:column property="destination.longitude" titleKey="parade.segment.destination.longitude" />		
		
		<spring:message code="parade.formatMoment" var="formatMomentHeader" />
			<display:column property="reachingOrigin" titleKey="parade.segment.reachingOrigin" 	format="${formatMomentHeader}" />
		
		<spring:message code="parade.formatMoment" var="formatMomentHeader" />
			<display:column property="reachingDestination" titleKey="parade.segment.reachingDestination" 	format="${formatMomentHeader}" />

		<jstl:if test="${owner}">
 			<a href="segment/brotherhood/create.do?paradeId=${parade.id}">
		<spring:message code="parade.segment.create" />
	</a>
	

		</jstl:if>
	</display:table>
	
	</fieldset>
			</jstl:if>
			
			
		<jstl:if test="${brotherhood==principal && !parade.isFinalMode && !parade.status=='accepted' }">

	<p style="color:blue;"><spring:message code="parade.info"/></p>
	
</jstl:if>		

<jstl:if test="${sponsorship ne null}">
	<div>
		<a href="${sponsorship.targetURL}"><img src="${sponsorship.banner}" alt="Sponsorship banner" /></a>
	</div>
</jstl:if>
	