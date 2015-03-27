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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * function to execute the command from command line.
	 */
	private JSONArray geNodeList() {
		
		ObjectMapper objMapper = new ObjectMapper();
		JSONArray jsonArrayNodes = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		NodePojo nodePojo = new NodePojo();
		JSONObject jsonObjForUI = new JSONObject();
		JSONObject jsonNodes = new JSONObject();
		
		try {
			ChefClient chefClient = new ChefClient();
			
			jsonNodes = new JSONObject(chefClient.getNodes());
			
			for(Iterator<String> iterator = jsonNodes.keys(); iterator.hasNext();) {
			    
				String nodeIdentifier = (String) iterator.next();
			    jsonObj = new JSONObject(chefClient.getNode(nodeIdentifier));
				nodePojo = new NodePojo();
				
				nodePojo.setName(jsonObj.getString("name"));
				nodePojo.setChef_type(jsonObj.getString("chef_type"));
				nodePojo.setChef_environment(jsonObj.getString("chef_environment"));
				nodePojo.setNodeRunList(jsonObj.getJSONArray("run_list").toString());
				//nodePojo.setUptime(jsonObj.getJSONObject("automatic").getString("uptime"));
				//nodePojo.setHostname(jsonObj.getJSONObject("automatic").getString("hostname"));
				nodePojo.setFqdn(jsonObj.getJSONObject("automatic").getString("fqdn"));
				nodePojo.setOs_version(jsonObj.getJSONObject("automatic").getString("os_version"));
				nodePojo.setPlatform_version(jsonObj.getJSONObject("automatic").getString("platform_version"));
				nodePojo.setCpu_total(jsonObj.getJSONObject("automatic").getJSONObject("cpu").getInt("total"));
				nodePojo.setPlatform(jsonObj.getJSONObject("automatic").getString("platform"));
				nodePojo.setOs(jsonObj.getJSONObject("automatic").getString("os"));
				nodePojo.setIpaddress(jsonObj.getJSONObject("automatic").getString("ipaddress"));
				nodePojo.setRecipes(jsonObj.getJSONObject("automatic").getJSONArray("recipes").toString());

				jsonObjForUI = new JSONObject(objMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nodePojo));
				jsonArrayNodes.put(jsonObjForUI);
			    
			}
			
		}
		catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonArrayNodes;
	}

	private void returnJSONArrayOfNodes(HttpServletRequest request, HttpServletResponse response, JSONArray jsonArrayNodes) 
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		try {
			System.out.println("jsonArrayNodes ::: "+jsonArrayNodes);
			obj.put("message", jsonArrayNodes);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		out.print(obj);
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			JSONArray jsonArrayNodes = geNodeList();
			returnJSONArrayOfNodes(request, response, jsonArrayNodes);
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
	}
	
}
