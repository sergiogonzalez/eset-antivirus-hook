package com.sympo.documentlibrary.action;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portlet.documentlibrary.antivirus.AntivirusScannerException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Sergio González
 */
public class EditFileEntryAction extends BaseStrutsPortletAction {

	public void processAction(
			StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		try {
			originalStrutsPortletAction.processAction(
				originalStrutsPortletAction, portletConfig, actionRequest,
				actionResponse);
		}
		catch (AntivirusScannerException ase) {
			SessionErrors.add(actionRequest, ase.getClass());
		}
	}

	public String render(
			StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

			return originalStrutsPortletAction.render(
				null, portletConfig, renderRequest, renderResponse);
		}

	public void serveResource(
			StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
		throws Exception {

			originalStrutsPortletAction.serveResource(
				originalStrutsPortletAction, portletConfig, resourceRequest,
				resourceResponse);
	}

}