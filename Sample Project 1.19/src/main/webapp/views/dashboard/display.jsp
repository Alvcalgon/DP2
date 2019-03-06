<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p> <strong> <spring:message code="dashboard.one" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataMembersPerBrotherhood[0]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[1]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[2]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[3]}" /> </td>
	</tr>
</table>

<spring:message code="brotherhood.date.format" var="dateFormat"/>

<p><strong> <spring:message code="dashboard.two" />: </strong></p>
<display:table name="largestBrotherhoods" id="row" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p><strong> <spring:message code="dashboard.three" />: </strong></p>
<display:table name="smallestBrotherhoods" id="row" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p> <strong> <spring:message code="dashboard.four" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="procession.ticker" /> </th>
		<th> <spring:message code="dashboard.request.pending" /> </th>
		<th> <spring:message code="dashboard.request.approved" /> </th>
		<th> <spring:message code="dashboard.request.rejected" /> </th>
	</tr>
	<jstl:forEach var="fila" items="${mapa.keySet()}">
		<tr>
			<td> <jstl:out value="${fila}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[0]}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[1]}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[2]}" /> </td>
		</tr>
	</jstl:forEach>
</table>

<p> <strong> <spring:message code="dashboard.five" />: </strong> </p>
<display:table name="processions" id="row" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="ticker" titleKey="procession.ticker" />
	<display:column property="title" titleKey="procession.title" />
</display:table>

<p> <strong> <spring:message code="dashboard.six" />: </strong> </p>
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.pending" />: </strong>
		<jstl:out value="${pendingRatio}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.approved" />: </strong>
		<jstl:out value="${approvedRatio}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.rejected" />: </strong>
		<jstl:out value="${rejectedRatio}" />
	</p>
	
<p> <strong> <spring:message code="dashboard.seven" />: </strong> </p>

<display:table name="members" id="row" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p> <strong> <spring:message code="dashboard.eight" />: </strong> </p>
<jstl:set var="hLabels" value="${histogramLabels}" />
<jstl:set var="hValues" value="${histogramValues}" />

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>

<div id="container" style="width:800px; height:600px;">
	<canvas id="myChart"></canvas>
</div>

<script>
window.onload = function() {
	var columnas = new Array();
	<jstl:forEach var="w_label" items="${hLabels}">
		var etiqueta = '${w_label}';
		columnas.push(etiqueta);
	</jstl:forEach>
	
	var filas = new Array();
	<jstl:forEach var="w_value" items="${hValues}">
		var val = '${w_value}';
		filas.push(val);
	</jstl:forEach>	
	
	var myChart = document.getElementById("myChart").getContext("2d");
	var massPopChart = new Chart(myChart, {
		type: 'bar',
		data: {
			labels: columnas,
			datasets:[{
				label: 'Histogram',
				data: filas,
				backgroundColor: 'green',
				borderColor: 'green',
				borderWidth: 1
			}]
		},
		options: {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero: true
					}
				}]
			}
		}
	});
};
</script>

<p> <strong> <spring:message code="dashboard.nine" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataBrotherhoodPerArea[0]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[1]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[2]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.ten" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataResultsPerFinder[0]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[1]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[2]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[3]}" /> </td>
	</tr>
</table>

<p>
	<strong> <spring:message code="dashboard.eleven" /> </strong>:
	<jstl:out value="${ratioEmptyVsNonEmpty}" />
</p>

<p>
	<a href="welcome/index.do"> <spring:message code="dashboard.return" /> </a>
</p>