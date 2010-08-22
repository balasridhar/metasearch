<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri ="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri ="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- This is the failure page that is displayed whenever an error occurs
	 while processing the search query.
	 author: Anusha -->
<html:html>
	<head>
	<title>EECS 767  Meta Search Engine Results - <%= request.getAttribute("searchString")%> - Error Page  </title>
		<html:base/>
		<script language="javascript">
		function getFormFields(source,name){
			
		var pageNumber = document.getElementById('pageNumber');
		if ((pageNumber.value == 1)&&(name == "prev")){
			alert("You are in the First Page");
			return false;
		}
		document.forms[0].action = "/MetaSearch/submitPagination.do?pageNumber="+pageNumber.value+"&pageType="+name;
		/*alert(document.forms[0].action);*/
		document.forms[0].submit();
		return false;
		}
		
		function getRelevantInfo(source){
		var pageNumber = document.getElementById('pageNumber');
		var total = document.getElementById('numberSearchResults').value;
		var relevantIds = new Array();
		var ids = "check";
		var j=0;
		for(var i =0;i<total;i++){
			var name = ids+i;
			if(document.getElementById(name).checked){
				relevantIds[j] = document.getElementById(name).value;
				j=j+1;
			}
		}
		if(j>0){
			var stringForm = relevantIds.toString();
			var relevantInfo = document.getElementsByName ('relevantInfo');
			relevantInfo[0].value = pageNumber.value;
			var relevantIds = document.getElementsByName ('relevantIds');
			relevantIds[0].value = stringForm;
			document.forms[0].action = "/MetaSearch/submitRelevancy.do?pageNumber="+pageNumber.value+"&relevantIds="+stringForm;
			document.forms[0].submit();
		}else{
			alert("Please select some results to rearrange the results based on relevancy");
		}
		return false;
		}
		
		function checkColorCoding(){
			var color1 = false;
			var color2 = false;
			var color3 = false;
			
			var yahooCheck = document.getElementsByName ('yahooCheck');
			var altavistaCheck = document.getElementsByName ('altavistaCheck');
			var microsoftCheck = document.getElementsByName ('microsoftCheck');
			var lycosCheck = document.getElementsByName ('lycosCheck');
			var askCheck = document.getElementsByName ('askCheck');
			
			if(yahooCheck[0].checked){
				if(!color1){
					document.getElementById("yahooFont").color="red";
					color1 = true;
				}else if(!color2){
					document.getElementById("yahooFont").color="green";
					color2 = true;
				}else if(!color3){
					document.getElementById("yahooFont").color="orange";
					color3 = true;
				}
			}
			if(altavistaCheck[0].checked){
				if(!color1){
					document.getElementById("altavistaFont").color="red";
					color1 = true;
				}else if(!color2){
					document.getElementById("altavistaFont").color="green";
					color2 = true;
				}else if(!color3){
					document.getElementById("altavistaFont").color="orange";
					color3 = true;
				}
			}
			if(microsoftCheck[0].checked){
				if(!color1){
					document.getElementById("microsoftFont").color="red";
					color1 = true;
				}else if(!color2){
					document.getElementById("microsoftFont").color="green";
					color2 = true;
				}else if(!color3){
					document.getElementById("microsoftFont").color="orange";
					color3 = true;
				}
			}
			if(lycosCheck[0].checked){
				if(!color1){
					document.getElementById("lycosFont").color="red";
					color1 = true;
				}else if(!color2){
					document.getElementById("lycosFont").color="green";
					color2 = true;
				}else if(!color3){
					document.getElementById("lycosFont").color="orange";
					color3 = true;
				}
			}
			if(askCheck[0].checked){
				if(!color1){
					document.getElementById("askFont").color="red";
					color1 = true;
				}else if(!color2){
					document.getElementById("askFont").color="green";
					color2 = true;
				}else if(!color3){
					document.getElementById("askFont").color="orange";
					color3 = true;
				}
			}
		}
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
			validateCheckBoxes();
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
	
	<body onLoad="checkColorCoding()">
	<html:form action="/submitSearchForm">
	<html:img page="/images/small.jpeg"/>
	
	<html:text property="searchString" size="42" maxlength="42"/>
	<html:submit property="searchEngineused" onclick="javascript: return checkEverything()">
		<bean:message key="button.search"/>
	</html:submit>&nbsp;&nbsp;
	<input id="checkValue" type="hidden" />
	<html:hidden property="pageId"/>		
	<input id="pageNumber" type="hidden" value='<%= request.getAttribute("pageNumber")%>'/>
	<input id="numberSearchResults" type="hidden" value='<%= request.getAttribute("numResults")%>'/>
	
	<html:checkbox property="yahooCheck" onclick="javascript: return validateCheckBoxes()">
		<font id="yahooFont"><bean:message key="button.yahooSearch"/></font>
	</html:checkbox>
	<html:checkbox property="altavistaCheck" onclick="javascript: return validateCheckBoxes()">
		<font id="altavistaFont"><bean:message key="button.altavistaSearch"/></font>
	</html:checkbox>
	<html:checkbox property="microsoftCheck" onclick="javascript: return validateCheckBoxes()">
		<font id="microsoftFont"><bean:message key="button.microsoftSearch"/></font>
	</html:checkbox>
	<html:checkbox property="lycosCheck" onclick="javascript: return validateCheckBoxes()">
		<font id="lycosFont"><bean:message key="button.lycosSearch"/></font>
	</html:checkbox>
	<html:checkbox property="askCheck" onclick="javascript: return validateCheckBoxes()">
		<font id="askFont"><bean:message key="button.askSearch"/></font>
	</html:checkbox>
	&nbsp;&nbsp;&nbsp;
	
	<html:hidden property="numberOfResults"/>
	<html:hidden property="relevantInfo"/>
	<html:hidden property="relevantIds"/>
	<html:hidden property="startPosition"/>
	<html:hidden property="endPosition"/>
	<html:hidden property="searchResults"/>
	<html:hidden property="relevantSearchResults"/>
	
	<html:radio property="aggregationType" value="Single Page Aggregation">
		<bean:message key="button.singlePage"/>
	</html:radio>
	<html:radio property="aggregationType" value="Three Page Aggregation">
		<bean:message key="button.threePage"/>
	</html:radio>
	
	<hr>
	<!--<font color="brown"><b>Results for <%= request.getAttribute("searchString")%> - Page <%= request.getAttribute("pageNumber")%> - <%= request.getAttribute("startingPosition")%>-<%= request.getAttribute("endingPosition")%> </b></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font style="text-align: right;" color="cyan"><b><html:link href="" onclick="javascript: return getRelevantInfo(this.form)">Sort by Relevancy</html:link></b></font>-->
	<font size="+2", color="brown"><b><%= request.getAttribute("Search") %></b></font>
	<hr>
	<!--<p><font align="left"><html:link href="" onclick="javascript: return getFormFields(this.form,'prev')"><html:img page="/images/previous.png" align="left"/></html:link></font><font align="right"><html:link href="" onclick="javascript: return getFormFields(this.form,'next')"><html:img page="/images/next.png" align="right"/></html:link></font></p>-->
	</html:form>
	</body>
</html:html>
