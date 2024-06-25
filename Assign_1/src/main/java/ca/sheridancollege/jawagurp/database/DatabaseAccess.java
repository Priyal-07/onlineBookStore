package ca.sheridancollege.jawagurp.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import ca.sheridancollege.jawagurp.beans.Book;
import ca.sheridancollege.jawagurp.beans.User;


@Component
@Repository
public class DatabaseAccess {
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	public void insertUser(User user) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO users(username, password, email, userRole) VALUES (:username, :password, :email, :userRole)";
		namedParameters.addValue("username", user.getUsername());
		namedParameters.addValue("password", user.getPassword());
		namedParameters.addValue("email", user.getEmail());
		namedParameters.addValue("userRole", "USER");
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
		System.out.println("User inserted into database");
		}
	}
	
	public void addBookToList(Book book) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO books(title, author, isbn, price, description, summary) VALUES (:title, :author, :isbn, :price, :description, :summary)";
		namedParameters.addValue("title", book.getTitle());
		namedParameters.addValue("author", book.getAuthor());
		namedParameters.addValue("isbn", book.getIsbn());
		namedParameters.addValue("price", book.getPrice());
		namedParameters.addValue("description", book.getDescription());
		namedParameters.addValue("summary", book.getSummary());
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
		System.out.println("Book inserted into database");
		}
	}
	
	public User findUserAccount(String username) {
		MapSqlParameterSource namedParameters = new
		MapSqlParameterSource();
		String query = "SELECT * FROM users where username = :username";
		namedParameters.addValue("username", username);
		try {
		return jdbc.queryForObject(query, namedParameters, new
		BeanPropertyRowMapper<>(User.class));
		} catch (EmptyResultDataAccessException erdae) {
		return null;
		}
		}
	
	public List<String> getRolesByUsername(String username) {
		MapSqlParameterSource namedParameters = new
		MapSqlParameterSource();
		String query = "SELECT userRole FROM  users where username = :username";
		namedParameters.addValue("username", username);
		return jdbc.queryForList(query, namedParameters, String.class);
		}
		
	
	
	public List<Book> getBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM books";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Book>(Book.class));
	}
	
	public List<Book> getBookDetails(String isbn) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM books where isbn = :isbn";
		 namedParameters.addValue("isbn", isbn);
		 return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Book>(Book.class));
	}
	
	public void addToCart(Book book) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO cart(title, author, isbn, price) VALUES (:title, :author, :isbn, :price)";
		namedParameters.addValue("title", book.getTitle());
		namedParameters.addValue("author", book.getAuthor());
		namedParameters.addValue("isbn", book.getIsbn());
		namedParameters.addValue("price", book.getPrice());
		//namedParameters.addValue("quantity", quantity);
		jdbc.update(query, namedParameters);
	}
	
	public List<Book> getCartList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM cart";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Book>(Book.class));
	}
	
	public void deleteBookFromCart(String isbn) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM cart where isbn = :isbn";
		namedParameters.addValue("isbn", isbn); 
		jdbc.update(query, namedParameters);
	}
	
	public void removeBook(String isbn) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM books where isbn = :isbn";
		namedParameters.addValue("isbn", isbn); 
		jdbc.update(query, namedParameters);
	}
	
	public void deleteCart() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM cart";
		jdbc.update(query, namedParameters);
	}
}
