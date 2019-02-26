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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<jstl:if test="${finder ne null}">
	<fieldset>
		<legend><spring:message code="procession.finder.parameters"/></legend>
		
		<p style="color:blue;"><spring:message code="procession.finder.warning"/><jstl:out value="${numberOfResults}"/></p>
		
		<ul>
			<li><strong><spring:message code="procession.finder.keyword"/>:</strong> <jstl:out value="${finder.keyword}"/></li>
			<li><strong><spring:message code="procession.finder.area"/>:</strong> <jstl:out value="${finder.area}"/></li>
			<li><strong><spring:message code="procession.finder.minimum.date"/>:</strong> <jstl:out value="${finder.minimumDate}"/></li>
			<li><strong><spring:message code="procession.finder.maximum.date"/>:</strong> <jstl:out value="${finder.maximumDate}"/></li>
		</ul>
		<div>
			<a href="finder/member/edit.do"><spring:message code="procession.finder.edit"/></a>
			&nbsp;
			<a href="finder/member/clear.do"><spring:message code="procession.finder.clear"/></a>
		</div>
	</fieldset>
</jstl:if>

<display:table pagesize="5" class="displaytag" name="processions"
	requestURI="${requestURI}" id="row">

	<display:column>
		<a href="procession/display.do?processionId=${row.id}"> <spring:message
				code="procession.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${!row.isFinalMode}">
				<a href="procession/brotherhood/makeFinal.do?processionId=${row.id}">
					<spring:message code="procession.makeFinal" />
				</a>

			</jstl:if>
		</display:column>
	</security:authorize>

	<jstl:if test="${isOwner}">
		<display:column>
			<a href="procession/brotherhood/edit.do?processionId=${row.id}">
				<spring:message code="procession.edit" />
			</a>
		</display:column>

		<display:column property="isFinalMode" titleKey="procession.finalMode" />
	</jstl:if>

	<security:authorize access="hasRole('MEMBER')">
		<display:column>
			<jstl:if test="${row.isFinalMode}">
				<jstl:if test="${memberAutorize==true}">
					<jstl:if test="${dateNow<row.moment}">
						<jstl:if test="${isRequest==true}">
							<jstl:forEach items="${requestsMember}" var="request">
								
								
							</jstl:forEach>
						</jstl:if>
					</jstl:if>
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>

	<display:column property="title" titleKey="procession.title" />

	<spring:message code="procession.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="procession.moment"
		format="${formatMomentHeader}" />


</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	 
	<jstl:if test="${areaSelected == true}">

		<a href="procession/brotherhood/create.do"><spring:message
				code="procession.create" /></a>

	</jstl:if>


	<jstl:if test="${areaSelected == false}">

		<a href="area/brotherhood/create.do?brotherhoodId=${brotherhoodId}"><spring:message
				code="select.area.procession" /></a>

	</jstl:if>
	 <br>
</security:authorize>
	
	<jstl:if test="${finder == null}">
		<a href="actor/display.do?actorId=${brotherhoodId}"><spring:message
				code="actor.return" /></a>
	</jstl:if>



