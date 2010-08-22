package org.meta.search.IR.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.meta.search.IR.Bo.SearchSeparateEngines;
import org.meta.search.IR.Form.SearchForm;

/**
 * This class is the receiver of all the view page requests that are made.
 * @author Anusha
 */
public class SearchAction extends Action {

	/**
	 * This function is executed whenever a form is submitted.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		SearchForm sForm = (SearchForm)form;
		SearchSeparateEngines sse = new SearchSeparateEngines(sForm);
		String searchResult = null;
		
		String path = mapping.getPath();
		if(path.contains("submitPagination")){
			searchResult=getPageResults(sse, request,sForm);
		}else if (path.contains("submitRelevancy")){
			searchResult=getRelevantResults(sse,request,sForm);
		}else{
			searchResult=getNormalSearch(sse,request,sForm);
		}
	
		sForm.setSearchResults(searchResult);
		sForm.setRelevantSearchResults(sForm.getRelevantSearchResults());
		request.setAttribute("searchString", sForm.getSearchString());
		request.setAttribute("Search", searchResult);
		request.setAttribute("pageNumber", sForm.getPageId());
		request.setAttribute("numResults", sse.getNumberOfResultsReturned());
		ActionForward forward = null;
		if(!sse.isError()){
			forward = mapping.findForward("results");
		}else{
			forward = mapping.findForward("error");
		}
		return forward;
	}
	
	/** 
	 * This function handles the relevancy request submitted by the JSP Page.
	 * @param sse 
	 * @param request
	 * @param sf
	 * @return
	 */
	private String getRelevantResults(SearchSeparateEngines sse,HttpServletRequest request,SearchForm sf){
		String relevantResults = null;
		String relevantQuery = request.getParameter("relevantIds");
		int pgNo = new Integer(request.getParameter("pageNumber")).intValue();
		relevantResults = sse.searchRelevantProcess(pgNo, relevantQuery);
		sf.setPageId(pgNo);
		request.setAttribute("startingPosition", sf.getStartPosition());
		request.setAttribute("endingPosition", sf.getEndPosition());
		sf.setNumberOfResults(sse.getNumberOfResultsReturned());
		sf.setRelevantSearchResults(relevantResults);
		return relevantResults;
	}
	
	/**
	 * This function handles the page request submitted by the JSP page.
	 * if the request is 'next' then the next page of aggregated results are returned.
	 * if the request is 'prev' then the previous page of aggregated results are returned.
	 * @param sse
	 * @param request
	 * @param sf
	 * @return
	 */
	private String getPageResults(SearchSeparateEngines sse, HttpServletRequest request, SearchForm sf){
		String pageResult = null;
		int pgNo = 0;
		int pNo = 0;
		int sPosition=0;
		int ePosition=0;
		if(request.getParameter("pageNumber") != null)
			pgNo = new Integer(request.getParameter("pageNumber")).intValue();
		
		if("next".equals(request.getParameter("pageType"))){
			pNo = pgNo+1;
		}else if("prev".equals(request.getParameter("pageType"))){
			pNo = pgNo-1;
		}
		sf.setPageId(pNo);
		if("".equals(sf.getRelevantInfo())){
			pageResult = sse.searchProcess(pNo);
		}else{
			pageResult = sse.searchRelevantProcess(pNo, sf.getRelevantIds());
		}
		if("next".equals(request.getParameter("pageType"))){
			sPosition = sf.getEndPosition()+1;
			ePosition = sf.getEndPosition()+sse.getNumberOfResultsReturned();
		}else if("prev".equals(request.getParameter("pageType"))){
			sPosition = sf.getStartPosition()-(1+sse.getNumberOfResultsReturned());
			ePosition = sPosition+sse.getNumberOfResultsReturned();
		}
		request.setAttribute("startingPosition", sPosition);
		request.setAttribute("endingPosition", ePosition);
		sf.setNumberOfResults(ePosition);
		sf.setStartPosition(sPosition);
		sf.setEndPosition(ePosition);
		return pageResult;
	}
	
	/**
	 * Returns the results for the new search query term submitted by the user.
	 * @param sse
	 * @param request
	 * @param sf
	 * @return
	 */
	private String getNormalSearch(SearchSeparateEngines sse,HttpServletRequest request,SearchForm sf){
		String normalSearch = null;
		normalSearch = sse.searchProcess(1);
		sf.setPageId(1);
		request.setAttribute("startingPosition", 1);
		request.setAttribute("endingPosition", sse.getNumberOfResultsReturned());
		sf.setNumberOfResults(sse.getNumberOfResultsReturned());
		sf.setStartPosition(1);
		sf.setEndPosition(sse.getNumberOfResultsReturned());
		return normalSearch;
	}
}
