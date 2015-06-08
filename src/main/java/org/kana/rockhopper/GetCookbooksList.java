package org.kana.rockhopper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class GetCookbooksList
 */
@WebServlet("/GetCookbooksList")
public class GetCookbooksList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCookbooksList() {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			ChefClient chefClient = new ChefClient();
			JSONObject obj = new JSONObject();
			String metadata = request.getParameter("name");
			if (metadata.equalsIgnoreCase("Cookbooks")) {
				obj = new JSONObject(chefClient.getCookbooks());
			} else if (metadata.equalsIgnoreCase("Roles")) {
				obj = new JSONObject(chefClient.getRoles());
			}
			JSONObject resObj = new JSONObject();
			resObj.put("metadata", metadata);
			resObj.put("value", obj);
			out.print(resObj);
		} catch (JSONException e) {
			System.out.println(e.toString());
		}
		System.out.println("Done");

	}

}
