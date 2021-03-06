package com.revature.serialization;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Employee implements Serializable {

	private int id;
	private String firstName;
	private String lastName;
	private Department dept;
	private LocalDateTime hireDate;
	private Role role;
	
	public Employee() {
		super();
	}

	public Employee(int id, String firstName, String lastName, Department dept, LocalDateTime hireDate, Role role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dept = dept;
		this.hireDate = hireDate;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public LocalDateTime getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDateTime hireDate) {
		this.hireDate = hireDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dept, firstName, hireDate, id, lastName, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
		return dept == other.dept && Objects.equals(firstName, other.firstName)
				&& Objects.equals(hireDate, other.hireDate) && id == other.id
				&& Objects.equals(lastName, other.lastName) && role == other.role;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dept=" + dept
				+ ", hireDate=" + hireDate + ", role=" + role + "]";
	}
}
