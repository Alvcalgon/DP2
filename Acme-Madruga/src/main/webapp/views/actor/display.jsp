<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="hasRole('MEMBER')">
	<jstl:if test="${actor.userAccount.authorities=='[BROTHERHOOD]'}">
		<jstl:if test="${isEnrolled}">
			<h2><a href="enrolment/member/dropOut.do?brotherhoodId=${actor.id}" onclick="return confirm('<spring:message code="enrolment.confirm.drop.out"/>')"><spring:message code="actor.drop.out"/></a></h2>
		</jstl:if>
		<jstl:if test="${!isEnrolled && !existEnrolmentRequest && hasSelectedArea}">
			<h2><a href="enrolment/member/requestEnrolment.do?brotherhoodId=${actor.id}"><spring:message code="actor.request.enrolment"/></a></h2>
		</jstl:if>
		<jstl:if test="${!isEnrolled && !existEnrolmentRequest && !hasSelectedArea}">
			<p style="color:blue;"><spring:message code="actor.request.enrolment.warning"/></p>
		</jstl:if>
	</jstl:if>
</security:authorize>

<fieldset>
	<legend><spring:message code="actor.legend"/></legend>
	<p> <strong> <spring:message code="actor.name" /> </strong>  <jstl:out value="${actor.name}" /></p>
	
	<p> <strong> <spring:message code="actor.middlename" /> </strong>  <jstl:out value="${actor.middleName}" /></p>
	
	<p> <strong> <spring:message code="actor.surname" /> </strong>  <jstl:out value="${actor.surname}" /></p>

	<p> <strong> <spring:message code="actor.photo" /> </strong> <img alt="Photo" src="<jstl:out value="${actor.photo}" />"></p>
	
	<p> <strong> <spring:message code="actor.email" /> </strong>  <jstl:out value="${actor.email}" /></p>
	
	<p> <strong> <spring:message code="actor.phoneNumber" /> </strong>  <jstl:out value="${actor.phoneNumber}" /></p>

	<p> <strong> <spring:message code="actor.address" /> </strong>  <jstl:out value="${actor.address}" /></p>
	
	<jstl:if test="${actor.userAccount.authorities=='[BROTHERHOOD]'}">
		
		<p> <strong> <spring:message code="actor.brotherhood.title" /> </strong>  <jstl:out value="${actor.title}" /></p>
		
		<p> 
			<strong> <spring:message code="actor.brotherhood.establishmentDate" /> </strong>
	
			<spring:message code="actor.formatMoment1" var="formatMoment"/>
				<fmt:formatDate value="${actor.establishmentDate}" pattern="${formatMoment}"/>
		</p>
		
		<p> <strong> <spring:message code="actor.brotherhood.pictures" /> </strong> <img alt="Picture" src="<jstl:out value="${actor.pictures}" />"> </p>
		
		
		
		
		
	</jstl:if>
	
	<security:authorize access="hasRole('ADMIN')">
	
		<jstl:if test="${actor.isSpammer == null }">
			<p> <strong> <spring:message code="actor.isSpammer" /> </strong>  <jstl:out value="N/A" /></p>
		</jstl:if>
		
		<jstl:if test="${actor.isSpammer != null }">
			<p> <strong> <spring:message code="actor.isSpammer" /> </strong>  <jstl:out value="${actor.isSpammer}" /></p>
		</jstl:if>
		
		<jstl:if test="${actor.score == null }">
			<p> <strong> <spring:message code="actor.score" /> </strong>  <jstl:out value="N/A" /></p>
		</jstl:if>	
		
		<jstl:if test="${actor.score != null }">
			<p> <strong> <spring:message code="actor.score" /> </strong>  <jstl:out value="${actor.score}" /></p>
		</jstl:if>	
		
		<jstl:if test="${actor.isSpammer == true || actor.score < thresholdScore}">
			<a href="actor/administrator/changeBan.do?actorId=${actor.id}"><spring:message code="actor.ban"/></a>
		</jstl:if>
		
		<jstl:if test="${actor.userAccount.isBanned}">
			<a href="actor/administrator/changeBan.do?actorId=${actor.id}"><spring:message code="actor.unban"/></a>
		</jstl:if>
	</security:authorize>
	
	
	<jstl:if test="${isAuthorized == true}">
	<a href="actor/administrator,brotherhood,member/edit.do?actorId=${actor.id}"><spring:message code="actor.edit"/></a>
	</jstl:if>
</fieldset>

<jstl:if test="${actor.userAccount.authorities=='[BROTHERHOOD]'}">
<fieldset>
		<legend>
			<spring:message code="actor.brotherhood.legend" />
		</legend>
		<p>
			<strong> <spring:message code="actor.brotherhood.members" />:
			</strong> <a href="enrolment/listMember.do?brotherhoodId=${actor.id}"><spring:message
					code="actor.brotherhood.members" /></a>
		</p>

		<p>
			<strong> <spring:message
					code="actor.brotherhood.processions" />:
			</strong> <a href="procession/list.do?brotherhoodId=${actor.id}"><spring:message
					code="actor.brotherhood.processions" /></a>
		</p>

		<p>
			<strong> <spring:message code="actor.brotherhood.floats" />:
			</strong> <a href="float/list.do?brotherhoodId=${actor.id}"><spring:message
					code="actor.brotherhood.floats" /></a>
		</p>

		<p>
			<strong> <spring:message code="actor.brotherhood.area" />
			</strong> <a href="area/display.do?areaId=${actor.area.id}"> <jstl:out
					value="${actor.area.name}" /></a>
			
			<jstl:if test="${actor.area == null && isActorLogged}">

				<a href="brotherhood/brotherhood/selectArea.do"><spring:message
						code="select.area" /></a>

			</jstl:if>
			</p>
	</fieldset>
</jstl:if>


<fieldset>
	<legend><spring:message code="userAccount.legend"/></legend>
	<p> <strong> <spring:message code="actor.username" />: </strong>  <jstl:out value="${actor.userAccount.username}" /></p>
	
	<p> <strong> <spring:message code="actor.authority" />: </strong>  <jstl:out value="${actor.userAccount.authorities}" /></p>

</fieldset>

<fieldset>
	<legend><spring:message code="other.legend"/></legend>
	<p> <strong> <spring:message code="actor.socialProfiles" />: </strong>  <a href="socialProfile/list.do?actorId=${actor.id}"><spring:message code="actor.socialProfiles"/></a></p>

</fieldset>

 <security:authorize access="isAnonymous()"> 	
 	<a href="brotherhood/list.do"><spring:message code="actor.return"/></a>
 </security:authorize>

