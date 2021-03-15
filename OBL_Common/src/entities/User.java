package entities;

import java.io.Serializable;
import java.util.List;


public class User implements Serializable
{
	protected String userName;
	protected String password;
	protected String idNumber;
	protected String firstName;
	protected String lastName;

	protected String loginStatus;
	protected String type;
	
	protected List<String> messages;

	public User() {}
	public User(String idNum, String name, String pass, String first, String last, String status, String userType)
	{
		this.userName = name;
		this.password = pass;
		this.idNumber = idNum;
		this.firstName = first;
		this.lastName = last;
		this.loginStatus = status;
		this.type = userType;
	}

	public User(String name, String pass)//for checking user exist
	{
		this.userName = name;
		this.password = pass;
	}

	public User(String name, String pass, String idNum, String first, String last)
	{
		this.userName = name;
		this.password = pass;
		this.idNumber = idNum;
		this.firstName = first;
		this.lastName = last;
		this.loginStatus = "off";
	}
	
	public User(String userName,String idNumber,String firstName,String lastName)  //tal cons
	{
		this.userName = userName;
		this.idNumber=idNumber;
		this.firstName = firstName;
		this.lastName=lastName;
	}
	
	public User(String idNumber,String firstName,String lastName)  //tal cons
	{
		this.idNumber=idNumber;
		this.firstName = firstName;
		this.lastName=lastName;
	}

	public String getPassWord()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName =  userName;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLoginStatus()
	{
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus)
	{
		this.loginStatus = loginStatus;
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

	public String getId()
	{
		return idNumber;
	}

	public void setId(String idNum)
	{
		this.idNumber = idNum;
	}
	
	public List<String> getMessages()
	{
		return messages;
	}

	public void setMessages( List<String> msgs)
	{
		this.messages = msgs;
	}
}