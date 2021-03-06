package org.stackdroid.utils;

import android.widget.LinearLayout;
import android.content.Context;

import org.stackdroid.views.FloatingIPView;
import org.stackdroid.views.ListSecGroupView;
import org.stackdroid.views.NetworkListView;
import org.stackdroid.views.RuleView;
import org.stackdroid.views.SecGroupView;
import org.stackdroid.views.UserView;
import org.stackdroid.views.ServerView;
import org.stackdroid.views.OSImageView;
import org.stackdroid.views.NetworkView;
import org.stackdroid.views.VolumeView;

public class LinearLayoutWithView extends LinearLayout implements GetView {

	private RuleView relatedRuleView = null;
    private UserView relatedUserView = null;
    private ServerView relatedServerView = null;
    private VolumeView relatedVolumeView = null;
    private OSImageView relatedOSImageView = null;
    private NetworkView relatedNetworkView = null;
    private ListSecGroupView relatedListSecGroupView = null;
    private FloatingIPView relatedFloatingIPView = null;
	private SecGroupView relatedSecGroupView = null;
	private NetworkListView relatedNetworkListView = null;

    public LinearLayoutWithView( Context ctx, UserView uv ) {
    	super( ctx );
    	relatedUserView = uv;
    }

    public LinearLayoutWithView( Context ctx, ServerView sv ) {
    	super( ctx );
    	relatedServerView = sv;
    }

     public LinearLayoutWithView( Context ctx, OSImageView sv ) {
    	 super( ctx );
    	 relatedOSImageView = sv;
    }   

     public LinearLayoutWithView( Context ctx, NetworkView nv ) {
    	 super( ctx );
   			relatedNetworkView = nv;
    }   
     
     public LinearLayoutWithView( Context ctx, FloatingIPView nv ) {
   	    super( ctx );
     	relatedFloatingIPView = nv;
     }   
        

     public LinearLayoutWithView( Context ctx, ListSecGroupView nv ) {
   	    super( ctx );
     	relatedListSecGroupView = nv;
     }
     
     public LinearLayoutWithView( Context ctx, VolumeView vv ) {
    	    super( ctx );
    	    relatedVolumeView = vv;
     }

     public LinearLayoutWithView( Context ctx, RuleView rv ) {
    	    super( ctx );
    	    relatedRuleView = rv;
     }

     public LinearLayoutWithView(Context ctx, NetworkListView nv ){
     	super( ctx );
    	relatedNetworkListView = nv;
     }
     
     
    @Override
 	public RuleView getRuleView( ) { return relatedRuleView; }
    @Override
 	public UserView getUserView( ) { return relatedUserView; }
    @Override
 	public ServerView getServerView( ) { return relatedServerView; }
    @Override
 	public OSImageView getOSImageView( ) { return relatedOSImageView; }
    @Override
 	public NetworkView getNetworkView( ) { return relatedNetworkView; }
    @Override
 	public ListSecGroupView getListSecGroupView( ) { return relatedListSecGroupView; }
    @Override
 	public FloatingIPView getFloatingIPView( ) { return relatedFloatingIPView; }
    @Override
 	public VolumeView getVolumeView( ) { return relatedVolumeView; }
    @Override
 	public SecGroupView getSecGroupView( ) { return relatedSecGroupView; }
    @Override
	public NetworkListView getNetworkListView() {return relatedNetworkListView;}
};
