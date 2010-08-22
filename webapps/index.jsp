<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri ="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri ="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- Index page for our meta search engine
	 Holds logic for client side validation in terms of javascript.
	 usage of struts tld files for doing form submissions.
	 Author: Anusha -->
	 
<html:html>
	<head>
	<title>EECS 767 Meta Search Engine</tile>
		<html:base/>
		<script language="javascript">
		function validateCheckBoxes(){
			var yahooCheck = document.getElementsByName ('yahooCheck');
			var altavistaCheck = document.getElementsByName ('altavistaCheck');
			var microsoftCheck = document.getElementsByName ('microsoftCheck');
			var lycosCheck = document.getElementsByName ('lycosCheck');
			var askCheck = document.getElementsByName ('askCheck');
			
			var i = 0;
			if (yahooCheck[0].checked){
				i=i+1;
			}
			if (altavistaCheck[0].checked){
				i=i+1;
			}
			if (microsoftCheck[0].checked){
				i=i+1;
			}
			if (lycosCheck[0].checked){
				i=i+1;
			}
			if (askCheck[0].checked){
				i=i+1;
			}
			
			if(i>2){
				if (!yahooCheck[0].checked){
					yahooCheck[0].disabled = true;
				}
				if (!altavistaCheck[0].checked){
					altavistaCheck[0].disabled = true;
				}
				if (!microsoftCheck[0].checked){
					microsoftCheck[0].disabled = true;
				}
				if (!lycosCheck[0].checked){
					lycosCheck[0].disabled = true;
				}
				if (!askCheck[0].checked){
					askCheck[0].disabled = true;
				}	
			}
			
			if(i<3){
				if (yahooCheck[0].disabled){
					yahooCheck[0].disabled = false;
				}
				if (altavistaCheck[0].disabled){
					altavistaCheck[0].disabled = false;
				}
				if (microsoftCheck[0].disabled){
					microsoftCheck[0].disabled = false;
				}
				if (lycosCheck[0].disabled){
					lycosCheck[0].disabled = false;
				}
				if (askCheck[0].disabled){
					askCheck[0].disabled = false;
				}	
			}
			document.getElementById('checkValue').value = i;
			return true;
		}
		
		function checkEverything(){
			if (document.getElementById('checkValue').value < 3){
				alert('Please Select 3 Search Engines');
				return false;
			}
			if((!document.getElementsByName('aggregationType')[0].checked)&&(!document.getElementsByName('aggregationType')[1].checked)){
				alert('Please Select the type of Aggregation');
				return false;
			}
			if(document.getElementsByName('searchString')[0].value == ""){
				alert('Please Enter a query to search for');
				return false;
			}
			return true;
		}
		</script>
	</head>
	
	<body>
	<center><html:img page="/images/search-engine-share.gif"/></center>
	<BR>
	<html:form action="/submitSearchForm">
	<center>
	<input id="checkValue" type="hidden" />
	<html:checkbox property="yahooCheck" onclick="javascript: return validateCheckBoxes()">
		<bean:message key="button.yahooSearch"/>
	</html:checkbox>
	<html:checkbox property="altavistaCheck" onclick="javascript: return validateCheckBoxes()">
		<bean:message key="button.altavistaSearch"/>
	</html:checkbox>
	<html:checkbox property="microsoftCheck" onclick="javascript: return validateCheckBoxes()">
		<bean:message key="button.microsoftSearch"/>
	</html:checkbox>
	<html:checkbox property="lycosCheck" onclick="javascript: return validateCheckBoxes()">
		<bean:message key="button.lycosSearch"/>
	</html:checkbox>
	<html:checkbox property="askCheck" onclick="javascript: return validateCheckBoxes()">
		<bean:message key="button.askSearch"/>
	</html:checkbox><br><br>
	
	<html:radio property="aggregationType" value="Single Page Aggregation">
		<bean:message key="button.singlePage"/>
	</html:radio>
	<html:radio property="aggregationType" value="Three Page Aggregation">
		<bean:message key="button.threePage"/>
	</html:radio><br><br>
	<html:text property="searchString" size="42" maxlength="42"/>
	<!--<html:submit property="searchEngineused">
		<bean:message key="button.lycosSearch"/>
	</html:submit>
	<html:submit property="searchEngineused">
		<bean:message key="button.askSearch"/>
	</html:submit>
	<html:submit property="searchEngineused">
		<bean:message key="button.yahooSearch"/>
	</html:submit>&nbsp;&nbsp;
	<html:submit property="searchEngineused">
		<bean:message key="button.altavistaSearch"/>
	</html:submit>&nbsp;&nbsp;
	<html:submit property="searchEngineused">
		<bean:message key="button.microsoftSearch"/>
	</html:submit>&nbsp;&nbsp;-->
	<html:submit property="searchEngineused" onclick="javascript: return checkEverything()">
		<bean:message key="button.search"/>
	</html:submit>&nbsp;&nbsp;
	</center>
	</html:form>
	<!--	<html:link page ="/CustomerDetails.jsp">Customer Form </html:link>-->
	</body>
</html:html>
