package entities;

import java.io.Serializable;

public class Report_Activity implements Serializable
{
	private String reportDate;
	private int totalNumberOfSubscribers = 0;
	private int activeSubscribersNumber = 0;
	private int lockedSubscribersNumber = 0;
	private int frozenSubscribersNumber = 0;
	private int currentNumOfBorrows = 0;
	private int numOfLateSubscribers = 0;
	
	public Report_Activity(String reportDate, int totalNumberOfSubscribers, int activeSubscribersNumber,
			int lockedSubscribersNumber, int frozenSubscribersNumber, int currentNumOfBorrows, int numOfLateSubscribers)
	{
		this.reportDate = reportDate;
		this.totalNumberOfSubscribers = totalNumberOfSubscribers;
		this.activeSubscribersNumber = activeSubscribersNumber;
		this.lockedSubscribersNumber = lockedSubscribersNumber;
		this.frozenSubscribersNumber = frozenSubscribersNumber;
		this.currentNumOfBorrows = currentNumOfBorrows;
		this.numOfLateSubscribers = numOfLateSubscribers;
	}

	public String getReportDate()
	{
		return reportDate;
	}

	public void setReportDate(String reportDate)
	{
		this.reportDate = reportDate;
	}

	public int getTotalNumberOfSubscribers()
	{
		return totalNumberOfSubscribers;
	}

	public void setTotalNumberOfSubscribers(int totalNumberOfSubscribers)
	{
		this.totalNumberOfSubscribers = totalNumberOfSubscribers;
	}

	public int getActiveSubscribersNumber()
	{
		return activeSubscribersNumber;
	}

	public void setActiveSubscribersNumber(int activeSubscribersNumber)
	{
		this.activeSubscribersNumber = activeSubscribersNumber;
	}

	public int getLockedSubscribersNumber()
	{
		return lockedSubscribersNumber;
	}

	public void setLockedSubscribersNumber(int lockedSubscribersNumber)
	{
		this.lockedSubscribersNumber = lockedSubscribersNumber;
	}

	public int getFrozenSubscribersNumber()
	{
		return frozenSubscribersNumber;
	}

	public void setFrozenSubscribersNumber(int frozenSubscribersNumber)
	{
		this.frozenSubscribersNumber = frozenSubscribersNumber;
	}

	public int getCurrentNumOfBorrows()
	{
		return currentNumOfBorrows;
	}

	public void setCurrentNumOfBorrows(int currentNumOfBorrows)
	{
		this.currentNumOfBorrows = currentNumOfBorrows;
	}

	public int getNumOfLateSubscribers()
	{
		return numOfLateSubscribers;
	}

	public void setNumOfLateSubscribers(int numOfLateSubscribers)
	{
		this.numOfLateSubscribers = numOfLateSubscribers;
	}
	

}
