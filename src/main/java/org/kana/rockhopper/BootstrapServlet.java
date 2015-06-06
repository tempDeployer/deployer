package org.kana.rockhopper;

import org.json.JSONException;
import org.json.JSONObject;
import org.kana.rockhopper.chefapi.ChefBootstrapper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author asarker
 * Servlet implementation class BootstrapServlet
 */

@WebServlet("/BootstrapServlet")
public class BootstrapServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public BootstrapServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ChefBootstrapper bootstrapper = new ChefBootstrapper();
		String ip = request.getParameter("ip");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String runlist = request.getParameter("resource");
		String runlistItem = request.getParameter("runlistItems");
		//System.out.println(runlistItem);
		JSONObject metadataObj = null;
		String cookbooks = null;
		String roles = null;
		try {
			metadataObj = new JSONObject(request.getParameter("runlistItems"));
			cookbooks = metadataObj.optString("cookbooks");
			roles = metadataObj.optString("roles");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bootstrapper.bootstrapLinuxNodeWithRunList(ip, userName, password, cookbooks, roles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

}
