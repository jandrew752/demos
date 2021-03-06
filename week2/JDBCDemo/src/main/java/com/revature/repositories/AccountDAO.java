package com.revature.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class AccountDAO implements IAccountDAO {

	private IUserDAO userDao = new UserDAO();
	
	@Override
	public List<Account> findAll() {
		List<User> allUsers = userDao.findAll(); // Potentially unsorted
		
		List<Account> allAccounts = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT * FROM project0.ACCOUNTS";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				double balance = rs.getDouble("balance");
				int ownerId = rs.getInt("owner");
				
				// find the User object in allUsers that matches the ownerId;
				User owner = null;
				// If there is no owner, then the Account object will have a null value for the owner
				// Which makes sense
				// Alternatively, we could perform encapsulation within the Account class if we
				// anticipate that all accounts should have an owner
				for(int i = 0; i < allUsers.size(); i++) {
					if(allUsers.get(i).getId() == ownerId) {
						owner = allUsers.get(i);
					}
				}
				
				Account a = new Account(id, balance, owner);
				
				allAccounts.add(a);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("WE FAILED TO RETRIEVE ALL ACCOUNTS");
			return null;
		}
		
		return allAccounts;
	}

	@Override
	public Account findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(Account a) {
		String sql = "INSERT INTO project0.accounts (balance, owner) VALUES (?, ?) RETURNING project0.accounts.id";
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setDouble(1, a.getBalance()); // PreparedStatement will prevent any content from
			// being executed as SQL
			stmt.setInt(2, a.getOwner().getId());
			
			ResultSet rs;
			if((rs = stmt.executeQuery()) != null) {
				rs.next();
				
				int id = rs.getInt(1);
				
				return id;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("WE FAILED TO INSERT ACCOUNT");
		}
	
		return 0; // Invalid primary key
	}

	@Override
	public boolean update(Account a) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean transfer(int source_account, int target_account, double amount) {
		String sql = "{ call transfer(?, ?, ?) }";
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.setInt(1, source_account);
			stmt.setInt(2, target_account);
			stmt.setDouble(3, amount);
			
			stmt.execute();
			
			return true;
		} catch (SQLException e) { }
	
		return false;
	}
	
	@Override
	public boolean transfer2(Account source_account, Account target_account, double amount) {
		
		if(amount > source_account.getBalance()) {
			return false;
		}
		
		if(amount < 0) {
			return false;
		}
		
		Connection conn = ConnectionUtil.getConnection();

		try {
			
			conn.setAutoCommit(false);
			
			String sql_withdraw = "UPDATE project0.accounts SET balance = ? WHERE id = ?";
			
			PreparedStatement stmt_withdraw = conn.prepareStatement(sql_withdraw);
			
			stmt_withdraw.setDouble(1, source_account.getBalance() - amount);
			stmt_withdraw.setInt(2, source_account.getId());
			
			if(stmt_withdraw.executeUpdate() == 1) {
				String sql_deposit = "UPDATE project0.accounts SET balance = ? WHERE id = ?";
				
				PreparedStatement stmt_deposit = conn.prepareStatement(sql_deposit);
				
				stmt_deposit.setDouble(1, target_account.getBalance() + amount);
				stmt_deposit.setInt(2, target_account.getId());
				
				if(stmt_deposit.executeUpdate() != 1) {
					System.out.println("DEPOSIT FAILED IN TRANSFER");
					conn.rollback();
				}
				
				conn.commit();
				return true;
			} else {
				System.out.println("WITHDRAW FAILED IN TRANSFER");
				conn.rollback();
			}
			
		} catch(SQLException e) { 
			System.out.println("AN EXCEPTION OCCURRED");
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public String toUppercase(String lowercase) {
		
		String sql = "{ ? = call upper(?) }";
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.registerOutParameter(1, Types.VARCHAR);
			
			stmt.setString(2, lowercase);
			
			stmt.execute();
			
			return stmt.getString(1);
		} catch(SQLException e) { }
		
		return null;
	}
}
