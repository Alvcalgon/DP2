<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="confirm.telephone" var="confirmTelephone"/>
<form:form action="actor/administrator,brotherhood,member/edit.do" modelAttribute="actor" onsubmit="javascript:calcMD5();">
	<jstl:choose>
		<jstl:when test="${role == 'administrator'}">
			<h2><spring:message code="header.administrator"/></h2>
		</jstl:when>
		
		<jstl:when test="${role == 'brotherhood'}">
			<h2><spring:message code="header.brotherhood"/></h2>
		
			<form:hidden path="area"/>
			
		</jstl:when>
		
		<jstl:when test="${role == 'member'}">
			<h2><spring:message code="header.member"/></h2>
		</jstl:when>
		
		
	</jstl:choose>
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isSpammer"/>
	<form:hidden path="score"/>
	<form:hidden path="userAccount"/>
	
	<fieldset>
		<legend><spring:message code="actor.legend"/></legend>
	
		<acme:textbox code="actor.name" path="name"/>
		
		<br />
		
		<acme:textbox code="actor.middlename" path="middleName"/>
		
		
		<br />
		
		<acme:textbox code="actor.surname" path="surname"/>
		
	
		<br />
		
		<acme:textbox code="actor.photo" path="photo"/>
		
		<br />
		
		<acme:textbox code="actor.email" path="email" placeholder="mail@email.com"/>
		
		<br />
		
		<acme:textbox code="actor.phoneNumber" path="email" placeholder="+34 (111) 654654654"/>
		
		<br />
		
		<acme:textbox code="actor.address" path="address"/>
		
		<br /> 
		
		<jstl:if test="${role == 'brotherhood'}">
		
			<acme:textbox code="actor.brotherhood.title" path="title"/>
			<br /> 
			
			<acme:textbox code="actor.brotherhood.establishmentDate" path="establishmentDate"/>
			<br />
			
			<acme:textbox code="actor.brotherhood.pictures" path="pictures"/>
			<br />
		
		</jstl:if>
	</fieldset>
 
	<fieldset>
		<legend><spring:message code="userAccount.legend"/></legend>
	
		<acme:textbox code="userAccount.newUsername" path="userAccount.username" id="newusernameId"/>
		
		<br />
		
		<acme:password code="userAccount.newPassword" path="userAccount.password" id="passwordId"/>
		
		<br />
		
		<acme:password code="userAccount.confirmPassword" path="userAccount.password" id="confirmPasswordId"/>
		
		<br />
		
	</fieldset>
	
 
 
 	<jstl:choose>
		<jstl:when test="${role == 'administrator'}">
			<acme:submit name="saveAdmin" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${role == 'brotherhood'}">
			<acme:submit name="saveBrotherhood" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${role == 'member'}">
			<acme:submit name="saveMember" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
	</jstl:choose>
	
	<acme:cancel url="actor/administrator,brotherhood,member/display.do" code="actor.cancel"/>
 
	
	<hr>
	
</form:form>