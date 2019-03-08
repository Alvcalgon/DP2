<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<spring:message code="parade.formatDate" var="dateFormat"/>

<jstl:if test="${finder ne null}">
	<fieldset>
		<legend><spring:message code="parade.finder.parameters"/></legend>
		
		<p style="color:blue;"><spring:message code="parade.finder.warning"/><jstl:out value="${numberOfResults}"/></p>
		
		<ul>
			<li>
				<strong><spring:message code="parade.finder.keyword"/>: </strong>
				<jstl:out value="${finder.keyword}"/>
			</li>
			<li>
				<strong><spring:message code="parade.finder.area"/>: </strong>
				<jstl:out value="${finder.area}"/>
			</li>
			<li>
				<strong><spring:message code="parade.finder.minimum.date"/>: </strong>
				<fmt:formatDate value="${finder.minimumDate}" pattern="${dateFormat}"/>
			</li>
			<li>
				<strong><spring:message code="parade.finder.maximum.date"/>: </strong>
				<fmt:formatDate value="${finder.maximumDate}" pattern="${dateFormat}"/>
			</li>
		</ul>
		<div>
			<a href="finder/member/edit.do"><spring:message code="parade.finder.edit"/></a>
			&nbsp;
			<a href="finder/member/clear.do"><spring:message code="parade.finder.clear"/></a>
		</div>
	</fieldset>
</jstl:if>

<display:table pagesize="5" class="displaytag" name="parades"
	requestURI="${requestURI}" id="row">

	<display:column>
		<a href="parade/display.do?paradeId=${row.id}"> <spring:message
				code="parade.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${!row.isFinalMode}">
				<a href="parade/brotherhood/makeFinal.do?paradeId=${row.id}">
					<spring:message code="parade.makeFinal" />
				</a>

			</jstl:if>
		</display:column>
	</security:authorize>

	<jstl:if test="${isOwner}">
		<display:column>
			<a href="parade/brotherhood/edit.do?paradeId=${row.id}">
				<spring:message code="parade.edit" />
			</a>
		</display:column>

		<display:column property="isFinalMode" titleKey="parade.finalMode" />
	</jstl:if>

	<display:column property="title" titleKey="parade.title" />

	<spring:message code="parade.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="parade.moment"
		format="${formatMomentHeader}" />


</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	 
	<jstl:if test="${areaSelected && not hasFloats }">

		<a href="parade/brotherhood/create.do"><spring:message
				code="parade.create" /></a>

	</jstl:if>
	
	<jstl:if test="${ hasFloats }">
		<spring:message
				code="parade.info.notFloat" /><a href="float/brotherhood/create.do"><spring:message
				code="parade.info.notFloat1" /></a>
	</jstl:if>


	<jstl:if test="${!areaSelected}">

		<a href="brotherhood/brotherhood/selectArea.do"><spring:message
				code="select.area.parade" /></a>

	</jstl:if>
	 <br>
</security:authorize>
	
	<jstl:if test="${finder == null}">
		<a href="actor/display.do?actorId=${brotherhoodId}"><spring:message
				code="actor.return" /></a>
	</jstl:if>



