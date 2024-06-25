package ca.sheridancollege.jawagurp.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {
	private String title;
	private String author;
	private String isbn;
	private  float price;
	private String description;
	private String summary;
}