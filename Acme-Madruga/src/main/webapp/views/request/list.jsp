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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<fieldset>
	<legend>
		<spring:message code="request.pending" />
	</legend>
	<display:table name="pendingRequests" id="row"
		requestURI="${requestURI }" class="displaytag" pagesize="5">

		<display:column>
			<a href="request/member/delete.do?requestId=${row.id}"><spring:message
					code="request.cancel" /></a>

		</display:column>

		<display:column property="procession.title"
			titleKey="request.procession" />

	</display:table>
</fieldset>

<fieldset>
	<legend>
		<spring:message code="request.approved" />
	</legend>
	<display:table name="approvedRequests" id="row"
		requestURI="${requestURI }" class="displaytag" pagesize="5">

		<display:column property="procession.title"
			titleKey="request.procession" />

		<display:column property="rowProcession"
			titleKey="request.rowProcession" />

		<display:column property="columnProcession"
			titleKey="request.columnProcession" />

	</display:table>
</fieldset>

<fieldset>
	<legend>
		<spring:message code="request.rejected" />
	</legend>
	<display:table name="rejectedRequests" id="row"
		requestURI="${requestURI }" class="displaytag" pagesize="5">

		<display:column property="reasonWhy"
			titleKey="request.reasonWhy" />

	</display:table>
</fieldset>

<br>
<br>
<input type="button" name="return"
	value="<spring:message code="socialProfile.return" />"
	onclick="javascript: relativeRedir('actor/display.do?actorId=${actorId}');" />