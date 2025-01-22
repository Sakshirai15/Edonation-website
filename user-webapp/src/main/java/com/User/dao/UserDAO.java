package com.User.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.User.model.User;

public class UserDAO {
	
	private static String jdbcURL="jdbc:mysql://localhost:30006/Edonationwebsite";
    private static String jdbcUserName="root";
    private static String jdbcPassword="Anshu@0097";
    
    
    private static final String INSERT_USER_SQL="Insert into Users"+"(user_id, username, email, password_hash, role) VALUES"+"(?,?,?,?,?);";
    private static final String SELECT_USER_BY_ID="SELECT * from USERS WHERE user_id =?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM USERS;";
    private static final String DELETE_USERS_SQL="DELETE FROM USERS WHERE user_id=?;";
    private static final String UPDATE_USERS_SQL="UPDATE USERS SET username=?,email=?,password_hash=?,role=? WHERE user_id=?;";
	
	
    
    public UserDAO() {
		super();
	}
    
    public Connection getConnection() {
    	Connection connection = null;
    	
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
			connection = DriverManager.getConnection(jdbcURL,jdbcUserName,jdbcPassword);
    	}catch(SQLException | ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return connection;
    	
    }
    
    public void insertUser(User user) throws SQLException {
    	UserDAO dao = new UserDAO();
    	
    	try(Connection connection = dao.getConnection())
    	{
    		PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
    		preparedStatement.setString(1, user.getUsername());
    		preparedStatement.setString(2, user.getEmail());
    		preparedStatement.setString(3,user.getPassword_hash());
    		preparedStatement.setString(4, user.getRole());
    		
    		preparedStatement.executeUpdate();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static User selectUser(int user_id) {
    	User user = new User();
    	UserDAO dao = new UserDAO();
    	
    	try(Connection connection = dao.getConnection()){
    		
    	    PreparedStatement preparedStatement=connection.prepareStatement(SELECT_USER_BY_ID);
    		preparedStatement.setInt(1, user_id);
    		
    		ResultSet resultSet = preparedStatement.executeQuery();
    		
    		while(resultSet.next()) {
    			user.setUser_id(user_id);
    			user.setUsername(resultSet.getString("username"));
    			user.setEmail(resultSet.getString("email"));
    			user.setPassword_hash(resultSet.getString("password_hash"));
    			user.setRole(resultSet.getString("role"));
    		}
    		
    	    
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return user;
    }
    
    public static List<User> selectAllUsers(){
    	List<User> users = new ArrayList<User>();
    	UserDAO dao = new UserDAO();
    	try(Connection connection = dao.getConnection()){
    		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
    		ResultSet resultSet = preparedStatement.executeQuery();
    		
    		while(resultSet.next()) {
    			int id = resultSet.getInt("id");
    			String uname=resultSet.getString("uname");
				String email=resultSet.getString("email");
				String password=resultSet.getString("passwd");
				String role=resultSet.getString("role");
				
				users.add(new User(id,uname,email,role,password));
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return users;
    	
    }
    
    public static boolean deleteUser(int id)
	{
		boolean status=false;
		UserDAO dao=new UserDAO();
		try(Connection connection=dao.getConnection())
		{
			PreparedStatement preparedStatement=connection.prepareStatement(DELETE_USERS_SQL);
			preparedStatement.setInt(1, id);
			
			status=preparedStatement.execute();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	public boolean updateUser(User user)
	{
		boolean status=false;
		UserDAO dao=new UserDAO();
		try(Connection connection=dao.getConnection())
		{
			PreparedStatement preparedStatement=connection.prepareStatement(UPDATE_USERS_SQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getRole());
			preparedStatement.setInt(4, user.getUser_id());
			
			status=preparedStatement.executeUpdate()>0;
			
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		return status;
	}
	
   public static void main(String args[])
   {
	   
	  UserDAO dao=new UserDAO();
	  User user=UserDAO.selectUser(1);
	  user.setUsername("demo");
	  user.setRole("demo");
	  user.setEmail("demo@abc.com");
	  Boolean status=dao.updateUser(user);
	  System.out.println(status);
		   
   }
    
    
}
