package Chap1;

import java.util.List;

public interface UserDao {
	public void add(final User user);

	public User get(String id);

	public List<User> getAll();

	public void deleteAll();

	public int getCount();
}
