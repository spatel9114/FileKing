package com.example.fileking.library;

public class DatabaseFunctions
{
	public static java.util.ArrayList<String> parseJSON(String result, int choice, int sort)
	{
		java.util.ArrayList<org.json.JSONObject> json_data = new java.util.ArrayList<org.json.JSONObject>();
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		try
		{
			org.json.JSONArray jArray = new org.json.JSONArray(result);
			switch(choice)
			{
				case 0:
					org.json.JSONObject[] sortedFiles = new org.json.JSONObject[jArray.length()];
					for (int i = 0; i < jArray.length(); i++)
					{
						json_data.add(jArray.getJSONObject(i));
						sortedFiles[i] = json_data.get(i);
					}
					switch(sort)
					{
						case 0:
							Sort.sortName(sortedFiles);
							break;
						case 1:
							Sort.sortType(sortedFiles);
							break;
						case 2:
							Sort.sortSize(sortedFiles);
							break;
						case 3:
							Sort.sortDate(sortedFiles);
							break;
					}
					for (int i = 0; i < sortedFiles.length; i++)
						list.add(sortedFiles[i].getString("name"));
					break;
				case 1:
					for (int i = 0; i < jArray.length(); i++)
					{
						org.json.JSONObject json_data1 = jArray.getJSONObject(i);
						list.add(json_data1.getString("name"));
					}
					break;
			}
		}
		catch (org.json.JSONException e)
		{
			android.util.Log.e("log_tag", "Error parsing data " + e.toString()); 
		}
		return list;
	}

	public String getItems(String directory, String choice)
	{
		try
		{
			org.apache.http.client.methods.HttpPost httppost;
			org.apache.http.client.HttpClient httpclient;

			httpclient = new org.apache.http.impl.client.DefaultHttpClient();
			httppost = new org.apache.http.client.methods.HttpPost("http://proj-309-17.cs.iastate.edu/Files/getitems.php");

			java.util.List<org.apache.http.NameValuePair> nameValuePairs = new java.util.ArrayList<org.apache.http.NameValuePair>();
			nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("choice", choice));
			nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("directory", directory));

			httppost.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(nameValuePairs));
			org.apache.http.client.ResponseHandler<String> responseHandler = new org.apache.http.impl.client.BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			return response.trim();
		}
		catch (Exception e)
		{
			System.out.println("ERROR : " + e.getMessage());
			return "error";
		}
	}
}