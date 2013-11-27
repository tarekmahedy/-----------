package tarekmahedy.app.recharger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class OutgoingCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if(bundle!=null){
			String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			if(phonenumber!=null){
				if(phonenumber.startsWith("**")){
					int lens=phonenumber.length();
					if(lens>2){
						phonenumber=phonenumber.substring(2);
						setResultData(checkfornumberformate(phonenumber,context));
					}else {
						setResultData(null);
						
						Intent callIntent = new Intent(context, MainActivity.class);
						context.startActivity(callIntent);
					}
				}
			}
		}

	}

	public String checkfornumberformate(String _number,Context _context){

		//mobinil
		//*102#number#
		//vodafone
		//*858*number#
		//etisalt
		//*556*number#
		//mobily
		//*1400*number#
		//sawa
		//*155*number#
		//return _number;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
		String idnumber = prefs.getString("idnumber","@");
		if(idnumber!="@")idnumber="*"+idnumber;
		else idnumber="";


		TelephonyManager telephonyManager =((TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE));
		String operatorNamev = telephonyManager.getNetworkOperatorName();
		String info="”Ê› Ì „ «·‘Õ‰ ·‘»ﬂ… "+operatorNamev;
		Toast.makeText(_context, info, Toast.LENGTH_LONG).show();
		String operatorName = telephonyManager.getNetworkOperator();
		if (operatorName != null) {

			int mcc = Integer.parseInt(operatorName.substring(0, 3));
			int mnc = Integer.parseInt(operatorName.substring(3));

			if(mcc==602){

				switch(mnc){
				case 1:
					return "*102#"+_number+"#";

				case 2:
					return "*858*"+_number+"#";

				case 3:
					return "*556*"+_number+"#";


				}	

			}
			else if(mcc==420){
				switch(mnc){
				case 3:
					return "*1400*"+_number+idnumber+"#";

				case 1:
					return "*155*"+_number+idnumber+"#";

				case 4:
					return "*141"+idnumber+"*"+_number+"#";


				}	

			}


		}

		return 	_number;

	}



}
