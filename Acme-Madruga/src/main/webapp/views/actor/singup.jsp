<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="confirm.telephone" var="confirmTelephone"/>
<form:form action="${Url}register${role}.do" modelAttribute="${role}" onsubmit="javascript:calcMD5();">
	<jstl:choose>
		<jstl:when test="${role == 'customer'}">
			<h2><spring:message code="header.customer"/></h2>
		
			<form:hidden path="fixUpTasks"/>
		</jstl:when>
		<jstl:when test="${role == 'handyWorker'}">
			<h2><spring:message code="header.handyworker"/></h2>
		
			<form:hidden path="finder"/>
			<form:hidden path="applications"/>
			<!-- <form:hidden path="make" /> -->
		</jstl:when>
		<jstl:when test="${role == 'sponsor'}">
			<h2><spring:message code="header.sponsor"/></h2>
		
			<form:hidden path="sponsorships"/>
		</jstl:when>
		<jstl:when test="${role == 'referee'}">
			<h2><spring:message code="header.referee"/></h2>
		
			<form:hidden path="complaints"/>
		</jstl:when>
		<jstl:when test="${role == 'administrator'}">
			<h2><spring:message code="header.administrator"/></h2>
		</jstl:when>
	</jstl:choose>
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isSuspicious"/>
	<form:hidden path="userAccount"/>
	
	<p><spring:message code="form.note"/></p>
	
	<fieldset>
		<legend><spring:message code="actor.legend"/></legend>
	
		<form:label path="name">
			<spring:message code="actor.name.requested" />
		</form:label>
		<form:input path="name"/>
		<form:errors cssClass="error" path="name" />
		<br />
		
		<form:label path="middleName">
			<spring:message code="actor.middlename" />
		</form:label>
		<form:input path="middleName"/>
		<form:errors cssClass="error" path="middleName" />
		<br />
		
		<form:label path="surname">
			<spring:message code="actor.surname.requested" />
		</form:label>
		<form:input path="surname"/>
		<form:errors cssClass="error" path="surname" />
		<br />
		
		<form:label path="photoLink">
			<spring:message code="actor.photoLink" />
		</form:label>
		<form:input path="photoLink"/>
		<form:errors cssClass="error" path="photoLink" />
		<br />
		
		<form:label path="email">
			<spring:message code="actor.email.requested" />
		</form:label>
		<form:input path="email"/>
		<form:errors cssClass="error" path="email" />
		<br />
		
		<form:label path="phoneNumber">
			<spring:message code="actor.phoneNumber" />
		</form:label>
		<form:input path="phoneNumber" placeholder="+34 (111) 654654654" id="phoneNumber"/>
		<form:errors cssClass="error" path="phoneNumber" />
		<br />
		
		<form:label path="address">
			<spring:message code="actor.address" />
		</form:label>
		<form:input path="address"/>
		<form:errors cssClass="error" path="address" />
		<br /> 
		 
		<jstl:if test="${role == 'handyWorker'}">
			<form:label path="make">
					<spring:message code="actor.handyworker.make" />
				</form:label>
				<form:input path="make"/>
				<form:errors cssClass="error" path="make" />
				<br /> 	
 		</jstl:if>
 		 
	</fieldset>
	
	<fieldset>
		<legend><spring:message code="userAccount.legend"/></legend>
	
		<label for="usernameId">
			<spring:message code="userAccount.username.requested" />
		</label>
		<input type="text" name="username" id="usernameId"/>
		<br />
		
		<label for="passwordId">
			<spring:message code="userAccount.password.requested" />
		</label>
		<input type="password" name="password" id="passwordId"/>
		<br />
		
		<label for="confirmPasswordId">
			<spring:message code="userAccount.confirmPassword.requested" />
		</label>
		<input type="password" name="confirmPassword" id="confirmPasswordId"/>
		<br />
		
<security:authorize access="hasRole('ADMIN')" >
<form:label path="userAccount.authorities">
		<spring:message code="actor.authority"/>
	</form:label>
	<form:select path="userAccount.authorities">
	<jstl:choose>
		<jstl:when test="${role == 'administrator'}">
				<form:option label="ADMINISTRATOR" value="ADMIN"/>
		</jstl:when>
		<jstl:when test="${role == 'referee'}">
				<form:option label="REFEREE" value="REFEREE"/>
		</jstl:when>
	</jstl:choose>
	
	</form:select>
	
 </security:authorize>
 
 
<security:authorize access="isAnonymous()" >
	<form:label path="userAccount.authorities">
		<spring:message code="actor.authority"/>
	</form:label>
	<form:select path="userAccount.authorities">
		<jstl:choose>
			<jstl:when test="${role == 'customer'}">
				<form:option label="CUSTOMER" value="CUSTOMER"/>
			</jstl:when>
			<jstl:when test="${role == 'handyWorker'}">
				<form:option label="HANDYWORKER" value="HANDYWORKER"/>
			</jstl:when>
			<jstl:when test="${role == 'sponsor'}">
				<form:option label="SPONSOR" value="SPONSOR"/>
			</jstl:when>
		</jstl:choose>
	</form:select>
</security:authorize>
		
		<input type="hidden" name="role" value="${role}"/>
	</fieldset>
	
	
	<input type="submit" name="save" value="<spring:message code="actor.save" />" onclick ="javascript: return checkTelephone('${confirmTelephone}');"/>
	<input type="button" name="cancel" value="<spring:message code="actor.cancel" />"
		onclick="javascript: relativeRedir('welcome/index.do')" />
</form:form>