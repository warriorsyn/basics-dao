package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("============= FIND BY ID ==============");
		Seller seller = sellerDao.findById(7);
		System.out.println(seller);
		
		System.out.println("============= FIND BY DEPARTMENT ==============");
		List <Seller> list = sellerDao.findByDepartment(new Department(2, null));
		
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("============= FIND ALL ==============");
		list = sellerDao.findAll();
		
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("============= INSERT ==============");
		Seller newSeller = new Seller(null, "Shun", "shun@shun.com", new Date(), 4000.0, new Department(1, null));
		sellerDao.insert(newSeller);
	}

}
