package org.kana.rockhopper;

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
		try {
			bootstrapper.bootstrapLinuxNode(ip, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

}