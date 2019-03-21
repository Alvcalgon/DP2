<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<spring:message code="finder.formatDate" var="dateFormat"/>
<spring:message code="finder.formatMoment" var="momentFormat"/>

<fieldset>
	<legend><spring:message code="finder.parameters"/></legend>
	
	<p style="color:blue;"><spring:message code="finder.warning"/><jstl:out value="${numberOfResults}"/></p>
	
	<ul>
		<li>
			<strong><spring:message code="finder.keyword"/>: </strong>
			<jstl:out value="${finder.keyword}"/>
		</li>
		<li>
			<strong><spring:message code="finder.area"/>: </strong>
			<jstl:out value="${finder.area}"/>
		</li>
		<li>
			<strong><spring:message code="finder.minimum.date"/>: </strong>
			<fmt:formatDate value="${finder.minimumDate}" pattern="${dateFormat}"/>
		</li>
		<li>
			<strong><spring:message code="finder.maximum.date"/>: </strong>
			<fmt:formatDate value="${finder.maximumDate}" pattern="${dateFormat}"/>
		</li>
	</ul>
	<div>
		<a href="finder/member/edit.do"><spring:message code="finder.edit"/></a>
		&nbsp;
		<a href="finder/member/clear.do"><spring:message code="finder.clear"/></a>
	</div>
</fieldset>

<display:table pagesize="5" class="displaytag" name="${finder.parades}" requestURI="${requestURI}" id="row">
	<display:column>
		<a href="parade/display.do?paradeId=${row.id}">
			<spring:message code="finder.display"/>
		</a>
	</display:column>

	<display:column property="title" titleKey="finder.parade.title" />
	<display:column property="moment" titleKey="finder.parade.moment" format="${momentFormat}" />
</display:table>