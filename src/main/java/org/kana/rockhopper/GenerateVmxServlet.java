package org.kana.rockhopper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import org.kana.rockhopper.services.VmxService;

/**
 * Servlet implementation class NodeListServlet
 */
@WebServlet("/GenerateVmxServlet")
public class GenerateVmxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenerateVmxServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		String ip  = request.getParameter("ipAddress");
		if ( ip != null) {
			PrintWriter out = response.getWriter();
			try {
				String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
				Runnable vmxRunnable = new VmxService(tStamp, ip, Boolean.parseBoolean(request.getParameter("deployEc2")));
				String filePath = InetAddress.getLocalHost().getHostAddress() + " : " + ((VmxService)vmxRunnable).getLogfilePath();
				System.out.println(filePath);
				new Thread(vmxRunnable).start();
				JSONObject jo = new JSONObject();
				jo.put("filePath", filePath);
				out.print(jo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
