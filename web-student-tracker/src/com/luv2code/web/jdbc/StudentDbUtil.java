package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {
		List<Student> students= new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn= dataSource.getConnection();
			
			String sql="select * from student order by last_name";
			
			myStmt=myConn.createStatement();
			myRs=myStmt.executeQuery(sql);	
			
			while(myRs.next()) {
				int id=myRs.getInt("id");
				String first_name=myRs.getString("first_name");
				String last_name=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				Student tempStudent=new Student(id, first_name, last_name,email);
				students.add(tempStudent);
			}
			return students;
			
		}
		finally {
			myConn.close();
		}
		
		
	}

	public void addStudent(Student theStudent) throws Exception {
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
			myConn = dataSource.getConnection();
			String sql= "insert into student"+ "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
						myStmt.setString(1, theStudent.getFirstName());
						myStmt.setString(2, theStudent.getLastName());
						myStmt.setString(3, theStudent.getEmail());
						
						// execute sql insert
						myStmt.execute();
		}
		finally {
			myConn.close();
		}
		
		
	}

	public void deleteStudent(String theStudentId)throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to delete student
			String sql = "delete from student where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute sql statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC code
			myConn.close();
		}
		
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			myConn.close();
		}
		
	}

	public Student getStudent(String theStudentId) throws Exception {
Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "select * from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
			return theStudent;
		}
		finally {
			// clean up JDBC objects
			myConn.close();
		}
	}

}
