package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student 
{
	
	public SimpleStringProperty studentName;
	public SimpleIntegerProperty studentID;
	public SimpleStringProperty StatusMembership;
	
	
	public Student(String name, int id, String StatusMembership)
	{
		this.studentName = new SimpleStringProperty(name);
		this.studentID =new SimpleIntegerProperty(id);
		this.StatusMembership = new SimpleStringProperty(StatusMembership);
	}
	   
	public String getStudentName() 
	{
		return studentName.get();
	}

	public void setStudentName(String studentName) 
	{
		this.studentName = new SimpleStringProperty(studentName);
	}

	public int getStudentID() 
	{
		return studentID.get();
	}

	public void setStudentID(int ID) 
	{
		this.studentID = new SimpleIntegerProperty(ID);
	}

	public String getStatusMembership() 
	{
		return StatusMembership.get();
	}

	public void setStatusMembership(String statusMembership)
	{
		this.StatusMembership = new SimpleStringProperty(statusMembership);
	}	   
}
