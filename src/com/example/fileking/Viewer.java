package com.example.fileking;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
//import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.fileking.R;
import com.example.fileking.library.NewFile;
import com.example.fileking.library.NewFolder;
import com.example.fileking.library.DatabaseFunctions;


public class Viewer extends Activity
{
	String data = "", data2 = "";
	public static boolean[] reverseSort = {false,false,false,false};
	ArrayList<String> files, folders;
	ArrayAdapter<String> fileList, folderList;
	Button newFolder, btnLogout, upload_btn, download_btn, name, type, size, date;
	ListView fileView, folderView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);

		newFolder = (Button) findViewById(R.id.newfolder);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		upload_btn = (Button) findViewById(R.id.btnupload);
		download_btn = (Button) findViewById(R.id.btndownload);
		name = (Button) findViewById(R.id.name);
		type = (Button) findViewById(R.id.type);
		size = (Button) findViewById(R.id.size);
		date = (Button) findViewById(R.id.date);
		fileView = (ListView) findViewById(R.id.files);
		folderView = (ListView) findViewById(R.id.folders);

		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec = tabs.newTabSpec("0");
		spec.setContent(R.id.viewer);
		spec.setIndicator("Viewer");
		tabs.addTab(spec);

		spec = tabs.newTabSpec("1");
		spec.setContent(R.id.manager);
		spec.setIndicator("Manager");
		tabs.addTab(spec);

		newFolder.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final EditText folderName = new EditText(Viewer.this);
				final AlertDialog dialog;
				dialog = new AlertDialog.Builder(Viewer.this).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setView(folderName);
				dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Create Folder", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if(folderName.length() > 0)
						{
							new NewFolder().execute(folderName.getText().toString());
							NewFolder.add(folderName.getText().toString(), folderList);
						}
						dialog.cancel();
					}
				}
				);
				dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.cancel();
					}
				}
				);
				dialog.show();
			}
		}
		);

		upload_btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final EditText fileName = new EditText(Viewer.this);
				final AlertDialog dialog;
				dialog = new AlertDialog.Builder(Viewer.this).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setView(fileName);
				dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Upload File", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if(fileName.length() > 0)
						{
							//new NewFile().execute(fileName.getText().toString());
							NewFile.add(fileName.getText().toString(), fileList);
						}
						dialog.cancel();
					}
				}
				);
				dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.cancel();
					}
				}
				);
				dialog.show();
			}
		}
		);

		download_btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final EditText fileName = new EditText(Viewer.this);
				final AlertDialog dialog;
				dialog = new AlertDialog.Builder(Viewer.this).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setView(fileName);
				dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download File", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if(fileName.length() > 0)
						{
							//new NewFile().execute(fileName.getText().toString());
							NewFile.add(fileName.getText().toString(), fileList);
						}
						dialog.cancel();
					}
				}
				);
				dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.cancel();
					}
				}
				);
				dialog.show();
			}
		}
		);

		name.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final DatabaseFunctions getData = new DatabaseFunctions();
				new Thread(new Runnable()
				{
					public void run()
					{
						data = getData.getItems("uploads/", "file");

						System.out.println(data);

						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								fileList.clear();
								ArrayList<String> files = DatabaseFunctions.parseJSON(data,0,0);
								for(int i = 0; i < files.size(); i++)
								{
									fileList.add(files.get(i));
								}
							}
						}
						);
					}
				}).start();
			}
		}
		);

		type.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final DatabaseFunctions getData = new DatabaseFunctions();
				new Thread(new Runnable()
				{
					public void run()
					{
						data = getData.getItems("uploads/", "file");

						System.out.println(data);

						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								fileList.clear();
								ArrayList<String> files = DatabaseFunctions.parseJSON(data,0,1);
								for(int i = 0; i < files.size(); i++)
								{
									fileList.add(files.get(i));
								}
							}
						}
						);
					}
				}).start();
			}
		}
		);

		size.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final DatabaseFunctions getData = new DatabaseFunctions();
				new Thread(new Runnable()
				{
					public void run()
					{
						data = getData.getItems("uploads/", "file");

						System.out.println(data);

						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								fileList.clear();
								ArrayList<String> files = DatabaseFunctions.parseJSON(data,0,2);
								for(int i = 0; i < files.size(); i++)
								{
									fileList.add(files.get(i));
								}
							}
						}
						);
					}
				}).start();
			}
		}
		);

		date.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				final DatabaseFunctions getData = new DatabaseFunctions();
				new Thread(new Runnable()
				{
					public void run()
					{
						data = getData.getItems("uploads/", "file");

						System.out.println(data);

						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								fileList.clear();
								ArrayList<String> files = DatabaseFunctions.parseJSON(data,0,3);
								for(int i = 0; i < files.size(); i++)
								{
									fileList.add(files.get(i));
								}
							}
						}
						);
					}
				}).start();
			}
		}
		);
		/*
		fileView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
			}
		}
		);

		folderView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
			}
		}
		);
		*/
		files = new ArrayList<String>();
		folders = new ArrayList<String>();
		fileList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,files);
		folderList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,folders);
		fileView.setAdapter(fileList);
		folderView.setAdapter(folderList);
		
		final DatabaseFunctions getData = new DatabaseFunctions();
		new Thread(new Runnable()
		{
			public void run()
			{
				data = getData.getItems("uploads/", "file");

				System.out.println(data);

				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						fileList.clear();
						ArrayList<String> files = DatabaseFunctions.parseJSON(data,0,0);
						for(int i = 0; i < files.size(); i++)
						{
							fileList.add(files.get(i));
						}
					}
				}
				);
			}
		}).start();

		final DatabaseFunctions getData2 = new DatabaseFunctions();
		new Thread(new Runnable()
		{
			public void run()
			{
				data2 = getData2.getItems("uploads/", "folder");
				
				System.out.println(data2);

				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						ArrayList<String> folders = DatabaseFunctions.parseJSON(data2,1,0);
						for(int i = 0; i < folders.size(); i++)
						{
							folderList.add(folders.get(i));
						}
					}
				}
				);
			}
		}).start();
	}
}