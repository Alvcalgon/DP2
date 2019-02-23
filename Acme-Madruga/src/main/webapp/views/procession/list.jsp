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


<display:table pagesize="5" class="displaytag" name="processions" requestURI="${requestURI}" id="row">

	<display:column>	
		<a href="procession/display.do?processionId=${row.id}">
			<spring:message	code="procession.display" />			
		</a>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">		
 	<jstl:if test="${isOwner}">	 
	<display:column >
			<a href="procession/brotherhood/edit.do?processionId=${row.id}">
				<spring:message	code="procession.edit" />
			</a>
		</display:column>
		<display:column property="isFinalMode" titleKey="procession.finalMode" />	
		
		</jstl:if>
	</security:authorize>
	
	<display:column property="title" titleKey="procession.title" />	
	
	<spring:message code="procession.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="procession.moment" format="${formatMomentHeader}" />
	

</display:table>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<a href="procession/brotherhood/create.do"><spring:message code="procession.create"/></a>
	</security:authorize>
	
	
