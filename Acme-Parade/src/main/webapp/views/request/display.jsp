<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


	<strong><spring:message code="request.status"/>:</strong>
		<jstl:out value="${request.status}"/>
	<br/>
	
	<jstl:if test="${request.status=='APPROVED'}">
	<strong><spring:message code="request.rowParade"/>:</strong>
		<jstl:out value="${request.rowParade}"/>
	<br/>

	<strong><spring:message code="request.columnParade"/>:</strong>
		<jstl:out value="${request.columnParade}"/>
	<br/>
	</jstl:if>
	
	<jstl:if test="${request.status=='REJECTED'}">
	<strong><spring:message code="request.reasonWhy"/>:</strong>
		<jstl:out value="${request.reasonWhy}"/>
	<br/>
	</jstl:if>
	
	<strong><spring:message code="request.member"/>:</strong>
		<jstl:out value="${request.member.name}"/>
	<br/>
	
	<strong><spring:message code="request.parade"/>:</strong>
		<jstl:out value="${request.parade.title}"/>
	<br/>
	
	<!-- Links -->	
	
	<a href="request/${rolActor}/list.do">
	<spring:message	code="request.return" />			
</a>
