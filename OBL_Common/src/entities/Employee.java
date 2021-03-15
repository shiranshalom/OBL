package entities;

import java.io.Serializable;

public class Employee implements Serializable
{
	public String empNumber;
	public String id;
	public String firstName;
	public String lastName;
	public String email;
	public String role;
	public String department;
	
	public Employee(String empNumber, String id, String firstName, String lastName, String email, String role,
			String department)
	{
		super();
		this.empNumber = empNumber;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.department = department;
	}

	public String getEmpNumber()
	{
		return empNumber;
	}

	public void setEmpNumber(String empNumber)
	{
		this.empNumber = empNumber;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getDepartment()
	{
		return department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}
}
