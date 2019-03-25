<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="actors" id="row" requestURI="${requestURI}" class="displaytag" pagesize="5">
	<display:column>
		<jstl:if test="${row.userAccount.authorities == '[BROTHERHOOD]' || row.userAccount.authorities == '[MEMBER]' || row.userAccount.authorities == '[CHAPTER]' 
			|| row.userAccount.authorities == '[SPONSOR]'}">
			<a href="actor/display.do?actorId=${row.id}"><spring:message code="actor.table.display.profile"/></a>
		</jstl:if>
	</display:column>
	
	<display:column property="fullname" titleKey="table.fullname" />
	
	<display:column property="email" titleKey="table.email" />
	
	<display:column property="phoneNumber" titleKey="table.phoneNumber" />
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column property="score" titleKey="table.score" />
	
		<display:column property="isSpammer" titleKey="table.isSpammer" />
		
		<display:column property="userAccount.username" titleKey="table.username" />
	</security:authorize>
	
	<security:authorize access="isAnonymous()">
		<jstl:if test="${row.userAccount.authorities == '[BROTHERHOOD]'}">
			<display:column property="title" titleKey="table.title" />
		
			<spring:message code="actor.formatMoment" var="formatMomentHeader" />
			<display:column  property="establishmentDate" titleKey="table.establishmentDate" sortable="true" format="${formatMomentHeader}" />
		</jstl:if>
		
		<jstl:if test="${row.userAccount.authorities=='[CHAPTER]'}">
			<display:column titleKey="actor.chapter.area">
				<a href="area/display.do?areaId=${row.area.id}">
					<jstl:out value="${row.area.name}" />
				</a>
			</display:column>
		</jstl:if>
	</security:authorize>
</display:table>

	<security:authorize access="hasRole('ADMIN')">	
		<form:form action="actor/administrator/computeScore.do">
		<input type="submit" name="compute"
			value="<spring:message code="actor.compute.score" />" />
		</form:form>
	
		<form:form action="actor/administrator/spammersProcess.do">
			<input type="submit" name="spammers"
				value="<spring:message code="actor.isSpammer.process" />" />
		</form:form>
	</security:authorize>
	
	 	
	<jstl:if test="${requestURI == 'member/list.do' }">
		<a href="actor/display.do?actorId=${brotherhoodId}"><spring:message code="actor.return"/></a>
	</jstl:if>
 	
