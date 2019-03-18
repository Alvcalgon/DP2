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


<!-- ---------------------------------FINDER CONFIGURATION-------------------------------------- -->
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

<!-- ---------------------------------NOTFINAL LIST-------------------------------------- -->
<jstl:if test="${isOwner}">
<fieldset>
	<legend>
		<spring:message code="parade.status.notFinal" />
	</legend>
	
<display:table pagesize="5" class="displaytag" name="paradesNotFinal" requestURI="${requestURI}" id="rowNotFinal">
	
	<display:column>
		<a href="parade/display.do?paradeId=${rowNotFinal.id}"> 
			<spring:message code="parade.display" />
		</a>
	</display:column>
	
	<display:column>
		<a href="parade/brotherhood/edit.do?paradeId=${rowNotFinal.id}">
			<spring:message code="parade.edit" />
		</a>
	</display:column>

	
		<display:column>
			<jstl:if test="${!rowNotFinal.isFinalMode}">
				<a href="parade/brotherhood/makeFinal.do?paradeId=${rowNotFinal.id}">
					<spring:message code="parade.makeFinal" />
				</a>
			</jstl:if>
		</display:column>
		
		<display:column>
				<a href="parade/brotherhood/makeCopy.do?paradeId=${rowNotFinal.id}">
					<spring:message code="parade.makeCopy" />
				</a>
		</display:column>
	
	<display:column property="title" titleKey="parade.title" />

	<spring:message code="parade.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="parade.moment" 	format="${formatMomentHeader}"/>


</display:table>
</fieldset>
</jstl:if>



<!-- ---------------------------------SUBMITTED LIST-------------------------------------- -->
<jstl:if test="${isOwner || isChapterOwner}">
<fieldset>
	<legend>
		<spring:message code="parade.status.submitted" />
	</legend>
	
<display:table pagesize="5" class="displaytag" name="paradesSubmitted" requestURI="${requestURI}" id="rowSubmitted">
	
	<jstl:set var="colorValue" value="grey" />
	
	<display:column style="background-color:${colorValue }">
		<a href="parade/display.do?paradeId=${rowSubmitted.id}"> 
			<spring:message code="parade.display" />
		</a>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${isOwner}">
		<display:column style="background-color:${colorValue }">
			<a href="parade/brotherhood/edit.do?paradeId=${rowSubmitted.id}">
				<spring:message code="parade.edit" />
			</a>
		</display:column>
		
	</jstl:if>
	
		<display:column style="background-color:${colorValue }">
			<jstl:if test="${!rowSubmitted.isFinalMode && isOwner}">
				<a href="parade/brotherhood/makeFinal.do?paradeId=${rowSubmitted.id}">
					<spring:message code="parade.makeFinal" />
				</a>
			</jstl:if>
		</display:column>
		
		<display:column style="background-color:${colorValue }">
			<jstl:if test="${isOwner}">
				<a href="parade/brotherhood/makeCopy.do?paradeId=${rowSubmitted.id}">
					<spring:message code="parade.makeCopy" />
				</a>
			</jstl:if>
		</display:column>	
	</security:authorize>
	
		


	<security:authorize access="hasRole('CHAPTER')">
		<display:column style="background-color:${colorValue }">		
			<jstl:if test="${isChapterOwner && rowSubmitted.isFinalMode && rowSubmitted.status=='submitted'}">
				<a href="parade/chapter/accept.do?paradeId=${rowSubmitted.id}">
					<spring:message code="parade.accept" />
				</a>
			</jstl:if>
		</display:column>
		
		<display:column style="background-color:${colorValue }">
			<jstl:if test="${isChapterOwner && rowSubmitted.isFinalMode && rowSubmitted.status=='submitted'}">
				<a href="parade/chapter/reject.do?paradeId=${rowSubmitted.id}">
					<spring:message code="parade.reject" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>	
		
	<display:column property="title" titleKey="parade.title"  style="background-color:${colorValue }"/>

	<spring:message code="parade.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="parade.moment" 	format="${formatMomentHeader}"  style="background-color:${colorValue }"/>


</display:table>
</fieldset>
</jstl:if>

