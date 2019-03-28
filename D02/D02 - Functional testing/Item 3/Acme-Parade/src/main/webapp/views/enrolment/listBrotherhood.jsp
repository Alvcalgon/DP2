<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table name="enrolments" id="row" requestURI="enrolment/member/listBrotherhood.do" pagesize="5" class="displaytag">

	<display:column>
		<a href="actor/display.do?actorId=${row.brotherhood.id}"><spring:message code="enrolment.display"/></a>
	</display:column>
	
	<display:column property="brotherhood.title" titleKey="enrolment.brotherhood.title" sortable="true"/>
	<display:column property="brotherhood.area.name" titleKey="enrolment.area" sortable="true"/>
	
	<display:column titleKey="brotherhood.currently.enroled" sortable="true">
		<jstl:choose>
			<jstl:when test="${row.registeredMoment ne null && row.dropOutMoment == null}">
				<spring:message code="yes"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="no"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
		
</display:table>