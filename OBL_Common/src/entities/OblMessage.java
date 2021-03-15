package entities;

import java.io.Serializable;

public class OblMessage  implements Serializable
{
	private String id;
	private String messageContent;
	private String recipientUserType;
	private String recipientUserId;
	
	public OblMessage(String id, String messageContent, String recipientUserType, String recipientUserId)
	{
		this.id = id;
		this.messageContent = messageContent;
		this.recipientUserType = recipientUserType;
		this.recipientUserId = recipientUserId;
	}

	public OblMessage(String messageContent, String recipientUserType, String recipientUserId)
	{
		this.messageContent = messageContent;
		this.recipientUserType = recipientUserType;
		this.recipientUserId = recipientUserId;
	}

	public OblMessage(String messageContent, String recipientUserType)
	{
		this.messageContent = messageContent;
		this.recipientUserType = recipientUserType;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getMessageContent()
	{
		return messageContent;
	}

	public void setMessageContent(String messageContent)
	{
		this.messageContent = messageContent;
	}

	public String getRecipientUserType()
	{
		return recipientUserType;
	}

	public void setRecipientUserType(String recipientUserType)
	{
		this.recipientUserType = recipientUserType;
	}

	public String getRecipientUserId()
	{
		return recipientUserId;
	}

	public void setRecipientUserId(String recipientUserId)
	{
		this.recipientUserId = recipientUserId;
	}	
}
