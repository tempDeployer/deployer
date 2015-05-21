package org.kana.rockhopper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

		if (request.getParameter("hostName") != null) {
			PrintWriter out = response.getWriter();
			String tStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
					.format(new java.util.Date());
			File fos = new File(ConfigurationUtil.getKey("LOGS_DIR") + "/"
					+ tStamp + ".txt");
			String hostName = request.getParameter("hostName");
			String cloneName = "clone" + hostName + tStamp;
			BufferedWriter bw = new BufferedWriter(new FileWriter(fos));
			KnifeVmxGeneration knifevmx = new KnifeVmxGeneration();
			knifevmx.cloneVm(hostName,
					 bw, tStamp, cloneName);
			if (Boolean.parseBoolean(request.getParameter("deployEc2"))) {
				ConvertVmxToOvf convertToOva = new ConvertVmxToOvf();
				String destDir = ConfigurationUtil.getKey("VMX_DEST_PATH") + cloneName + "-ovf/";
				File file = new File(destDir);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				convertToOva.convert(ConfigurationUtil.getKey("VMX_DEST_PATH") + cloneName + "/", cloneName,
						destDir, bw);
				Ec2UploadVmx createEc2Ins = new Ec2UploadVmx();
				createEc2Ins.uploadVmxToS3(destDir + cloneName + ".ovf-disk1.vmdk",
						bw);
			}
			try {
				JSONObject jo = new JSONObject();
				jo.put("test", "yeah");
				out.print(jo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				bw.close();
			}

		}
	}
}
