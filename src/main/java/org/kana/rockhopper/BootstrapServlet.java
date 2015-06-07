package org.kana.rockhopper;

import org.json.JSONException;
import org.json.JSONObject;
import org.kana.rockhopper.chefapi.ChefBoostrapperService;
import org.kana.rockhopper.chefapi.ChefBootstrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

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
		
		String ip = request.getParameter("ip");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JSONObject metadataObj = null;
		String cookbooks = null;
		String roles = null;
		String logFilePath = null;
		try {
			metadataObj = new JSONObject(request.getParameter("runlistItems"));
			cookbooks = metadataObj.optString("cookbooks");
			roles = metadataObj.optString("roles");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
			.format(new java.util.Date());
			logFilePath = ConfigurationUtil.getKey("deployer.logs.dir") + "/" + tStamp + ".log";
			Runnable bootstrapService = new ChefBoostrapperService(logFilePath, ip, userName, password, roles, cookbooks);
			new Thread(bootstrapService).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject resObj = new JSONObject();
		try {
			resObj.put("logfilePath", logFilePath);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(resObj);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

}
