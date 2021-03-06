package Chap1;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		ApplicationContext context = new GenericXmlApplicationContext(
				"applicationContext.xml");
		// ApplicationContext context = new
		// AnnotationConfigApplicationContext(DaoFactory.class);
		// ApplicationContext context = new
		// AnnotationConfigApplicationContext(CountionDaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);

		User user = new User();
		user.setId("whiteship2");
		user.setName("성진");
		user.setPassword("testpassword");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공 ");

		User user2 = dao.get(user.getId());

		if (!user.getName().equals(user2.getName())) {
			System.out.println("테스트 실패 (name)");
		} else if (!user.getPassword().equals(user2.getPassword())) {
			System.out.println("테스트 실패 (password)");
		} else {
			System.out.println("조회 테스트 성공");
		}

		// System.out.println(user2.getName());
		// System.out.println(user2.getPassword());
		System.out.println(user2.getId() + " 조회 성공 ");

		// CountingConnectionMaker ccm = context.getBean("connectionMaker",
		// CountingConnectionMaker.class);
		// System.out.println("Connection counter: " + ccm.getCounter());
	}
}
