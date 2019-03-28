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


	<strong><spring:message code="area.name"/>:</strong>
		<jstl:out value="${area.name}"/>
	<br/>
	
	<jstl:if test="${not empty pictures}">
		<strong><spring:message code="area.pictures"/>:</strong>
		<br>
		<ul>
			<jstl:forEach var="picture" items="${pictures}">
				<img src="${picture}" alt="picture" height="300px" width="500px">				
			</jstl:forEach>
		</ul>
	</jstl:if>
	
	<security:authorize access="isAnonymous()">
		<p> <strong> <spring:message code="area.brotherhoods" />: </strong> </p>
		<display:table name="brotherhoods" id="row" requestURI="area/display.do?areaId=${area.id}" class="displaytag" pagesize="5">
			<display:column>
				<a href="actor/display.do?actorId=${row.id}"><spring:message code="actor.table.display.profile"/></a>
			</display:column>
			
			<display:column property="fullname" titleKey="table.fullname" />
	
			<display:column property="email" titleKey="table.email" />
			
			<display:column property="title" titleKey="table.title" />	
		</display:table>
	</security:authorize>
	
	<!-- Links -->	

<security:authorize access="hasRole('ADMIN')">
	<a href="area/administrator/list.do">
		<spring:message	code="area.back" />			
	</a>
</security:authorize>

<security:authorize access="isAnonymous()">
	<a href="chapter/list.do">
		<spring:message	code="area.back" />
	</a>
</security:authorize>
