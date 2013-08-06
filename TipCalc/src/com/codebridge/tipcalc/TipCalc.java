package com.codebridge.tipcalc;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;


public class TipCalc extends Activity {
	
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	
	private double billBeforeTip;
	
	private double tipAmount;
	
	private double finalBill;

	EditText billBeforeTipET;
	EditText tipAmountET;
	EditText finalBillET;

	// Sum of all radio buttons and check boxes
	
	private int[] checklistValues = new int[12]; 
		
	// Declare CheckBoxes
		
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
		
	// Declare RadioButtons
		
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadio;
	RadioButton availableOKRadio;
	RadioButton availableGoodRadio;
		
	// Declare Spinner (Drop Down Box)
		
	Spinner problemsSpinner;
		
	// Declare Buttons
		
	Button startChronometerButton;
	Button pauseChronometerButton;
	Button resetChronometerButton;
		
	// Declare Chronometer
		
	Chronometer timeWaitingChronometer;
		
	// The number of seconds you spent 
	// waiting for the waitress
		
	long secondsYouWaited = 0;
		
	// TextView for the chronometer
		
	TextView timeWaitingTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);
        
        //Check to see if we are just starting or recovering from a restart 
        // and populate fields if there is data from previous use.
        
        if(savedInstanceState == null) {
        	
        	billBeforeTip = 0.0;
        	tipAmount = .15;
        	finalBill =0.0;
        } else {
        	
        	billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
        	tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
        	finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        	
        }
        
        //Initialize text fields / cast to object type and get by Id.
        
        billBeforeTipET = (EditText) findViewById(R.id.billEditText);
    	tipAmountET = (EditText) findViewById(R.id.tipEditText);
    	finalBillET = (EditText) findViewById(R.id.finalBillEditText);
    	
    	//Initialize seek bar and add a change listener
    	
    	tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);
		
		tipSeekBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) tipSeekBarListener);
    	
    	// Setup a change Listener
    	
    	billBeforeTipET.addTextChangedListener(billBeforeTipListener);
    	
    	// Initialize CheckBoxs
		
 		friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
 		specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
 		opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);
    			
 		setUpIntroCheckBoxes(); // Add change listeners to check boxes
    			
 		// Initialize RadioButtons
    			
 		availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
 		availableOKRadio = (RadioButton) findViewById(R.id.availableOKRadio);
 		availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);
    			
 		// Initialize RadioGroups
    			
 		availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
    			
 		// Add ChangeListener To Radio buttons
    			
 		addChangeListenerToRadios();
    			
 		// Initialize the Spinner
    			
 		problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);
    			
 		problemsSpinner.setPrompt("Problem Solving");
    			
    	// Add ItemSelectedListener To Spinner
 			
 		addItemSelectedListenerToSpinner();
    			
 		// Initialize Buttons
    			
    	startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
    	pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
    	resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
    			
    	// Add setOnClickListeners for buttons
    			
    	setButtonOnClickListeners();
    			
    	// Initialize Chronometer
    			
    	timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
    			
    	// TextView for Chronometer
    			
    	timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
    	
    }
    
