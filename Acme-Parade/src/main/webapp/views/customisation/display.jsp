<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p>
	<strong> <spring:message code="customisation.systemName" />: </strong>
	<jstl:out value="${customisation.name}" />
</p>

<p> 
	<strong> <spring:message code="customisation.banner" />: </strong>
	<jstl:out value="${customisation.banner}" />		
</p>

<p>
	<strong> <spring:message code="customisation.englishWelcomeMessage" />: </strong>
	<jstl:out value="${customisation.englishWelcomeMessage}" />
</p>

<p>
	<strong> <spring:message code="customisation.spanishWelcomeMessage" />: </strong>
	<jstl:out value="${customisation.spanishWelcomeMessage}" />
</p>

<p>
	<strong> <spring:message code="customisation.countryCode" />: </strong>
	<jstl:out value="${customisation.countryCode}" />
</p>

<p>
	<strong> <spring:message code="customisation.timeCached" />: </strong>
	<jstl:out value="${customisation.timeCachedResults}" />
</p>

<p>
	<strong> <spring:message code="customisation.maxResults" />: </strong>
	<jstl:out value="${customisation.maxNumberResults}" />
</p>

<p>
	<strong> <spring:message code="customisation.rowLimit" />: </strong>
	<jstl:out value="${customisation.rowLimit}" />
</p>

<p>
	<strong> <spring:message code="customisation.columnLimit" />: </strong>
	<jstl:out value="${customisation.columnLimit}" />
</p>

<p>
	<strong> <spring:message code="customisation.threshold" />: </strong>
	<jstl:out value="${customisation.thresholdScore}" />
</p>

<p> <strong> <spring:message code="customisation.priorities" />: </strong> </p>	
<ul>
	<jstl:forEach var="row" items="${priorities}">
		<li> <jstl:out value="${row}" /> </li>
	</jstl:forEach>
</ul>

<p> <strong> <spring:message code="customisation.languages" />: </strong> </p>
<ul>
	<jstl:forEach var="row" items="${languages}">
		<li> <jstl:out value="${row}" /> </li>
	</jstl:forEach>
</ul>

<display:table name="positiveWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.positiveWords"/>
</display:table>

<display:table name="negativeWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.negativeWords"/>
</display:table>

<display:table name="spamWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.spamWords"/>
</display:table>

<!-- LINKS -->
<a href="customisation/administrator/edit.do">
	<spring:message code="customisation.edit" />
</a>
<br />

<a href="welcome/index.do">
	<spring:message code="customisation.return" />
</a>


	   
	   