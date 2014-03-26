package com.example.fileking;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpEntity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fileking.R;
import com.example.fileking.library.UserFunctions;

@SuppressWarnings("unused")
public class DashboardActivity extends Activity {
	private static final int SELECT_FILE = 1;
	HttpEntity resEntity;
	UserFunctions userFunctions;
	Button btnLogout;
	Dialog dialog;
	TextView cur_val;
	WebView webview;

	// Instance variables related to uploading
	TextView upload_tv;
	Button upload_btn;
	Button btnChoose;
	int serverResponseCode = 0;
	ProgressDialog upload_dialog = null;
	Button openUrl;

	// Instance variables related to downloading
	EditText path_input;
	TextView download_tv;
	ProgressBar pb;
	Button download_btn;
	Dialog download_dialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView down_cur_val;
	String download_file_path = "http://proj-309-17.cs.iastate.edu/Files/uploads/"; //TODO change this to new server
	String upload_file_path = "NONE";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		/**
		 * Dashboard Screen for the application
		 * */
		// Check login status in database
		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			setContentView(R.layout.dashboard);
			
            
			//transfer.ftpChangeDirectory("Files/uploads/");
			openUrl = (Button) findViewById(R.id.server);
			btnChoose = (Button) findViewById(R.id.chooser);
			btnLogout = (Button) findViewById(R.id.btnLogout);
			upload_btn = (Button) findViewById(R.id.btnupload);
			download_btn = (Button) findViewById(R.id.btndownload);
			upload_tv = (TextView) findViewById(R.id.upload_pathname);
			upload_tv.setText(upload_tv.getText() + upload_file_path);
			download_tv = (TextView) findViewById(R.id.download_path);
			path_input = (EditText) findViewById(R.id.download_path);
			
			

			btnLogout.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					userFunctions.logoutUser(getApplicationContext());
					Intent login = new Intent(getApplicationContext(),
							LoginActivity.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
			});

			// Code related to pressing the upload button
			upload_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					upload_dialog = ProgressDialog.show(DashboardActivity.this,
							"", "Uploading file...", true);
					new Thread(new Runnable() {
						public void run() {
							runOnUiThread(new Runnable() {
								public void run() {
									upload_tv.setText("uploading started.....");
								}
							});
							int response = uploadFile(upload_file_path);
							System.out.println("RES : " + response);
						}
					}).start();
				}
			});

			// Opens gallery of files to choose from on the device
			btnChoose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openGallery(SELECT_FILE);
				}
			});
			
	        openUrl.setOnClickListener(new OnClickListener() {
	 
	            public void onClick(View view) {                
	                Uri uri = Uri.parse("http://proj-309-17.cs.iastate.edu/Files/uploads/"); //TODO change accordingly
	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	                startActivity(intent);
	            }
	        });

			// Code related to pressing the download button

			download_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					download_file_path = download_file_path + download_tv.getText().toString();
					showProgress(download_file_path);
					new Thread(new Runnable() {
			            public void run() {
			            	 downloadFile();
			            	 download_file_path = "http://proj-309-17.cs.iastate.edu/Files/uploads/"; //TODO change accordingly
			            }
			          }).start();

				}
			});
			
			

		} else {
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(),
					LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}

	}

	public void openGallery(int req_code) {

		Intent intent = new Intent();
		intent.setType("*/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent, "Select file to upload "),
				req_code);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
			if (requestCode == SELECT_FILE) {
				upload_file_path = getPath(selectedImageUri);
				System.out.println("File Path : " + upload_file_path);
			}
			upload_tv.setText("Selected File paths : " + upload_file_path);
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 
	 * method executes code that uploads a file to the web server.
	 * 
	 * @param sourceFileUri
	 *            sourceFileUri is a string of the files path on the android
	 *            device. This value is edited by the user when they pick a file
	 *            with the file chooser intent.
	 * @return
	 */
	public int uploadFile(String sourceFileUri) {
		String upLoadServerUri = "http://proj-309-17.cs.iastate.edu/Files/upload.php"; //TODO change accordingly
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return 0;
		}
		try { // open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
																// connection to
																// the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName);
			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available(); // create a buffer of
															// maximum size

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			if (serverResponseCode == 200) {
				runOnUiThread(new Runnable() {
					public void run() {
						upload_tv.setText("File Upload Completed.");
						Toast.makeText(DashboardActivity.this,
								"File Upload Complete.", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			// close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			upload_dialog.dismiss();
			ex.printStackTrace();
			Toast.makeText(DashboardActivity.this, "MalformedURLException",
					Toast.LENGTH_SHORT).show();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			upload_dialog.dismiss();
			e.printStackTrace();
			Toast.makeText(DashboardActivity.this,
					"Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("Upload file to server Exception",
					"Exception : " + e.getMessage(), e);
		}
		upload_dialog.dismiss();
		return serverResponseCode;
	}

	public void downloadFile(){
		 try {
	            URL url = new URL(download_file_path);
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	 
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setDoOutput(true);
	 
	            //connect
	            urlConnection.connect();
	 
	            //set the path where we want to save the file           
	            File SDCardRoot = Environment.getExternalStorageDirectory(); 
	            //create a new file, to save the downloaded file 
	            File file = new File(SDCardRoot,download_tv.getText().toString());
	  
	            FileOutputStream fileOutput = new FileOutputStream(file);
	 
	            //Stream used for reading the data from the internet
	            InputStream inputStream = urlConnection.getInputStream();
	 
	            //this is the total size of the file which we are downloading
	            totalSize = urlConnection.getContentLength();
	 
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    pb.setMax(totalSize);
	                }               
	            });
	             
	            //create a buffer...
	            byte[] buffer = new byte[1024];
	            int bufferLength = 0;
	 
	            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                fileOutput.write(buffer, 0, bufferLength);
	                downloadedSize += bufferLength;
	                // update the progressbar //
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        pb.setProgress(downloadedSize);
	                        float per = ((float)downloadedSize/totalSize) * 100;
	                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
	                    }
	                });
	            }
	            //close the output stream when complete //
	            fileOutput.close();
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    // pb.dismiss(); // if you want close it..
	                }
	            });         
	         
	        } catch (final MalformedURLException e) {
	            showError("Error : MalformedURLException " + e);        
	            e.printStackTrace();
	        } catch (final IOException e) {
	            showError("Error : IOException " + e);          
	            e.printStackTrace();
	        }
	        catch (final Exception e) {
	            showError("Error : Please check your internet connection " + e);
	        }       
		
	}
	
	public void showError(final String err){
		runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(DashboardActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
	}
	
	//Add resources for progress bar and look at dialog/cur_val indepth
	public void showProgress(String file_path){
        dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
 
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();
         
        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));  
    }
	
	public class myMenu extends Activity {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	    }
	}
	 
	public boolean onCreateOptionsMenu(Menu menu) {
	  super.onCreateOptionsMenu(menu);
	  menu.add(0,0,0,"View Server");
	  menu.add(0,1,0,"Help");
	  return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		  super.onOptionsItemSelected(item);
		  return false;
	}
	 

}