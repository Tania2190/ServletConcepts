package com.comeback.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DbController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection connection = null;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded succesufully");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		System.out.println("Config object created and extracted values from config object.\n\n");

		String url = getInitParameter("url");
		String username = getInitParameter("username");
		String password = getInitParameter("password");
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);

		try {
			connection = DriverManager.getConnection(url, username, password);

			if (connection != null) {
				System.out.println("Connection established succesfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("<h1>Request type is :: " + request.getMethod() + "</h1>");

		Statement statement = null;
		ResultSet rset = null;

		try {
			if (connection != null) {
				statement = connection.createStatement();
				rset = statement.executeQuery("select id,name,subject from student_details");
				
				if (rset != null) {
					System.out.println("ID"+"\t\t"+"NAME"+"\t\t"+"SUBJECT");
					while (rset.next()) {
						int id = rset.getInt(1);
						String name = rset.getString(2);
						String subject = rset.getString(3);
						System.out.println(id+"\t\t"+name+"\t\t"+subject);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				statement.close();
				connection.close();				
				out.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

	}

}
