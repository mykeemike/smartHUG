package noyau.presentation;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsFacebookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_facebook);
	}
		
	/* CONNEXION A FACEBOOK */

	
	private void loginFacebook(View v) {
        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                        	String userConnexion = "";
                        	if (user != null) {
                                userConnexion = "Hello " + user.getName() + "!";
                            } else {
                                userConnexion = "connection unsuccessful";
                            }
                            Toast.makeText(getApplicationContext(), userConnexion, Toast.LENGTH_SHORT).show();                             
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
	  
	  public void logoutFacebook(View v){
		  Session session = Session.getActiveSession();
          if (session != null & !session.isClosed()) {
              session.closeAndClearTokenInformation();
          }
    	  Toast.makeText(getApplicationContext(), "Vous vous êtes déconnectés!", Toast.LENGTH_SHORT).show();		  
	  }
}
