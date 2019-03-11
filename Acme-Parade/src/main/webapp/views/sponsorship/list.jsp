<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table name="sponsorships" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column>
		<a href="sponsorship/sponsor/display.do?sponsorshipId=${row.id}"><spring:message code="sponsorship.display"/></a>
	</display:column>
	
	<display:column>
		<jstl:choose>
			<jstl:when test="${row.isActive}">
				<a href="sponsorship/sponsor/remove.do?sponsorshipId=${row.id}"><spring:message code="sponsorship.remove"/></a>
			</jstl:when>
			<jstl:otherwise>
				<a href="sponsorship/sponsor/reactivate.do?sponsorshipId=${row.id}"><spring:message code="sponsorship.reactivate"/></a>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<display:column>
		<a href="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}"><spring:message code="sponsorship.edit"/></a>
	</display:column>
	
	<display:column property="parade.title" titleKey="sponsorship.parade.table" sortable="true" href="parade/display.do" paramId="paradeId" paramProperty="parade.id"/>
	
	<display:column titleKey="sponsorship.active.table" sortable="true">
		<jstl:choose>
			<jstl:when test="${row.isActive}">
				<spring:message code="sponsorship.yes"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="sponsorship.no"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
		
</display:table>
