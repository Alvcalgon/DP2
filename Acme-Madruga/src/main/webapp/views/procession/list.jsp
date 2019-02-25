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
	<a href="procession/brotherhood/create.do"><spring:message
			code="procession.create" /></a>
</security:authorize>


	
	 <input type="button" name="return" value="<spring:message code="procession.return" />" 
				onclick="javascript: window.history.back();" />

