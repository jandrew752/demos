package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class UserDAO implements IUserDAO {
	// We will follow a Data Access Object Design Pattern
	// This class will have instance methods
	// whose responsibility is to perform common CRUD operations
	// against the database as needed
	
	// Common operations:
	// findAll
	// findByUsername
	// update
	// insert
	// delete
	
	// We will declare a method for each of the above operations
	// Because of this consistency in needed CRUD operations
	// It is not uncommon to create an interface for our DAO classes
	
	@Override
	public List<User> findAll() {
		List<User> allUsers = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT * FROM project0.USERS";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				Role role = Role.valueOf(rs.getString("role"));
				
				User u = new User(id, username, password, role);
				allUsers.add(u);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("WE FAILED TO RETRIEVE ALL USERS");
			return null;
		}
		
		return allUsers;
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			
//			Statement stmt = conn.createStatement();
//			
//			String sql = "INSERT INTO users (username, password, role) VALUES ("
//						+ u.getUsername() + ", " + u.getPassword() + ", "
//						+ u.getRole() + ")";
//			
//			return stmt.execute(sql);
//			
//			// What if your username was "; DROP TABLE users CASCADE;"
//			// This problem is called SQL Injection
			
			String sql = "INSERT INTO project0.users (username, password, role) VALUES (?, ?, ?) RETURNING project0.users.id";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, u.getUsername()); // PreparedStatement will prevent any content from
			// being executed as SQL
			stmt.setString(2, u.getPassword());
			stmt.setObject(3, u.getRole());
			
			ResultSet rs;
			if((rs = stmt.executeQuery()) != null) {
				rs.next();
				
				int id = rs.getInt(1);
				
				return id;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("WE FAILED TO INSERT USER");
		}
		
		return 0; // Invalid primary key
	}

	@Override
	public boolean update(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
