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
	<strong><spring:message code="request.rowProcession"/>:</strong>
		<jstl:out value="${request.rowProcession}"/>
	<br/>

	<strong><spring:message code="request.columnProcession"/>:</strong>
		<jstl:out value="${request.columnProcession}"/>
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
	
	<strong><spring:message code="request.procession"/>:</strong>
		<jstl:out value="${request.procession.title}"/>
	<br/>
	
	<!-- Links -->	
	
	<a href="request/${rolActor}/list.do">
	<spring:message	code="request.return" />			
</a>
