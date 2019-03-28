<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<ul>
	<li>
		<strong><spring:message code="process.deactivate.sponsorships"/></strong>
		<acme:cancel url="sponsorship/administrator/process/deactivate.do" code="process.launch"/>
	</li>
	
	<li>
		<form:form action="actor/administrator/spammersProcess.do">
			<strong><spring:message code="process.check.spammers"/></strong>
			<input type="submit" name="spammers" value="<spring:message code="process.launch" />" />
		</form:form>
	</li>
	
	<li>
		<form:form action="actor/administrator/computeScore.do">
			<strong><spring:message code="process.compute.score"/></strong>
			<input type="submit" name="compute" value="<spring:message code="process.launch" />" />
		</form:form>
	</li>
</ul>