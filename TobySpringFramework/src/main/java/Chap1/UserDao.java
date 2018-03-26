package Chap1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UserDao {
	private DataSource dataSource;
	private User user;
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public void add(final User user) throws ClassNotFoundException, SQLException {
		this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select * from users where id = ?");

			ps.setString(1, id);
			rs = ps.executeQuery();
			this.user = null;

			if (rs.next()) {
				this.user = new User();
				this.user.setId(rs.getString("id"));
				this.user.setName(rs.getString("name"));
				this.user.setPassword(rs.getString("password"));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {

				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {

				}
			}
		}

		if (this.user == null)
			throw new EmptyResultDataAccessException(1);

		return this.user;
	}

	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() throws SQLException {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
}
