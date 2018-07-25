package Chap1;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import Chap1.User.Level;
import static Chap1.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static Chap1.UserService.MIN_RECOMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired
	UserDao userDao;
	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "wjdtjdwlsqkq@naver.com"), new User(
				"joytouch", "강명호", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "wjdtjdwlsqkq@naver.com"), new User("erwins", "신승한", "p1",
				Level.SILVER, 60, MIN_RECOMEND_FOR_GOLD - 1, "wjdtjdwlsqkq@naver.com"), new User("madnite1", "이상호", "p1", Level.SILVER, 60,
				MIN_RECOMEND_FOR_GOLD, "wjdtjdwlsqkq@naver.com"), new User("green", "오민규", "p1", Level.GOLD, 100, Integer.MAX_VALUE,
				"wjdtjdwlsqkq@naver.com"));
	}

	@Test
	public void upgradeLevels() throws SQLException {
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}

		userService.upgradeLevels();

		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);

	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test
	public void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		assertThat(userWithLevel.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevel.getLevel(), is(userWithoutLevel.getLevel()));
	}

	@Test
	public void upgradeAllOrNothing() throws SQLException {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setTransactionManager(transactionManager);
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}

		checkLevelUpgraded(users.get(1), false);
	}

	static class TestUserService extends UserService {
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}

		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}

	static class TestUserServiceException extends RuntimeException {

	}
}
