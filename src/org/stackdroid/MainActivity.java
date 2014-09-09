package org.stackdroid;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.stackdroid.R;
import org.stackdroid.utils.Configuration;
import org.stackdroid.utils.Defaults;
import org.stackdroid.utils.User;
import org.stackdroid.utils.Utils;
import org.stackdroid.activities.FloatingIPActivity;
import org.stackdroid.activities.SecGrpActivity;
import org.stackdroid.activities.UsersActivity;
import org.stackdroid.activities.ServersActivity;
import org.stackdroid.activities.OSImagesActivity;
import org.stackdroid.activities.OverViewActivity;
import org.stackdroid.activities.VolumesActivity;

//import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity
{
//    private int SCREENH = 0;
    private int SCREENW = 0;
//    private static boolean downloading_image_list = false;
//    private static boolean downloading_quota_list = false;
//    private static boolean downloading_server_list = false;

    private String selectedUser;

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
        String versionName = null;
        try {
        	versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch(NameNotFoundException e) {
        	versionName="N/A";
        }
        
        Utils.putStringPreference( "VERSIONNAME", versionName, this );
        setContentView(R.layout.main);
        this.setTitle("DroidStack v "+versionName);
        
        //Utils.createDir( getFilesDir( ) + "/DroidStack/users" );
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREENW = dm.widthPixels;
        int density = (int)this.getResources().getDisplayMetrics().density;
        
        Configuration.getInstance().setValue( "DISPLAYDENSITY", ""+density );
    }
    
    /**
     *
     *
     *
     *
     */
    @Override
    public void onDestroy( ) {
      super.onDestroy( );
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
      
      if( !this.isExternalStorageWritable() ) {

    	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	  builder.setMessage( getString(R.string.NOEXTSTORAGEWRITABLE));//"External storage is not writable ! This App cannot work." );
    	  builder.setCancelable(false);
	    
    	  DialogInterface.OnClickListener yesHandler = new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int id) {
    			  finish( );
    		  }
	      };

	      builder.setPositiveButton("OK", yesHandler );
	        
	      AlertDialog alert = builder.create();
	      alert.getWindow( ).setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	      alert.setCancelable(false);
	      alert.setCanceledOnTouchOutside(false);
	      alert.show();
	      return;
      }
      
      if( !Utils.internetOn( this ) ) {

    	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	  builder.setMessage( getString(R.string.NOINTERNETCONNECTION) );
    	  builder.setCancelable(false);
	    
    	  DialogInterface.OnClickListener yesHandler = new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int id) {
    			  finish( );
    		  }
	      };

	      builder.setPositiveButton("OK", yesHandler );
	        
	      AlertDialog alert = builder.create();
	      alert.getWindow( ).setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	      alert.setCancelable(false);
	      alert.setCanceledOnTouchOutside(false);
	      alert.show();
	      return;
      }
      
      File file = new File(Environment.getExternalStorageDirectory() + "/DroidStack" );
      file.mkdirs( );
      Configuration.getInstance().setValue( "FILESDIR", file.getPath() );
      (new File(Environment.getExternalStorageDirectory() + "/DroidStack/users" )).mkdirs( );
      selectedUser = Utils.getStringPreference("SELECTEDUSER", "", this);
      if(selectedUser.length()!=0) {  

    	  try {
    		  User u = User.fromFileID( selectedUser, Configuration.getInstance().getValue("FILESDIR",Defaults.DEFAULTFILESDIR) );
	      
    		  ((TextView)findViewById(R.id.selected_user)).setText(getString(R.string.SELECTEDUSER)+": "+u.getUserName() + " (" + u.getTenantName() + ")"); 
    	  } catch(Exception e) {
    		  Utils.alert("ERROR: "+e.getMessage(), this );
    		  return;
    	  }
      } else {
    	  ((TextView)findViewById(R.id.selected_user)).setText(getString(R.string.SELECTEDUSER)+": "+getString(R.string.NONE)); 
      }
    }
    
    /**
     *
     *
     *
     *
     */
    public void login( View v ) {
      Class<?> c = (Class<?>)UsersActivity.class;
      Intent I = new Intent( MainActivity.this, c );
      startActivity( I );
    }
    
    /**
     *
     *
     *
     *
     */
    public void overview( View v ) {
	if(selectedUser.length()==0) {
    	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
    	    return;
    	}
	Class<?> c = (Class<?>)OverViewActivity.class;
	Intent I = new Intent( MainActivity.this, c );
	startActivity( I );
    }

    /**
     *
     *
     *
     *
     */
    public void glance( View v ) {
    	if(selectedUser.length()==0) {
    	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
    	    return;
    	}
	Class<?> c = (Class<?>)OSImagesActivity.class;
 	Intent I = new Intent( MainActivity.this, c );
	startActivity(I);
    }
    

    /**
     *
     *
     *
     *
     */
    public void floatingip( View v ) {
    	if(selectedUser.length()==0) {
    	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
    	    return;
    	}
	    Class<?> c = (Class<?>)FloatingIPActivity.class;
 	    Intent I = new Intent( MainActivity.this, c );
	    startActivity(I);
    }
    
    /**
     *
     *
     *
     *
     */
    public void nova( View v ) {
	if(selectedUser.length()==0) {
	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
	    return;
	}

	Class<?> c = (Class<?>)ServersActivity.class;
	Intent I = new Intent( MainActivity.this, c );
	startActivity(I);
	
    }

    /**
     *
     *
     *
     *
     */
    public void secgroups( View v ) {
	  //Utils.alert("NOTIMPLEMENTED", this);
    	if(selectedUser.length()==0) {
    	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
    	    return;
    	}
    	Class<?> c = (Class<?>)SecGrpActivity.class;
    	Intent I = new Intent( MainActivity.this, c );
    	startActivity(I);
    }

    /**
     *
     *
     *
     *
     */
    public void volumes( View v ) {
    	//Utils.alert(getString(R.string.NOTIMPLEMENTED), this);
    	if(selectedUser.length()==0) {
    	    Utils.alert( getString(R.string.NOUSERSELECTED) , this);
    	    return;
    	}
    	Class<?> c = (Class<?>)VolumesActivity.class;
    	Intent I = new Intent( MainActivity.this, c );
    	startActivity(I);
    }

    /**
     *
     *
     *
     *
     */
    public void neutron( View v ) {
    	Utils.alert(getString(R.string.NOTIMPLEMENTED), this);
    }

    /**
     *
     *
     *
     *
     */
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
