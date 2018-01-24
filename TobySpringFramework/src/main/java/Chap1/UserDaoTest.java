package Chap1;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class UserDaoTest {
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {

		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		User user = new User();
		user.setId("whiteship2");
		user.setName("성진");
		user.setPassword("testpassword");

		dao.add(user);
		assertThat(dao.getCount(), is(1));

		User user2 = dao.get(user.getId());

		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
