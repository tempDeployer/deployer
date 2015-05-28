package org.kana.rockhopper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author asarker
 * Servlet implementation class used by the home page
 */
@WebServlet("/NodesServlet")
public class NodesServlet extends HttpServlet {
	
	public NodesServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * code to make the service call
	 */
	private void makeWebServiceCall() {
		try {
			ChefClient chefClient = new ChefClient();
			JSONObject x = new JSONObject(chefClient.getNodes());
			System.out.println(x.toString());
			System.out.println(chefClient.getNodes());
		} catch (Exception e) {
			System.out.println("An error occurred.");
		}
	}

	/*
	 * function to execute the command from command line.
	 */
	private void makeSystemCall() {
		try {
			Process p = Runtime.getRuntime().exec("cmd /c dir");
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

	private void createJsonAndReturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		ChefClient chefClient = new ChefClient();
		JSONObject obj = new JSONObject();
		try {
			obj = new JSONObject(chefClient.getNodes());
		} catch (JSONException e) {
			System.out.println(e.toString());
		}
		System.out.println("Done");
		out.print(obj);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("test");
		makeSystemCall();
		makeWebServiceCall();
		createJsonAndReturn(request, response);
	}
}
