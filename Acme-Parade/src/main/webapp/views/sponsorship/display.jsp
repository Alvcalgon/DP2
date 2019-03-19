<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<fieldset>
	<legend><spring:message code="sponsorship.general.fields"/></legend>
	
	<p>
		<strong><spring:message code="sponsorship.parade.referenced"/> </strong>
		<a href="parade/display.do?paradeId=${sponsorship.parade.id}"><jstl:out value="${sponsorship.parade.title}"/></a>
	</p>

	<p>
		<strong><spring:message code="sponsorship.active"/> </strong>
		<jstl:choose>
			<jstl:when test="${sponsorship.isActive}">
				<spring:message code="sponsorship.yes"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="sponsorship.no"/>
			</jstl:otherwise>
		</jstl:choose>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.target.url"/> </strong>
		<a href="${sponsorship.targetURL}"><jstl:out value="${sponsorship.targetURL}"/></a>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.banner"/> </strong>
		<img alt="Sponsorship banner" src="${sponsorship.banner}">
	</p>
</fieldset>

<fieldset>
	<legend><spring:message code="sponsorship.creditcard"/></legend>
	
	<p>
		<strong><spring:message code="sponsorship.creditcard.holder"/> </strong>
		<jstl:out value="${sponsorship.creditCard.holder}"/>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.creditcard.make"/> </strong>
		<jstl:out value="${sponsorship.creditCard.make}"/>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.creditcard.number"/> </strong>
		<jstl:out value="${sponsorship.creditCard.number}"/>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.creditcard.expiration.month"/> </strong>
		<jstl:out value="${sponsorship.creditCard.expirationMonth}"/>
	</p>
	
	<p>
		<strong><spring:message code="sponsorship.creditcard.expiration.year"/> </strong>
		<jstl:out value="${sponsorship.creditCard.expirationYear}"/>
	</p>
</fieldset>


<!-- Links -->

<div>
	<a href="sponsorship/sponsor/list.do"><spring:message code="sponsorship.return.list"/></a>
	&nbsp;
	<a href="sponsorship/sponsor/edit.do?sponsorshipId=${sponsorship.id}"><spring:message code="sponsorship.edit.extended"/></a>
	&nbsp;
	<jstl:choose>
		<jstl:when test="${sponsorship.isActive}">
			<a href="sponsorship/sponsor/remove.do?sponsorshipId=${sponsorship.id}"><spring:message code="sponsorship.remove"/></a>
		</jstl:when>
		<jstl:otherwise>
			<a href="sponsorship/sponsor/reactivate.do?sponsorshipId=${sponsorship.id}"><spring:message code="sponsorship.reactivate"/></a>
		</jstl:otherwise>
	</jstl:choose>
</div>
