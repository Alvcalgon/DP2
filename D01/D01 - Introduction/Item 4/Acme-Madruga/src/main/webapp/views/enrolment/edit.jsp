<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="enrolment/brotherhood/edit.do" modelAttribute="enrolment">

	<input type="hidden" name="isEnrolling" value="${isEnrolling}"/>	
	<form:hidden path="id"/>
	
	<div>
		<strong><spring:message code="enrolment.member"/>:</strong>
		<jstl:out value="${memberName}"/>
	</div>
	
	<div>
		<form:label path="position">
			<spring:message code="enrolment.position"/>:
		</form:label>
		<form:select path="position" multiple="false" size="1">
			<jstl:forEach var="positionId" items="${positions.keySet()}">
				<form:option label="${positions.get(positionId)}" value="${positionId}"/>
			</jstl:forEach>
		</form:select>
		<form:errors cssClass="error" path="position"/>
	</div>
	
	
	<!-- Buttons -->
	
	<div>
		<acme:submit name="save" code="enrolment.save"/>
		<jstl:if test="${isEnrolling}">
			<acme:cancel code="enrolment.cancel" url="enrolment/brotherhood/listMemberRequest.do"/>
		</jstl:if>
		<jstl:if test="${!isEnrolling}">
			<acme:cancel code="enrolment.cancel" url="enrolment/listMember.do?brotherhoodId=${enrolment.brotherhood.id}"/>
		</jstl:if>
	</div>

</form:form>