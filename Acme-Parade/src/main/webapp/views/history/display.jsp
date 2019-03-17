<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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


<p>
	<strong><spring:message code="history.brotherhood" /></strong> :
	<jstl:out value="${history.brotherhood.name}"></jstl:out>
</p>

<fieldset>
	<legend>
		<spring:message code="history.inceptionRecord" />
	</legend>
	<p>
		<strong><spring:message code="history.inceptionRecord.title" /></strong>
		:
		<jstl:out value="${history.inceptionRecord.title}"></jstl:out>
	</p>
	<p>
		<strong><spring:message code="history.inceptionRecord.text" /></strong>
		:
		<jstl:out value="${history.inceptionRecord.text}"></jstl:out>
	</p>
	<p>
		<jstl:if test="${not empty photos}">
			<strong><spring:message code="history.inceptionRecord.photos" />:</strong>
			<ul>
				<jstl:forEach var="photo" items="${photos}">
					<img src="${photo}" alt="photo" height="125px" width="200px">
				</jstl:forEach>
			</ul>
		</jstl:if>
	</p>
	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
			<a
				href="inceptionRecord/brotherhood/edit.do?inceptionRecordId=${history.inceptionRecord.id}">
				<spring:message code="history.edit" />
			</a>
		</jstl:if>
	</security:authorize>
</fieldset>

<fieldset>
	<legend>
		<spring:message code="history.periodRecord" />
	</legend>
	<display:table name="history.periodRecords" id="rowPeriodRecord"
		pagesize="5" class="displaytag" requestURI="${requestURI}">

		<security:authorize access="hasRole('BROTHERHOOD')">
			<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
				<display:column>
					<a
						href="periodRecord/brotherhood/edit.do?periodRecordId=${rowPeriodRecord.id}">
						<spring:message code="history.edit" />
					</a>
				</display:column>
			</jstl:if>
		</security:authorize>

		<display:column>
			<a
				href="periodRecord/display.do?periodRecordId=${rowPeriodRecord.id}"><spring:message
					code="history.display" />
			</a>
		</display:column>

		<spring:message code="history.periodRecord.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="false" />

		<spring:message code="history.periodRecord.text" var="textHeader" />
		<display:column property="text" title="${textHeader}" sortable="false" />

		<spring:message code="history.periodRecord.startYear"
			var="startYearHeader" />
		<display:column property="startYear" title="${startYearHeader}"
			sortable="false" />

		<spring:message code="history.periodRecord.endYear"
			var="endYearHeader" />
		<display:column property="endYear" title="${endYearHeader}"
			sortable="false" />

	</display:table>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
			<a href="periodRecord/brotherhood/create.do"> <spring:message
					code="history.periodRecord.create" />
			</a>
		</jstl:if>
	</security:authorize>
</fieldset>

<fieldset>
	<legend>
		<spring:message code="history.miscellaneousRecord" />
	</legend>
	<display:table name="history.miscellaneousRecords"
		id="rowMiscellaneousRecord" pagesize="5" class="displaytag"
		requestURI="${requestURI}">

		<security:authorize access="hasRole('BROTHERHOOD')">
			<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
				<display:column>
					<a
						href="miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${rowMiscellaneousRecord.id}">
						<spring:message code="history.edit" />
					</a>
				</display:column>
			</jstl:if>
		</security:authorize>

		<display:column>
			<a
				href="miscellaneousRecord/display.do?miscellaneousRecordId=${rowMiscellaneousRecord.id}"><spring:message
					code="history.display" /></a>
		</display:column>

		<spring:message code="history.miscellaneousRecord.title"
			var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="false" />

		<spring:message code="history.miscellaneousRecord.text"
			var="textHeader" />
		<display:column property="text" title="${textHeader}" sortable="false" />

	</display:table>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
			<a href="miscellaneousRecord/brotherhood/create.do"> <spring:message
					code="history.miscellaneousRecord.create" />
			</a>
		</jstl:if>
	</security:authorize>
</fieldset>

<fieldset>
	<legend>
		<spring:message code="history.legalRecord" />
	</legend>
	<display:table name="history.legalRecords" id="rowLegalRecord"
		pagesize="5" class="displaytag" requestURI="${requestURI}">

		<security:authorize access="hasRole('BROTHERHOOD')">
			<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
				<display:column>
					<a
						href="legalRecord/brotherhood/edit.do?legalRecordId=${rowLegalRecord.id}">
						<spring:message code="history.edit" />
					</a>
				</display:column>
			</jstl:if>
		</security:authorize>

		<display:column>
			<a href="legalRecord/display.do?legalRecordId=${rowLegalRecord.id}"><spring:message
					code="history.display" /></a>
		</display:column>

		<spring:message code="history.legalRecord.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="false" />

		<spring:message code="history.legalRecord.text" var="textHeader" />
		<display:column property="text" title="${textHeader}" sortable="false" />

		<spring:message code="history.legalRecord.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" sortable="false" />


		<spring:message code="history.legalRecord.vatNumber"
			var="vatNumberHeader" />
		<display:column property="vatNumber" title="${vatNumberHeader}"
			sortable="false" />

	</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
			<a href="legalRecord/brotherhood/create.do"> <spring:message
					code="history.legalRecord.create" />
			</a>
		</jstl:if>
	</security:authorize>


</fieldset>

<fieldset>
	<legend>
		<spring:message code="history.linkRecord" />
	</legend>
	<display:table name="history.linkRecords" id="rowLinkRecord"
		pagesize="5" class="displaytag" requestURI="${requestURI}">
		<security:authorize access="hasRole('BROTHERHOOD')">
			<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
				<display:column>
					<a
						href="linkRecord/brotherhood/edit.do?linkRecordId=${rowLinkRecord.id}">
						<spring:message code="history.edit" />
					</a>
				</display:column>
			</jstl:if>
		</security:authorize>

		<display:column>
			<a href="linkRecord/display.do?linkRecordId=${rowLinkRecord.id}"><spring:message
					code="history.display" /></a>
		</display:column>

		<spring:message code="history.linkRecord.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="false" />

		<spring:message code="history.linkRecord.text" var="textHeader" />
		<display:column property="text" title="${textHeader}" sortable="false" />


		<spring:message code="history.linkRecord.brotherhood"
			var="brotherhoodHeader" />
		<display:column property="brotherhood.name"
			title="${brotherhoodHeader}" sortable="false" />
	</display:table>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${brotherhoodLoginId == brotherhoodHistoryId}">
			<a href="linkRecord/brotherhood/create.do"> <spring:message
					code="history.linkRecord.create" />
			</a>
		</jstl:if>
	</security:authorize>

</fieldset>