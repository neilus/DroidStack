package org.openstack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;

import android.os.Bundle;
import android.os.AsyncTask;

import android.content.Intent;
import android.content.Context;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.Display;

import android.util.Log;

import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;

import android.content.res.Configuration;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Set;

public class main extends Activity implements OnClickListener
{
    private static main ACTIVITY = null;
    private Hashtable<String, OpenStackImage> osimages = null;
    private CustomProgressDialog progressDialogWaitStop = null;
    private int SCREENH = 0;
    private int SCREENW = 0;
    private static boolean downloading_image_list = false;

    /**
     *
     *
     *
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	ACTIVITY = this;
	
	setContentView(R.layout.main);
	
        if( !Utils.internetOn( this ) )
          Utils.alert( "The device is not connected to Internet. This App cannot work.", this );
	  
	progressDialogWaitStop = new CustomProgressDialog( this, ProgressDialog.STYLE_SPINNER );
        progressDialogWaitStop.setMessage( "Please wait: connecting to remote server..." );

	WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        SCREENH = d.getHeight();
	SCREENW = d.getWidth();
    }
    
    /**
     *
     *
     *
     *
     */
    @Override
    public void onDestroy( ) {
      ACTIVITY = null;
      super.onDestroy( );
      progressDialogWaitStop.dismiss();
    }

    /**
     *
     *
     *
     *
     */
    @Override
    public void onResume( ) {
      
      super.onResume( );
      
      // Button login = (Button)findViewById(R.id.LOGIN);
      // Button glance= (Button)findViewById(R.id.GLANCE);
      // Button nova  = (Button)findViewById(R.id.NOVA);
      // int buttonSize = (Utils.getIntegerPreference( "SCREENW", 320, this )-12)/2;
      // Log.d("MAIN.onResume", "SCREENW="+Utils.getIntegerPreference( "SCREENW", 320, this ) + " - ButtonSize="+buttonSize);
      // LinearLayout.LayoutParams lp_l = new LinearLayout.LayoutParams( buttonSize, 200 );
      // login.setLayoutParams(lp_l);
      // glance.setLayoutParams(lp_l);
      // nova.setLayoutParams(lp_l);

      if( !Utils.internetOn( this ) )
        Utils.alert( "The device is not connected to Internet. This App cannot work.", this );
      
      String osimage = Utils.getStringPreference("SELECTED_OSIMAGE", "", this);
      if(osimage.length() != 0) {
      
        String message = "Name: \""+osimage+"\""
	    + "\nSize: "   + osimages.get(osimage).getSize()/1048576 + " MBytes"
	    + "\nFormat: " + osimages.get(osimage).getFormat();
	
        Utils.putStringPreference("SELECTED_OSIMAGE", "", this);
	
	
      }
    }
    
    /**
     *
     *
     *
     *
     */
    public void login( View v ) {
      Class<?> c = (Class<?>)Login.class;
      Intent I = new Intent( main.this, c );
      startActivity( I );
    }
    
    /**
     *
     *
     *
     *
     */
    public void overview( View v ) {
	// Class<?> c = (Class<?>)OverView.class;
	// Intent I = new Intent( main.this, c );
	// startActivity( I );
    }
    
