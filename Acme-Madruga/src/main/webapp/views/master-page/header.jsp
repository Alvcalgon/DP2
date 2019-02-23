<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-Madruga, Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="customisation/administrator/display.do"> <spring:message code="master.page.customisation" /> </a></li>
					<li><a href="position/administrator/list.do"> <spring:message code="master.page.position" /> </a></li>					
					<li><a href="actor/administrator/create.do?role=administrator"><spring:message code="master.page.administrator.create" /></a></li>
					<li><a href="actor/administrator/list.do"><spring:message code="master.page.administrator.list" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message	code="master.page.requests" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/member/list.do"><spring:message code="master.page.requests.list" /></a></li>			
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message	code="master.page.requests" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/brotherhood/list.do"><spring:message code="master.page.requests.list" /></a></li>				
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		
			<li><a class="fNiv"><spring:message	code="master.page.register" /></a>	
			<ul>
				<li class="arrow"></li>
				<li><a href="actor/create.do?role=brotherhood"><spring:message code="master.page.brotherhood.create" /></a></li>
				<li><a href="actor/create.do?role=member"><spring:message code="master.page.member.create" /></a></li>
			</ul>
		</li>
		
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>		
					<li><a href="actor/administrator,brotherhood,member/display.do"><spring:message code="master.page.actor.display" /></a></li>			
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

