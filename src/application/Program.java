package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department dp = new Department(1, "books");
		
		Seller seller = new Seller(12, "Joao", "joao@gmail.com", new Date(), 3000.00, dp);
		System.out.println(seller);
		
	}

}
