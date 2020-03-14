package com.Training;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.WritableTypeId.Inclusion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Servlet implementation class UserManagement
 */

public class UserManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UserManagement() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		System.out.println("HI");
		Unirest.setTimeouts(0, 0);
		try {
			HttpResponse<String> tokenResponse = Unirest.post("http://104.211.200.228:7080/ECM-v5.4.1/api/login")
			  .header("Content-Type", "application/json")
			  .body("{\"username\":\"admin\",\"password\":\"password\"}")
			  .asString();
			
			System.out.println(tokenResponse.getStatus());
			
			
			
			ObjectMapper mapper = new ObjectMapper();
			com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(tokenResponse.getBody());
			String access_token = jsonNode.get("access_token").asText();
			
			System.out.println(access_token);
			ObjectNode in = mapper.createObjectNode();
			
			in.put("username",request.getParameter("username"));
			in.put("firstname",request.getParameter("firstname"));
			
			SimpleDateFormat sdfin = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfout = new SimpleDateFormat("MM-dd-yyyy");
			
			System.out.println(request.getParameter("startdate"));
			
			Date startDate=sdfin.parse(request.getParameter("startdate")); //get the parameter convert it to a data type Date.
			
			System.out.println(startDate);
			Date enddate=sdfin.parse(request.getParameter("enddate")); //get the parameter convert it to a data type Date.

			
			in.put("startdate",sdfout.format(startDate));
			in.put("enddate",sdfout.format(enddate));
			in.put("statuskey","1");
			in.put("passwordExpired","true");
			in.put("enabled","true");
			in.put("accountExpired","false");
			in.put("accountLocked","false");
			in.put("allowpastdate","true");
			System.out.println(in.toString());
			
			Unirest.setTimeouts(0, 0);
			
			/*
			HttpResponse<String> userAPIResponse = Unirest.post("http://104.211.200.228:7080/ECM-v5.4.1/api/v5/createUser")
			  .header("Content-Type", "application/json")
			  .header("Authorization", "Bearer "+access_token)
			  .body("{\n\"username\":\"charmi123\",\n\"firstname\":\"Charmi\",\n\"startdate\":\"10-18-2018\",\n\"enddate\":\"10-11-2019\",\n\"statuskey\":\"1\",\n\"passwordExpired\":\"true\",\n\"enabled\": \"true\",\n\"accountExpired\":\"true\",\n\"accountLocked\":\"false\",\n\"allowpastdate\":\"true\"\n}")
			  .asString();
			  
			 */
			
			
			
			
			HttpResponse<String> userAPIResponse = Unirest.post("http://104.211.200.228:7080/ECM-v5.4.1/api/v5/createUser")
					  .header("Content-Type", "application/json")
					  .header("Authorization", "Bearer "+access_token)
					  .body(in.toString())
					  .asString();
					  
					  
			
			System.out.println(userAPIResponse.getStatus());
			
			if(userAPIResponse.getStatus()==200) {
				System.out.println("USer Created Sucessfully");
			}
			
			response.getWriter().append("User Created Sucessfully!!");
			
			
			
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
