package com.example.fileking.library;

import com.example.fileking.Viewer;

public class Sort
{
	public static void sortName(org.json.JSONObject[] files)
	{
		java.util.Arrays.sort(files,fileComparatorName);

		if(Viewer.reverseSort[0])
			Viewer.reverseSort[0] = false;
		else
			Viewer.reverseSort[0] = true;

		Viewer.reverseSort[1] = false;
		Viewer.reverseSort[2] = false;
		Viewer.reverseSort[3] = false;
	}

	public static void sortType(org.json.JSONObject[] files)
	{
		java.util.Arrays.sort(files,fileComparatorType);

		if(Viewer.reverseSort[1])
			Viewer.reverseSort[1] = false;
		else
			Viewer.reverseSort[1] = true;

		Viewer.reverseSort[0] = false;
		Viewer.reverseSort[2] = false;
		Viewer.reverseSort[3] = false;
	}

	public static void sortSize(org.json.JSONObject[] files)
	{
		java.util.Arrays.sort(files,fileComparatorSize);

		if(Viewer.reverseSort[2])
			Viewer.reverseSort[2] = false;
		else
			Viewer.reverseSort[2] = true;

		Viewer.reverseSort[0] = false;
		Viewer.reverseSort[1] = false;
		Viewer.reverseSort[3] = false;
	}

	public static void sortDate(org.json.JSONObject[] files)
	{
		java.util.Arrays.sort(files,fileComparatorDate);

		if(Viewer.reverseSort[3])
			Viewer.reverseSort[3] = false;
		else
			Viewer.reverseSort[3] = true;

		Viewer.reverseSort[0] = false;
		Viewer.reverseSort[1] = false;
		Viewer.reverseSort[2] = false;
	}

	
	private static java.util.Comparator<? super org.json.JSONObject> fileComparatorName = new java.util.Comparator<org.json.JSONObject>()
	{
		public int compare(org.json.JSONObject a, org.json.JSONObject b)
		{
			try
			{
				if(Viewer.reverseSort[0] /*== true*/)
					return String.valueOf(b.getString("name")).compareTo(a.getString("name"));
				return String.valueOf(a.getString("name")).compareTo(b.getString("name"));  
			}
			catch(org.json.JSONException e)
			{
				return 0;
			}
	    }
	};
	
	private static java.util.Comparator<? super org.json.JSONObject> fileComparatorType = new java.util.Comparator<org.json.JSONObject>()
	{
		public int compare(org.json.JSONObject a, org.json.JSONObject b)
		{
			try
			{
				if(Viewer.reverseSort[1] /*== true*/)
					return String.valueOf(b.getString("type")).compareTo(a.getString("type"));
				return String.valueOf(a.getString("type")).compareTo(b.getString("type"));  
			}
			catch(org.json.JSONException e)
			{
				return 0;
			}
	    }
	};
	
	private static java.util.Comparator<? super org.json.JSONObject> fileComparatorSize = new java.util.Comparator<org.json.JSONObject>()
	{
		public int compare(org.json.JSONObject a, org.json.JSONObject b)
		{
			try
			{
				if(Viewer.reverseSort[2] /*== true*/)
					return Long.valueOf(b.getLong("size")).compareTo(a.getLong("size"));
				return Long.valueOf(a.getLong("size")).compareTo(b.getLong("size"));  
			}
			catch(org.json.JSONException e)
			{
				return 0;
			}
	    }
	};
	
	private static java.util.Comparator<? super org.json.JSONObject> fileComparatorDate = new java.util.Comparator<org.json.JSONObject>()
	{
		public int compare(org.json.JSONObject a, org.json.JSONObject b)
		{
			try
			{
				if(Viewer.reverseSort[3] /*== true*/)
					return Long.valueOf(b.getLong("date")).compareTo(a.getLong("date"));
				return Long.valueOf(a.getLong("date")).compareTo(b.getLong("date"));  
			}
			catch(org.json.JSONException e)
			{
				return 0;
			}
	    }
	};
}