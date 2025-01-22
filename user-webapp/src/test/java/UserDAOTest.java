
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.SQLException;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.User.dao.UserDAO;
import com.User.model.User;
import com.User.model.User.*;

class UserDAOTest {

	private Connection mockConnection;
	private PreparedStatement mockPreparedStatement;
	UserDAO userDAO = new UserDAO();
	
	@Test
	void selectUser_testcase1()
	{
		User user = UserDAO.selectUser(1);
		assertNotNull(user);
	}
	
	@Test
	void selectAllUsers_testcase2()
	{
		List<User> users = UserDAO.selectAllUsers();
		assertFalse(users.size()>0);
	}
	
	@Test
	void deletelUser_testcase3() {
		assertFalse(userDAO.deleteUser(56));
	}
	
	
	
	@Test 
	void insertUser_testcase4() throws SQLException
	{
		User user1 = new User();
		UserDAO user = new UserDAO();
		
		user.insertUser( user1);
		assertTrue(user.selectAllUsers().isEmpty());
		assertEquals(1, user.selectAllUsers().size());
	}
	
}
