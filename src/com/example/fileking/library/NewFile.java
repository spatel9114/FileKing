package com.example.fileking.library;

public class NewFile
{
	public static void add(String file, android.widget.ArrayAdapter<String> fileList)
	{
		fileList.add(file);
		fileList.notifyDataSetChanged();
	}
}