package com.wch.crm.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.wch.crm.domain.Customer;

@Transactional
public class CustomerServiceImpl implements ICustomerService {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Customer> findAll(){
		/*String sql = "select * from t_customer";
		List<Customer> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Customer.class));*/
		String sql = "select * from t_customer";
		List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>(){
			public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
				int id = rs.getInt("id");//根据字段名称从结果集中获取对应的值
				String name = rs.getString("name");
				String station = rs.getString("station");
				String telephone = rs.getString("telephone");
				String address = rs.getString("address");
				String decidedzone_id = rs.getString("decidedzone_id");
				return new Customer(id, name, station, telephone, address, decidedzone_id);
			}
		});
		return list;
	}

	public List<Customer> findListNotAssociation() {
		String sql = "select * from t_customer where decidedzone_id is null";
		List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>(){
			public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
				int id = rs.getInt("id");//根据字段名称从结果集中获取对应的值
				String name = rs.getString("name");
				String station = rs.getString("station");
				String telephone = rs.getString("telephone");
				String address = rs.getString("address");
				String decidedzone_id = rs.getString("decidedzone_id");
				return new Customer(id, name, station, telephone, address, decidedzone_id);
			}
		});
		return list;
	}

	public List<Customer> findListHasAssociation(String decidedzoneId) {
		String sql = "select * from t_customer where decidedzone_id = ? ";
		List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>(){
			public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
				int id = rs.getInt("id");//根据字段名称从结果集中获取对应的值
				String name = rs.getString("name");
				String station = rs.getString("station");
				String telephone = rs.getString("telephone");
				String address = rs.getString("address");
				String decidedzone_id = rs.getString("decidedzone_id");
				return new Customer(id, name, station, telephone, address, decidedzone_id);
			}
		},decidedzoneId);
		return list;
	}

	public void assigncustomerstodecidedzone(String decidedzoneId, Integer[] customerIds) {
		String sql1 = "update t_customer set decidedzone_id = null where decidedzone_id = ? ";
		String sql2 = "update t_customer set decidedzone_id = ? where id = ? ";
		jdbcTemplate.update(sql1, decidedzoneId);
		if (customerIds != null && customerIds.length > 0) {
			final String id = decidedzoneId;
			final List<Integer> list = Arrays.asList(customerIds);
			jdbcTemplate.batchUpdate(sql2,new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, id);
					ps.setInt(2, list.get(i));
				}
				
				public int getBatchSize() {
					return list.size();
				}
			});
		}
	}
}
