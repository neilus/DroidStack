package org.stackdroid.utils;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 3000000000000000003L;

    private String  userName;
    private String  userID;
    private String  tenantName;
    private String  tenantId;
    private String  token;
    private long    tokenExpireTime;
    private String  password;
    private boolean usessl;
    private boolean role_admin;
    
    private boolean hasGlance;
    private boolean hasNova;
    private boolean hasNeutron;
    private boolean hasCinder1;
    private boolean hasCinder2;
    
    private String identityEndpoint;
    private String novaEndpoint;
    private String glanceEndpoint;
    private String neutronEndpoint;
    private String cinder1Endpoint;
    private String cinder2Endpoint;
    private String identityHostname;
    //private URL    identityUrl;
    
    public User( String _userName, 
    			 String _userID, 
    			 String _tenantName, 
    			 String _tenantId, 
    			 String _token, 
    			 long _tokenExpireTime, 
    			 boolean _role_admin,
    			 boolean hasGlance,
    			 boolean hasNova,
    			 boolean hasNeutron,
    			 boolean hasCinder1,
    			 boolean hasCinder2,
    			 String identityEndpoint,
    			 String glanceEndpoint,
    			 String novaEndpoint,
    			 String neutronEndpoint,
    			 String cinder1Endpoint,
    			 String cinder2Endpoint,
    			 String identityHostname) 
    {
        userName        = _userName;
        userID          = _userID;
        tenantName      = _tenantName;
        tenantId        = _tenantId;
        token           = _token;
        tokenExpireTime = _tokenExpireTime;
        password        = "";
        role_admin      = _role_admin;
        this.hasGlance  = hasGlance;
        this.hasNova    = hasNova;
        this.hasNeutron = hasNeutron;
        this.hasCinder1 = hasCinder1;
        this.hasCinder2 = hasCinder2;
        this.identityEndpoint 	= identityEndpoint;
        this.glanceEndpoint 	= glanceEndpoint;
        this.novaEndpoint   	= novaEndpoint;
        this.neutronEndpoint	= neutronEndpoint;
        this.cinder1Endpoint	= cinder1Endpoint;
        this.cinder2Endpoint	= cinder2Endpoint;
        this.identityHostname   = identityHostname;
        
/*        try {
			identityUrl	= new URL(identityEndpoint);
		} catch (MalformedURLException e) {
		}*/
    }
    
    public String getIdentityHostname( ) { 
    	//InetAddress addr = InetAddress.getByName("192.168.190.62");
    	  //String host = addr.getHostName();
    	//try {
		//	return InetAddress.getByName(identityUrl.getHost()).getHostName();
		//} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			return identityHostname;
		//} 
    }
    
    public void setPassword( String _password ) { password = _password ;} 
    //public void setAuthEndpoint( String ep ) { endpoint = ep; }
    public void setSSL( boolean _usessl ) { usessl = _usessl; }
    
    //public String getEndpoint( ) { return endpoint; }
    public String getTenantName( ) { return tenantName; }
    public String getTenantID( ) { return tenantId; }
    public String getToken( ) { return token; }
    public long   getTokenExpireTime( ) { return tokenExpireTime; }
    public String getUserName( ) { return userName; }
    public String getUserID( ) { return userID; }
    public String getPassword( ) { return password; }
    public boolean useSSL( ) { return usessl; }
    public boolean isRoleAdmin( ) { return role_admin; }
    
    public boolean hasNova( ) { return hasNova; }
    public boolean hasGlance( ) { return hasGlance; }
    public boolean hasNeutron( ) { return hasNeutron; }
    public boolean hasCinder1( ) { return hasCinder1; }
    public boolean hasCinder2( ) { return hasCinder2; }
     
    public String getIdentityEndpoint( ) { return identityEndpoint; }
    public String getNovaEndpoint( ) { return novaEndpoint; }
    public String getGlanceEndpoint( ) { return glanceEndpoint; }
    public String getNeutronEndpoint( ) { return neutronEndpoint; }
    public String getCinder1Endpoint( ) { return cinder1Endpoint; }
    public String getCinder2Endpoint( ) { return cinder2Endpoint; }
    
    public String getFilename( ) {
    	String filename = getUserID( );
    	filename += "."+getTenantID( );
    	filename += "."+identityEndpoint.hashCode();
    	return filename;//getUserID( ) + "." + getTenantID( ) + "." + identityEndpoint.hashCode();
    }
    
    /*
     * 
     * 
     * 
     * 
     * 
     */
    @Override
    public String toString( ) {
    	return "User{identityEndpoint="+identityEndpoint+
    	",novaEndpoint="+novaEndpoint+
    	",glanceEndpoint="+glanceEndpoint+
    	",neutronEndpoint="+neutronEndpoint+
    	",cinder1Endpoint="+cinder1Endpoint+
    	",cinder2Endpoint="+cinder2Endpoint+
    	",identityHostname="+identityHostname+
	    ",userName="+userName+
	    ",userID="+userID+
	    ",tenantName="+tenantName+
	    ",tenantId="+tenantId+
	    ",tokenExpireTime="+tokenExpireTime+
	    ",password="+password+
	    ",usessl="+usessl+
	    ",role_admin="+role_admin+
	    "}";
    }

    /*
     * 
     * 
     * 
     * 
     * 
     */
    public int compareTo( User u ) {
    	if(u.getUserID()!=this.getUserID())
    		return 1;
    	return 0;
    }

    /*
     * 
     * 
     * 
     * 
     * 
     */
    public static User fromFileID( String ID, String filesDir ) throws IOException, ClassNotFoundException, NotExistingFileException {
    	String filename = filesDir + "/users/" + ID;
    	if(false == (new File(filename)).exists())
    		throw new NotExistingFileException( "File [" + filename + "] doesn't exist" );
    	try {
    		InputStream is = new FileInputStream( filename );
    		ObjectInputStream ois = new ObjectInputStream( is );
    		User U = (User)ois.readObject( );
    		ois.close( );
    		return U;
    	} catch(IOException ioe) {
    		(new File(filename)).delete();
    		
    		if(ioe.getMessage( ).contains("Incompatible class (SUID")) {
    			return null;
    		}
    		
    		throw new IOException( "User.fromFileID.InputStream.readObject: " + ioe.getMessage( ) );
    	} catch(ClassNotFoundException cnfe) {
    		throw new ClassNotFoundException( "User.fromFileID.ObjectInputStream.readObject: " + cnfe.getMessage( ) );
    	}
    }
    
    /*
     * 
     * 
     * 
     * 
     * 
     */
    public void toFile( String filesDir ) throws IOException {
    	String filename = filesDir + "/users/" + getFilename( );// getUserID( ) + "." + getTenantID( ) + "." + endpoint.hashCode();
    	File f = new File( filename );
    	if(f.exists()) f.delete();
    	try {
    		OutputStream os = new FileOutputStream( filename );
    		ObjectOutputStream oos = new ObjectOutputStream( os );
    		oos.writeObject( this );
    		oos.close( );
    	} catch(IOException ioe) {
	    		throw new IOException("User.toFile.OutputStream.write/close: "+ioe.getMessage() );
		}
    }
}
