package com.example.fileking.library;

public class NewFolder extends android.os.AsyncTask<String, Void, Void>
{
	public static void add(String name, android.widget.ArrayAdapter<String> folderList)
	{
		folderList.add(name);
		folderList.notifyDataSetChanged();
	}

	@Override
	protected Void doInBackground(String... name)
	{
		// Create a new HttpClient and Post Header
		org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
		org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost("http://proj-309-17.cs.iastate.edu/Files/addfolder.php");

		try
		{
			// Add your data
			java.util.List<org.apache.http.NameValuePair> nameValuePairs = new java.util.ArrayList<org.apache.http.NameValuePair>();
			nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("folder_name", name[0]));

			httppost.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.client.ResponseHandler<String> responseHandler = new org.apache.http.impl.client.BasicResponseHandler();
			httpclient.execute(httppost, responseHandler);
		}
		catch (org.apache.http.client.ClientProtocolException e){}
		catch (java.io.IOException e){}
		return null;
	}
}