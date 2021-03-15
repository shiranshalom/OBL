package entities;

import java.io.Serializable;
import java.util.Map;

public class Report_LateReturns implements Serializable
{
	private Map<String, NumberAndDurationOfLates> bookToNumberAndDurationOfLates;

	
	
	
	public Map<String, NumberAndDurationOfLates> getBookToNumberAndDurationOfLates()
	{
		return bookToNumberAndDurationOfLates;
	}

	public void setBookToNumberAndDurationOfLates(Map<String, NumberAndDurationOfLates> bookToNumberAndDurationOfLates)
	{
		this.bookToNumberAndDurationOfLates = bookToNumberAndDurationOfLates;
	}

	public class NumberAndDurationOfLates implements Serializable
	{
		private int numberOfLates;
		private float avarageNumberOfLates;
		private int durationOfLates;


		public NumberAndDurationOfLates() {}
		public NumberAndDurationOfLates(int numberOfLates, int durationOfLates, float avarageNumberOfLates)
		{
			this.setNumberOfLates(numberOfLates);
			this.setDurationOfLates(durationOfLates);
			this.setAvarageNumberOfLates(avarageNumberOfLates);
		}

		public int getDurationOfLates()
		{
			return durationOfLates;
		}

		public void setDurationOfLates(int durationOfLates)
		{
			this.durationOfLates = durationOfLates;
		}

		public float getAvarageNumberOfLates()
		{
			return avarageNumberOfLates;
		}

		public void setAvarageNumberOfLates(float avarageNumberOfLates)
		{
			this.avarageNumberOfLates = avarageNumberOfLates;
		}

		public int getNumberOfLates()
		{
			return numberOfLates;
		}

		public void setNumberOfLates(int numberOfLates)
		{
			this.numberOfLates = numberOfLates;
		}

	}

}
