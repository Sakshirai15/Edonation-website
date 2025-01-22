package com.User.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.User.dao.UserDAO;
import com.User.model.User;

import jakarta.servlet.annotation.WebServlet;


@WebServlet("/")
public class Users_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	UserDAO dao;
    
    public Users_Servlet() {
        super();
        
    }
    
    public void init() {
    	dao = new UserDAO();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		
		switch(action) 
		{
		case "/new":showNewForm( request, response); break;
		case "/insert":try {
				insertUser( request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} break;
			
		case "/list": listUser(request, response); break;
		case "/login": login(request, response); break;
		
		case "/loginprocess": try {
				loginprocess(request, response);
			} catch (SQLException | ServletException | IOException e) {
				e.printStackTrace();
			} break;
		
		case "/logout": logout(request, response); break;
			
		}
	}
	
	
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
	
	public void loginprocess(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UserDAO userDAO = new UserDAO();
		
		try(Connection connection = userDAO.getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("select * from users where email=? and password=?");
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
			{
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("status", "active");
				httpSession.setAttribute("email", email);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("Welcome.jsp");
			    dispatcher.forward(request, response);
			}
			else 
			{
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("status", "Inactive");
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
				dispatcher.forward(request, response);
			}
		}
	}
	
	
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
	
	
	public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
		dispatcher.forward(request, response);
		
	}
	
	public void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException 
	{
		String user_id = request.getParameter("user_id");
		int userId = Integer.parseInt(user_id);
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		
		User user = new User( userId, name, email, password, role);
		
		dao.insertUser(user);
		
		response.sendRedirect("list");
		
	}
	
	public void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<User> users = dao.selectAllUsers();
		request.setAttribute("users", users);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user_List.jsp");
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