<!-- ---------------------------------REJECTED LIST-------------------------------------- -->
<jstl:if test="${isOwner || isChapterOwner}">
<fieldset>
	<legend>
		<spring:message code="parade.status.rejected" />
	</legend>
	
<display:table pagesize="5" class="displaytag" name="paradesRejected" requestURI="${requestURI}" id="rowRejected">
	
	<jstl:set var="colorValue" value="red" />
	<display:column style="background-color:${colorValue }">
		<a href="parade/display.do?paradeId=${rowRejected.id}"> 
			<spring:message code="parade.display" />
		</a>
	</display:column>

	<jstl:if test="${isOwner}">
		<display:column style="background-color:${colorValue }">
			<a href="parade/brotherhood/edit.do?paradeId=${rowRejected.id}">
				<spring:message code="parade.edit" />
			</a>
	</display:column>	
	
	<display:column style="background-color:${colorValue }">
				<a href="parade/brotherhood/makeCopy.do?paradeId=${rowRejected.id}">
					<spring:message code="parade.makeCopy" />
				</a>
		</display:column>
	</jstl:if>
	
	<display:column property="title" titleKey="parade.title"  style="background-color:${colorValue }"/>

	<spring:message code="parade.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="parade.moment" 	format="${formatMomentHeader}"  style="background-color:${colorValue }"/>

</display:table>
</fieldset>
</jstl:if>

<!-- ---------------------------------ACCEPTED LIST-------------------------------------- -->
<fieldset>
	<legend>
		<spring:message code="parade.status.accepted" />
	</legend>
	
<display:table pagesize="5" class="displaytag" name="paradesAccepted" requestURI="${requestURI}" id="rowAccepted">
	
	<jstl:set var="colorValue" value="green" />
	
	<display:column style="background-color:${colorValue }">
		<a href="parade/display.do?paradeId=${rowAccepted.id}"> 
			<spring:message code="parade.display" />
		</a>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">	
		<display:column style="background-color:${colorValue }" >
			<jstl:if test="${!rowAccepted.isFinalMode && isOwner}">
				<a href="parade/brotherhood/makeFinal.do?paradeId=${rowAccepted.id}">
					<spring:message code="parade.makeFinal" />
				</a>
			</jstl:if>
		</display:column>

	<jstl:if test="${isOwner}">
		<display:column style="background-color:${colorValue }">
			<a href="parade/brotherhood/edit.do?paradeId=${rowAccepted.id}">
				<spring:message code="parade.edit" />
			</a>
	</display:column>	
	<display:column style="background-color:${colorValue }">
				<a href="parade/brotherhood/makeCopy.do?paradeId=${rowAccepted.id}">
					<spring:message code="parade.makeCopy" />
				</a>
		</display:column>
	</jstl:if>	
	</security:authorize>
	
	<display:column property="title" titleKey="parade.title"  style="background-color:${colorValue }"/>

	<spring:message code="parade.formatMoment" var="formatMomentHeader" />
	<display:column property="moment" titleKey="parade.moment" 	format="${formatMomentHeader}"  style="background-color:${colorValue }"/>

</display:table>
</fieldset>





<!-- ---------------------------------LINK AND INFO-------------------------------------- -->
	<security:authorize access="hasRole('BROTHERHOOD')"> 
		<jstl:if test="${areaSelected && not hasFloats }">
			<a href="parade/brotherhood/create.do">
				<spring:message code="parade.create" />
			</a>
		</jstl:if>
	
		<jstl:if test="${ hasFloats }">
			<spring:message code="parade.info.notFloat" />
				<a href="float/brotherhood/create.do">
					<spring:message code="parade.info.notFloat1" />
				</a>
		</jstl:if>

		<jstl:if test="${!areaSelected}">
			<a href="brotherhood/brotherhood/selectArea.do">
				<spring:message code="select.area.parade" />
			</a>
		</jstl:if>
		 <br>
	</security:authorize>
	
	<jstl:if test="${finder == null}">
		<a href="actor/display.do?actorId=${brotherhoodId}">
			<spring:message code="actor.return" />
		</a>
	</jstl:if>



