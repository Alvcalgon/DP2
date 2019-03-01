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

<p>
	<strong> <spring:message code="dashboard.two" />: </strong>
	<jstl:out value="${largestBrotherhoods}" />
</p>

<p>
	<strong> <spring:message code="dashboard.three" />: </strong>
	<jstl:out value="${largestBrotherhoods}" />
</p>

<p>
	<strong> <spring:message code="dashboard.four" />: </strong>
	<jstl:out value="ratio" />
</p>

<p> <strong> <spring:message code="dashboard.five" />: </strong> </p>
<display:table name="processions" id="row" requestURI="dashboard/administrator/processions" pagesize="5" class="displaytag">
	<display:column property="ticker" titleKey="procession.ticker" />
	<display:column property="title" titleKey="procession.title" />
</display:table>

<p> <strong> <spring:message code="dashboard.six" />: </strong> </p>
<p> <strong> <spring:message code="dashboard.seven" />: </strong> </p>

<p> <strong> <spring:message code="dashboard.eight" />: </strong> </p>

<p> <strong> <spring:message code="dashboard.nine" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="1.0" /> </td>
		<td> <jstl:out value="2.0" /> </td>
		<td> <jstl:out value="3.0" /> </td>
		<td> <jstl:out value="4.0" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.ten" />: </strong> </p>

<jstl:set var="hLabels" value="${histogramLabels}" />
<jstl:set var="hValues" value="${histogramValues}" />
<jstl:set var="n" value="${tam}" />

<jstl:out value="${hLabels}" />
<jstl:out value="${hValues}" />

<div id="container" style="width:800px; height:600px;">
	<canvas id="myChart"></canvas>
</div>
<script>
window.onload = function() {
	var w_labels = "<jstl:out value='${hLabels}' />";
	var w_values = "<jstl:out value='${hValues}' />";

	var filas = [];
	var columnas = [];
	for (var i in w_labels) {
		columnas.push(w_labels[i]);
		filas.push(w_values[i]);
	}	
	
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