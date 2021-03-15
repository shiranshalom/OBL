package entities;

public class Subscriber extends User
{
	private String subscriberNumber;
	private String phoneNumber;
	private String email;
	private String status;
	private String isGraduatedStatus;
	
	public Subscriber(String subscriberNum, String id, String phone, String email, String status)
	{
		super();
		  subscriberNumber = subscriberNum;
		  this.idNumber = id;
		  this.phoneNumber = phone;
		  this.email = email;
		  this.status = status;
	}

	public Subscriber(String uName, String pass, String idNum, String first, String last, String phone, String mail,
			String subsNumber,String status)
	{
		super(uName, pass, idNum, first, last);
		this.subscriberNumber = subsNumber;
		this.status = status;
		this.email = mail;
		this.phoneNumber = phone;
	}

	/*public Subscriber(String uName, String pass, String idNum, String first, String last,String status)
	{
		super(uName, pass, idNum, first, last);
		status = "active";
		email = null;
		phoneNumber = null;
	}
	/*public Subscriber(String subscriberNumber,String phoneNumber,String email,String status ,String userName,String idNumber,String firstName,String lastName) //tal cons
	{
		super(userName,idNumber,firstName,lastName);
		this.subscriberNumber=subscriberNumber;
		this.phoneNumber=phoneNumber;
		this.email=email;
		this.status=status;

	}*/
	
	public Subscriber (String idNumber,String firstName,String lastName,String phoneNumber,String email,String status)  //only update information constructor
	{
		super(idNumber,firstName,lastName);
		this.phoneNumber=phoneNumber;
		this.email=email;
		this.status = status;
	}
		
	public Subscriber(String id)
	{
		super();
		  this.idNumber = id;
	}
	
	public String getSubscriberNumber()
	{
		return subscriberNumber;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public String getEmail()
	{
		return email;
	}

	public String getStatus()
	{
		return status;
	}


	public void setSubscriberNumber(String subscriberId)
	{
		this.subscriberNumber = subscriberId;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getIsGraduatedStatus() 
	{
		return isGraduatedStatus;
	}

	public void setIsGraduatedStatus(String isGraduatedStatus) 
	{
		this.isGraduatedStatus = isGraduatedStatus;
	}

	public void FillInformationFromUser(User userToCheck)
	{
		this.userName = userToCheck.userName;
		this.password = userToCheck.password;
		this.idNumber = userToCheck.idNumber;
		this.firstName = userToCheck.firstName;
		this.lastName = userToCheck.lastName;
		this.loginStatus = userToCheck.loginStatus;
		this.type = userToCheck.type;
	}
}
