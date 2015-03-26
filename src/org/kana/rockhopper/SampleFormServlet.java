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
 * Servlet implementation class SampleFormServlet
 */
@WebServlet("/SampleFormServlet")
public class SampleFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SampleFormServlet() {
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
	private void makeWebServiceCall() {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// specify the host, protocol, and port
			HttpHost target = new HttpHost("weather.yahooapis.com", 80, "http");

			// specify the get request
			HttpGet getRequest = new HttpGet("/forecastrss?p=80020&u=f");

			System.out.println("executing request to " + target);

			HttpResponse httpResponse = httpclient.execute(target, getRequest);
			HttpEntity entity = httpResponse.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			if (entity != null) {
				System.out.println(EntityUtils.toString(entity));
			}
			
			ChefClient chefClient = new ChefClient();
			System.out.println(chefClient.getCookbooks());
			//System.out.println(chefClient.getRoles());
			//System.out.println(chefClient.getEnvironments());
			//System.out.println(chefClient.getNodes());
			//System.out.println(chefClient.getNode("WINDOWS194.kana-test.com"));
			
			//Updating a Node
			String updateMessage = "{\"name\": \"WINDOWS194.kana-test.com\","
									+ "\"chef_type\": \"node\","
									+ "\"json_class\": \"Chef::Node\","
									+ "\"run_list\": [\"recipe[ke_starter]\",\"role[ke]\"]"
									+ "}";
			System.out.println(updateMessage);
			
			System.out.println(chefClient.updateNode("WINDOWS194.kana-test.com", updateMessage));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
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
		JSONObject obj = new JSONObject();
		System.out.println("Done");
		try {
			obj.put("message", "hello from " + request.getParameter("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//
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
