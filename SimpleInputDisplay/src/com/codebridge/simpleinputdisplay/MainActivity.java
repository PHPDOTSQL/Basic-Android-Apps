package com.codebridge.simpleinputdisplay;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	// Store a unique message using your package name to avoid conflicts
	// with other apps. This stores the message I plan on displaying
	public final static String EXTRA_MESSAGE = "com.codebridge.simpleinputdisplay.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// This method is called when the user clicks on the Send button
	
	public void sendMessage(View view){
		
		// An intent is an object that can be used to start another activity
		// this is a reference to MainActivity
		// MainActivity is of type Activity, which is of type Context.
		// A Context can represent various application specific 
		// resources. Different resources gain access to others because
		// of shared methods inherited from the Context class.
		
		// DisplayMessageActivity is an Activity we want to start
		
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		
		// Create a text area and place it in the view
		EditText editText = (EditText) findViewById(R.id.edit_message);
		
		// Store the text in the text area
		String message = editText.getText().toString();
		
		// Add the text to the intent
		intent.putExtra(EXTRA_MESSAGE, message);
		
		// startActivity causes the Activity to start
		startActivity(intent);
		
	}

}