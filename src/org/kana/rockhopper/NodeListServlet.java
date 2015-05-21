package org.kana.rockhopper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kana.rockhopper.chef.NodePojo;
import org.kana.rockhopper.chef.SimpleNodePojo;
import org.kana.rockhopper.ConfigurationUtil;
/**
 * Servlet implementation class NodeListServlet
 */
@WebServlet("/NodeListServlet")
public class NodeListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NodeListServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONArray jsonArrayNodes = geNodeList();
			returnJSONArrayOfNodes(request, response, jsonArrayNodes);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");

		if (request.getParameter("nodeName") != null) {
			PrintWriter out = response.getWriter();
			ChefClient cc = new ChefClient();
			String jsonNode = cc.getNode(request.getParameter("nodeName"));
			if (jsonNode != null) {
				try {
					JSONObject jo = getNodeDetails(jsonNode);
					out.print(jo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				response.sendError(400);
			}
		}
	}

	private JSONObject getNodeDetails(String nodeJson) {

		ObjectMapper objMapper = new ObjectMapper();
		JSONObject jsonObj = new JSONObject();
		NodePojo nodePojo = new NodePojo();
		JSONObject jsonObjForUI = new JSONObject();

		try {
			jsonObj = new JSONObject(nodeJson);
			nodePojo = new NodePojo();

			nodePojo.setName(jsonObj.getString("name"));
			nodePojo.setChef_type(jsonObj.getString("chef_type"));
			nodePojo.setChef_environment(jsonObj.getString("chef_environment"));
			nodePojo.setNodeRunList(jsonObj.getJSONArray("run_list").toString());

			if (jsonObj.getJSONObject("automatic") != null
					&& jsonObj.getJSONObject("automatic").length() > 0) {
				if (jsonObj.getJSONObject("automatic").getString("uptime") != null) {
					nodePojo.setUptime(jsonObj.getJSONObject("automatic")
							.getString("uptime"));
				} else
					nodePojo.setUptime("");

				nodePojo.setHostname(jsonObj.getJSONObject("automatic")
						.getString("hostname"));
				nodePojo.setFqdn(jsonObj.getJSONObject("automatic").getString(
						"fqdn"));
				nodePojo.setOs_version(jsonObj.getJSONObject("automatic")
						.getString("os_version"));
				nodePojo.setPlatform_version(jsonObj.getJSONObject("automatic")
						.getString("platform_version"));
				nodePojo.setCpu_total(jsonObj.getJSONObject("automatic")
						.getJSONObject("cpu").getInt("total"));
				nodePojo.setPlatform(jsonObj.getJSONObject("automatic")
						.getString("platform"));
				nodePojo.setOs(jsonObj.getJSONObject("automatic").getString(
						"os"));
				nodePojo.setIpaddress(jsonObj.getJSONObject("automatic")
						.getString("ipaddress"));
				nodePojo.setRecipes(jsonObj.getJSONObject("automatic")
						.getJSONArray("recipes").toString());
				nodePojo.setMacaddress(jsonObj.getJSONObject("automatic")
						.getString("macaddress"));
				nodePojo.setVirtualization(jsonObj.getJSONObject("automatic")
						.getJSONObject("virtualization").toString());
			} else {
				nodePojo.setUptime("Node bootstrapped yet");
				nodePojo.setHostname("Node bootstrapped yet");
				nodePojo.setFqdn("Node bootstrapped yet");
				nodePojo.setOs_version("Node bootstrapped yet");
				nodePojo.setPlatform_version("Node bootstrapped yet");
				nodePojo.setCpu_total(0);
				nodePojo.setPlatform("Node bootstrapped yet");
				nodePojo.setOs("Node bootstrapped yet");
				nodePojo.setIpaddress("Node bootstrapped yet");
				nodePojo.setRecipes("Node bootstrapped yet");
				nodePojo.setMacaddress("Node bootstrapped yet");
				nodePojo.setVirtualization("Node bootstrapped yet");
			}

			jsonObjForUI = new JSONObject(objMapper.writer()
					.withDefaultPrettyPrinter().writeValueAsString(nodePojo));

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObjForUI;
	}

	/*
	 * function to execute the command from command line.
	 */
	private JSONArray geNodeList() {

		ObjectMapper objMapper = new ObjectMapper();
		JSONArray jsonArrayNodes = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjForUI = new JSONObject();
		JSONObject jsonNodes = new JSONObject();

		try {
			ChefClient chefClient = new ChefClient();

			jsonNodes = new JSONObject(chefClient.getNodes());

			for (Iterator<String> iterator = jsonNodes.keys(); iterator
					.hasNext();) {

				String nodeIdentifier = (String) iterator.next();
				jsonObj = new JSONObject(chefClient.getNode(nodeIdentifier));
				SimpleNodePojo snp = new SimpleNodePojo();

				snp.setName(jsonObj.getString("name"));
				snp.setNodeRunList(jsonObj.getJSONArray("run_list").toString());
				try{
				String hostName = jsonObj.getJSONObject("automatic").getString("hostname") != null ? jsonObj.getJSONObject("automatic").getString("hostname") : "";
				snp.setHostName(hostName);
				}
				catch(Exception e)
				{
					snp.setHostName("");

				}
				if (jsonObj.getJSONObject("automatic") != null
						&& jsonObj.getJSONObject("automatic").length() > 0) {
					snp.setIpaddress(jsonObj.getJSONObject("automatic")
							.getString("ipaddress"));
					if (jsonObj.getJSONObject("automatic").getString("uptime") != null) {
						snp.setIsMachineUp(true);
					} else
					snp.setIsMachineUp(false);
				} else {
					snp.setIpaddress("Node bootstrapped yet");
					snp.setIsMachineUp(false);

				}
				jsonObjForUI = new JSONObject(objMapper.writer()
						.withDefaultPrettyPrinter().writeValueAsString(snp));
				jsonArrayNodes.put(jsonObjForUI);

			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArrayNodes;
	}

	private void returnJSONArrayOfNodes(HttpServletRequest request,
			HttpServletResponse response, JSONArray jsonArrayNodes)
			throws ServletException, IOException {

		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		try {
			obj.put("jsonArrayNodes", jsonArrayNodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(obj);
	}

}
