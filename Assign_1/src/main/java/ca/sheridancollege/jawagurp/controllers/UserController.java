package ca.sheridancollege.jawagurp.controllers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.jawagurp.beans.Book;
import ca.sheridancollege.jawagurp.beans.User;
import ca.sheridancollege.jawagurp.database.DatabaseAccess;

@Controller
public class UserController {
	@Autowired
	private DatabaseAccess da;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/addBook")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		return "secure/addBook";
	}
	
	@PostMapping("/addBookToList")
	public String addBookToList(Model model, @ModelAttribute Book book) {
		da.addBookToList(book);
		model.addAttribute("book", new Book());
		model.addAttribute("bookList", da.getBookList());
		return "secure/index";
	}
	
	@GetMapping("/css/mycss.css")
	public String css() {
		return "css/mycss.css";
	}



	@PostMapping("/addUser")
	public String addUser(Model model, @ModelAttribute User user) {
		da.insertUser(user);
		model.addAttribute("user", new User());
		return "login";
	}

	List<Book> bookList = new CopyOnWriteArrayList<Book>();

	@GetMapping("/secure")
	public String secureIndex(Model model, Authentication authentication) {
		model.addAttribute("bookList", bookList);
		model.addAttribute("bookList", da.getBookList());
            return "/secure/index";
        
	}

	@GetMapping("/secure/details/{isbn}")
	public String details(Model model, @PathVariable String isbn) {
		List<Book> book = da.getBookDetails(isbn);
		model.addAttribute("book", book.get(0));
		return "/secure/details";
	}

	@PostMapping("/secure/cart/{isbn}")
	public String cart(Model model, @PathVariable String isbn) {
		List<Book> book = da.getBookDetails(isbn);
		da.addToCart(book.get(0));
		model.addAttribute("book", book.get(0));
		model.addAttribute("book", new Book());
		model.addAttribute("bookList", da.getBookList());
		return "/secure/index";
	}

	@GetMapping("/secure/viewCart")
	public String viewCart(Model model) {
		List<Book> cartList = da.getCartList();
		model.addAttribute("bookList", bookList);
		model.addAttribute("bookList", cartList);
		
		double totalPrice = 0.0;
	    for (Book book : cartList) {
	        totalPrice += book.getPrice();
	    }
	    model.addAttribute("totalPrice", totalPrice);
		return "/secure/cart";
	}

	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}

	@GetMapping("/deleteBook/{isbn}")
	public String deleteBookFromCart(Model model, @PathVariable String isbn) {
		da.deleteBookFromCart(isbn);
		List<Book> cartList = da.getCartList();
		model.addAttribute("book", new Book());
		model.addAttribute("bookList", cartList);
		
		double totalPrice = 0.0;
	    for (Book book : cartList) {
	        totalPrice += book.getPrice();
	    }
	    model.addAttribute("totalPrice", totalPrice);
		return "/secure/cart";
		
	}
	
	@GetMapping("/removeBook/{isbn}")
	public String removeBook(Model model, @PathVariable String isbn) {
		da.removeBook(isbn);
		List<Book> bookList = da.getBookList();
		model.addAttribute("bookList", bookList);
		return "secure/index";
	}
		
	
	@PostMapping("/secure/checkout")
	public String checkout(Model model) {
		da.deleteCart();
		return "/secure/checkout";
	}

}