    /**
     *
     *
     *
     *
     */
    public void list_glance( View v ) {
      
      progressDialogWaitStop.show();
      downloading_image_list = true;
      
      long expirationTime = Utils.getLongPreference(   "TOKEN_EXPIRATION", 0, this);
      String endpoint     = Utils.getStringPreference( "LAST_ENDPOINT", "", this);
      String tenant       = Utils.getStringPreference( "LAST_TENANT",   "", this);
      String username     = Utils.getStringPreference( "LAST_USERNAME", "", this);
      String password     = Utils.getStringPreference( "LAST_PASSWORD", "", this);
      boolean usessl      = Utils.getBoolPreference(   "USESSL", true, this);
      if(expirationTime <= Utils.now( )-10 ) {
        
	if( endpoint.length()==0 ||
	    tenant.length()==0 ||
	    username.length()==0 ||
	    password.length()==0 ) 
        {
	  
	  Utils.alert( "The token is expired and you haven't provided yet your credentials. Please press on Login.", this );
	  return;
	      
	} else {
	    String jsonResponse = null;
	    try {
	      jsonResponse = RESTClient.requestToken( endpoint, tenant, username, password, usessl );
	    } catch(IOException e) {
	      Utils.alert(e.getMessage( ), this);
	      return;
	    }
	    try {
		Tenant t = ParseUtils.getToken( jsonResponse );
		Utils.putStringPreference( "TOKEN_STRING", t.getToken(), this );
		Utils.putLongPreference( "TOKEN_EXPIRATION", t.getExpireTime( ), this );
		Utils.putStringPreference("TENANT_ID", t.getTenantID(), this );
	    } catch(ParseException pe) {
		Utils.alert( pe.getMessage( ), this);
		return;
	    }
	}
      }
 
      (new AsyncTaskOSListImages( )).execute("");
      
    }
    
    /**
     *
     *
     *
     *
     */  
    private void showImageList( String jsonBuf ) {
    
	Hashtable<String, OpenStackImage> result = null;
	try {
	    result = ParseUtils.getImages( jsonBuf.toString( ) );
	} catch(ParseException pe) {
	    Utils.alert( pe.getMessage( ), this );
	    return;
	}

	Class<?> c = (Class<?>)ImagesExplore.class;
	Intent I = new Intent( main.this, c );
	ArrayList<String> imageNames = null;
	if(result!=null) {
	    osimages = result;
	    Set<String> keys = result.keySet();
	    Iterator<String> it = keys.iterator();
	    if(keys.isEmpty() == true) {
		Utils.alert("No image", this);
		return;
	    }
	    imageNames = new ArrayList<String>();
	    while( it.hasNext( ) ) {
		String image = it.next();
		imageNames.add( image );
	    }
      
	    if(imageNames.size() > 0) {
		I.putStringArrayListExtra("OSIMAGELIST", imageNames);
		startActivity( I );
	    }
	} 
    } 

    /**
     *
     *
     *
     *
     */    
    public void onClick( View v ) {
        v.requestFocus();
        String name = ((Named)v).getName();
        Utils.alert(name, this);
    }
    
    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    protected class AsyncTaskOSListImages extends AsyncTask<String, String, String>
    {
     	private  String   errorMessage  =  null;
	private  boolean  hasError      =  false;
	private  String   jsonBuf       = null;
	
	protected String doInBackground( String... v ) 
	{
	   //String jsonBuf = null;
	   
   	   try {
             jsonBuf = RESTClient.requestImages( Utils.getStringPreference( "LAST_ENDPOINT", "", ACTIVITY), 
						 Utils.getStringPreference( "TOKEN_STRING", "", ACTIVITY) );
     	   } catch(IOException e) {
	     errorMessage = e.getMessage();
	     hasError = true;
    	     return "";
    	   }
      
    	   return "";
	}
	
	@Override
	    protected void onPreExecute() {
	    super.onPreExecute();
	    
	    downloading_image_list = true;
	}
	
	@Override
	    protected void onProgressUpdate(String... values) {
	    super.onProgressUpdate(values);
	}
	
	@Override
	    protected void onCancelled() {
	    super.onCancelled();
	}
	
	@Override
	    protected void onPostExecute( String result ) {
	    super.onPostExecute(result);
	    
 	    if(hasError) {
 		Utils.alert( errorMessage, ACTIVITY );
 		downloading_image_list = false;
 		ACTIVITY.progressDialogWaitStop.dismiss( );
 		return;
 	    }
	    
	    downloading_image_list = false; // questo non va spostato da qui a
	    ACTIVITY.progressDialogWaitStop.dismiss( );
	    ACTIVITY.showImageList( jsonBuf );
	}
    }
}