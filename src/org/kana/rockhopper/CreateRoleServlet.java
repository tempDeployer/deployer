package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class CreateRoleServlet
 */
@WebServlet("/CreateRoleServlet")
public class CreateRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateRoleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * code to make the service call
	 */
	/*
	 * function to execute the command from command line.
	 */
	private void makeSystemCall(String fileName) {
		try {
			Process p = Runtime.getRuntime().exec("cmd /c cd C:/sc_projects/deployer/trunk/chef-repo && knife role from file " + fileName + ".rb");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException e1) {

		} catch (InterruptedException e2) {

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void writeFile(String name, String content) {
		try {
			// Whatever the file path is.
			File roleFile = new File(
					"C:/sc_projects/deployer/trunk/chef-repo/roles/" + name
							+ ".rb");
			FileOutputStream is = new FileOutputStream(roleFile);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			w.write(content);
			w.close();
		} catch (IOException e) {
			System.err.println("Problem writing to the file " + name + "rb");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		String fileName = request.getParameter("roleName");
//		writeFile(fileName,
//				request.getParameter("roleText"));
//		makeSystemCall(fileName);
		ChefClient client = new ChefClient();
		System.out.println(client.createRole(request.getParameter("roleText")));
		System.out.println(client.getRoles());
		//		JSONObject jObj;
//		try {
//			jObj = new JSONObject(client.getRoles());

//			// System.out.println(jObj.get("chef_handler"));
//			// System.out.println(jObj);
//			// System.out.println(client.getNode("10.156.22.28"));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		createJsonAndReturn(request, response);/	
		}

}