private TextWatcher billBeforeTipListener = new TextWatcher(){

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
		
		try{
			
			billBeforeTip = Double.parseDouble(s.toString());
		}
		
		catch (NumberFormatException e){
			
			billBeforeTip = 0.0;
		}
		
		updateTipAndFinalBill();
	}
	
};

	private void updateTipAndFinalBill() {
		
		double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
		
		double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
		
		finalBillET.setText(String.format("%.02f", finalBill));
		
	}
		// if devise status changes call the code block below so we save our ET values
	
	
	protected void onSaveInstanceState(Bundle outstate){
		
		
		super.onSaveInstanceState(outstate);
		
		outstate.putDouble(TOTAL_BILL, finalBill);
		outstate.putDouble(CURRENT_TIP, tipAmount);
		outstate.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
		
	}

	// SeekBar used to make a custom tip
	
	private SeekBar tipSeekBar;
	
	private OnSeekBarChangeListener tipSeekBarListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			// Get the value set on the SeekBar
			
			tipAmount = (tipSeekBar.getProgress()) * .01;
			
			// Set tipAmountET with the value from the SeekBar
			
			tipAmountET.setText(String.format("%.02f", tipAmount));
			
			// Update all the other EditTexts
			
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private void setUpIntroCheckBoxes(){
		
		// Add ChangeListener to the friendlyCheckBox
		
		friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						
				// Use java ternary operator to set the right values for
				// each item on the waitress check box checklist
						
				checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
				
				// Calculate tip using the waitress checklist options
				
				setTipFromWaitressChecklist(); 
						
				// Update all the other EditTexts
						
				updateTipAndFinalBill();
						
			}
					
		});
		
		// Add ChangeListener to the specialsCheckBox
		
		specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						
				// Use java ternary operator to set the right values for
				// each item on the waitress check box checklist
						
				checklistValues[1] = (specialsCheckBox.isChecked())?1:0;
				
				// Calculate tip using the waitress checklist options
				
				setTipFromWaitressChecklist(); 
						
				// Update all the other EditTexts
						
				updateTipAndFinalBill();
						
			}
					
		});		
		
		// Add ChangeListener to the opinionCheckBox
		
		opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						
				// Use java ternary operator to set the right values for
				// each item on the waitress check box checklist
						
				checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
				
				// Calculate tip using the waitress checklist options
				
				setTipFromWaitressChecklist(); 
						
				// Update all the other EditTexts
						
				updateTipAndFinalBill();
						
			}
					
		});				
		
	}
	
	// Calculate tip using the waitress checklist options
	
	private void setTipFromWaitressChecklist(){
		
		int checklistTotal = 0;
		
		// Cycle through all the checklist values to calculate
		// a total amount based on waitress performance
		
		for(int item : checklistValues){
			
			checklistTotal += item;
			
		}
		
		// Set tipAmountET 
		
		tipAmountET.setText(String.format("%.02f", checklistTotal * .01));
		
	}
	
	private void addChangeListenerToRadios(){
		
		// Setting the listeners on the RadioGroups and handling them
		// in the same location
		
		availableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
			// checkedId is the RadioButton selected
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	
	        	// Use java ternary operator to set the right values for
	    		// each item on the waitress radiobutton checklist
	        	
	        	checklistValues[3] = (availableBadRadio.isChecked())?-1:0;
	        	checklistValues[4] = (availableOKRadio.isChecked())?2:0;
	        	checklistValues[5] = (availableGoodRadio.isChecked())?4:0;
	        	
	        	// Calculate tip using the waitress checklist options
				
				setTipFromWaitressChecklist(); 
						
				// Update all the other EditTexts
						
				updateTipAndFinalBill();
	        	
	        }
	    });
		
	}
	
	// Adds Spinner ItemSelectedListener
	
	private void addItemSelectedListenerToSpinner(){
		
		problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				checklistValues[6] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Bad"))?-1:0;
				checklistValues[7] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("OK"))?3:0;
				checklistValues[8] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Good"))?6:0;
				
				// Calculate tip using the waitress checklist options
				
				setTipFromWaitressChecklist(); 
						
				// Update all the other EditTexts
						
				updateTipAndFinalBill();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	// Adds ClickListeners for buttons so they can control
	// the chronometer
	
	private void setButtonOnClickListeners(){
		
		startChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				// Holds the number of milliseconds paused
				
				int stoppedMilliseconds = 0;
				
				// Get time from the chronometer
				
			    String chronoText = timeWaitingChronometer.getText().toString();
			    String array[] = chronoText.split(":");
			    if (array.length == 2) {
			    	
			    	// Find the seconds
			    	
			      stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
			            + Integer.parseInt(array[1]) * 1000;
			    } else if (array.length == 3) {
			    	
			    	// Find the minutes
			    	
			      stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 
			            + Integer.parseInt(array[1]) * 60 * 1000
			            + Integer.parseInt(array[2]) * 1000;
			    }
			    
			    // Amount of time elapsed since the start button was
			    // pressed, minus the time paused

			    timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
			    
			    // Set the number of seconds you have waited
			    // This would be set for minutes in the real world
			    // obviously. That can be found in array[2]
			    
			    secondsYouWaited = Long.parseLong(array[1]);
			    
			    updateTipBasedOnTimeWaited(secondsYouWaited);
			    
			    // Start the chronometer
			    
			    timeWaitingChronometer.start();
				
			}
			
			
		});
		
		pauseChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				timeWaitingChronometer.stop();
				
			}
			
			
		});
		
		resetChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
				
				// Reset milliseconds waited back to 0
				
				secondsYouWaited = 0;
				
			}			
		});
	}
	
	private void updateTipBasedOnTimeWaited(long secondsYouWaited){
		
		// If you spent less then 30 minutes then add 2 to the tip
		// if you spent more then 30 minutes subtract 2
		
		checklistValues[9] = (secondsYouWaited > 1800)?-2:2;
		
		// Calculate tip using the waitress checklist options
		
		setTipFromWaitressChecklist(); 
				
		// Update all the other EditTexts
				
		updateTipAndFinalBill();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip_calc, menu);
		return true;
	}}
	

