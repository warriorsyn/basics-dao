package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn = null;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement stmt = null;
		
		try {
			
			stmt = conn.prepareStatement(
						"INSERT INTO seller "
						+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
						+"VALUES "
						+"(?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS
					);
			stmt.setString(1, obj.getName());
			stmt.setString(2, obj.getEmail());
			stmt.setDate(3, new Date(obj.getBithDate().getTime()));
			stmt.setDouble(4, obj.getBaseSalary());
			stmt.setInt(5, obj.getDepartment().getId());
			
			int rows = stmt.executeUpdate();
			if( rows > 0 ) {
				ResultSet rs = stmt.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error, No rows affected!");
			}
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(stmt);
		}
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?"
					);
			
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			if( rs.next() ) {
				
				Department department = departmentFunction(rs);
				Seller seller = sellerFunction(rs, department);
				
				return seller;
			
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
		
	}
	
	private Seller sellerFunction(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBithDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(department);
		return seller;
	}

	private Department departmentFunction(ResultSet rs) throws SQLException {
		Department department =  new Department();
		department.setId(rs.getInt("DepartmentId"));
		department.setName(rs.getString("DepName"));
		
		return department;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			stmt = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"ORDER BY Name"
					);
		
			rs = stmt.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while( rs.next() ) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if( dep == null ) {
				 dep = departmentFunction(rs);
				 map.put(rs.getInt("DepartmentId"), dep);
					
				}
				
				Seller seller = sellerFunction(rs, dep);
				
				list.add(seller);
			}

			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
	} 

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name"
					);
			
			stmt.setInt(1, department.getId());
			
			rs = stmt.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while ( rs.next() ) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if( dep == null ) {
					
					dep = departmentFunction(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = sellerFunction(rs, dep);
				
				list.add(seller);
			
			}
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
		
	}

}
