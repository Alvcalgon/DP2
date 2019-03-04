<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table name="enrolments" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column>
		<a href="actor/display.do?actorId=${row.member.id}"><spring:message code="enrolment.display"/></a>
	</display:column>
	
	<jstl:if test="${isOwnBrotherhood}">
		<display:column>
			<a href="enrolment/brotherhood/edit.do?enrolmentId=${row.id}"><spring:message code="enrolment.edit.position"/></a>
		</display:column>
		
		<display:column>
			<a href="enrolment/brotherhood/remove.do?enrolmentId=${row.id}" onclick="return confirm('<spring:message code="enrolment.confirm.remove"/>')"><spring:message code="enrolment.remove"/></a>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${isRequestList}">
	
		<jstl:if test="${areaSelected == true}">
		
			<display:column>
				<a href="enrolment/brotherhood/enrol.do?enrolmentId=${row.id}"><spring:message code="enrolment.enrol"/></a>
			</display:column>
		
		</jstl:if>
		
		
		<jstl:if test="${areaSelected == false}">
		
			<display:column>
				<a href="brotherhood/brotherhood/selectArea.do"><spring:message code="select.area"/></a>
			</display:column>
		
		</jstl:if>
		
		<display:column>
			<a href="enrolment/brotherhood/reject.do?enrolmentId=${row.id}"><spring:message code="enrolment.reject"/></a>
		</display:column>
	</jstl:if>
	
	<display:column property="member.fullname" titleKey="enrolment.member.fullname" sortable="true"/>
	<display:column property="member.email" titleKey="enrolment.member.email" sortable="true"/>
	
	<jstl:if test="${!isRequestList}">
		<display:column titleKey="enrolment.position" sortable="true">
			<jstl:if test="${row.position ne null}">
				<jstl:out value="${positions.get(row.position.id)}"/>
			</jstl:if>
		</display:column>
	</jstl:if>
		
</display:table>

	<jstl:if test="${requestURI == 'enrolment/listMember.do'}">
		<a href="actor/display.do?actorId=${brotherhoodId}"><spring:message
			code="actor.return" /></a>
	</jstl:if>
