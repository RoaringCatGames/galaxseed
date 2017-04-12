package com.roaringcatgames.galaxseed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.example.games.basegameutils.BaseGameUtils;

public class AndroidLauncher extends AndroidApplication implements
        IAdController, IGameServiceController,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AdView adView;
    protected View gameView;

    private GoogleApiClient googleApiClient;
    private static int RC_SIGN_IN = 9001;
    private static int REQUEST_ACHIEVEMENTS = 9002;
    private static int REQUEST_LEADERBOARD = 9003;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    private int lastAction = -1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useImmersiveMode = true;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        //layout.setLayoutTransition(new LayoutTransition());


        AdView admobView = createAdView();
        layout.addView(admobView);
        View gameView = createGameView(cfg);
        layout.addView(gameView);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .useDefaultAccount()
                .build();

        setContentView(layout);
	}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setId(getResources().getInteger(R.integer.ad_view_id)); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        adView.setVisibility(View.GONE);
        return adView;
    }

    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(App.Initialize(this, this), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
    }

    private void startAdvertising(AdView adView) {
        final AdView aView = adView;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Gdx.app.log("ANDROID LAUNCHER", "### STARTING ADVERTISEMENT");
                AdRequest adRequest = new AdRequest.Builder().build();
                aView.loadAd(adRequest);
                aView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
    }


    /**
     * IAdController Implementation
     */

    @Override
    public void showBannerAd(AdPlacement placement) {
        if(this.adView != null && this.adView.getVisibility() != View.VISIBLE){
            this.startAdvertising(adView);
        }
    }

    @Override
    public void hideBannerAd(AdPlacement placement) {
        if(this.adView != null && this.adView.getVisibility() != View.GONE){
            final AdView targetAd = this.adView;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    targetAd.setVisibility(View.GONE);
                }
            });
        }
    }


    /**
     * Connection Callback implementations
     * @param bundle
     */

    @Override
    public void onConnected(Bundle bundle) {
        //Gdx.app.log("ANDROID LAUNCHER", "CONNECTED!!");
        if(lastAction == REQUEST_LEADERBOARD){
            showLeaderBoard();
        }else if(lastAction == REQUEST_ACHIEVEMENTS){
            showAchievements();
        }
        //this.connectionEstablished();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Gdx.app.log("ANDROID LAUNCHER", "Connection Suspended: " + i);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Gdx.app.log("ANDROID LAUNCHER", "Connection Failed!!" + connectionResult.getErrorCode() + "-" + connectionResult.hasResolution() + "-" + connectionResult.getResolution().toString());
        //Gdx.app.log("ANDROID LAUNCHER", " Resolving Connection?: " + mResolvingConnectionFailure);
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            //Gdx.app.log("ANDROID LAUNCHER", "ATtempting to Resolve COnnection Failure");
            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    googleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                //Gdx.app.log("ANDROID LAUNCHER", "Failed to Resolve ConnectionFailer via BaseGameUtils");
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                }catch(Exception e){
                    Gdx.app.error("ANDROID LAUNCHER", "FAILED TO MAUNAULLY RESOLVE SHIT");
                }
                mResolvingConnectionFailure = false;
            }else{
                mResolvingConnectionFailure = false;
                //Gdx.app.log("ANDROID LAUNCHER", "Resolved I guess");
                try {
                    ConnectionResult connResult = googleApiClient.getConnectionResult(Games.API);
                    if (connResult != null) {
                        onConnectionFailed(connResult);
                    }
                }catch(Exception e){
                    //Gdx.app.log("ANDROID LAUNCHER", "Failed to get ConnectionResult: " + e.getMessage());
                }
            }
        }
    }


    protected void onActivityResult(final int requestCode, final int resultCode,final Intent data) {
        //Gdx.app.log("ANDROID LAUNCHER", "Activity Result Occurred Request Code: " + requestCode +  " Result Code: " + resultCode);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            //Gdx.app.log("ANDROID LAUNCHER", "The User was Chosen!");
            googleApiClient.connect();
        } else if ( resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED)  {
            //Gdx.app.log("ANDROID LAUNCHER", "Google Play Services Sign Out occurred!");
            googleApiClient.disconnect();
        }
    }

    /**
     * GameServiceController Implementation
     */

    @Override
    public boolean isConnected() {
        return googleApiClient != null ? googleApiClient.isConnected() : false;
    }

    @Override
    public void connectToGameServices() {

        if(!googleApiClient.isConnected()){
            mSignInClicked = true;
            googleApiClient.connect();
        }
    }

    @Override
    public void unlockAchievement(String name) {

        if(googleApiClient.isConnected()) {
            switch (name) {
                case AchievementItems.KITTEN:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.kitten_achievement_id));
                    break;
                case AchievementItems.TREE_HIGH:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.tree_high_club_achievement_id));
                    break;
                case AchievementItems.WEAPON:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.weapon_achievement_id));
                    break;
                case AchievementItems.GALACTIC_GARDENER:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.galactic_gardener_achievement_id));
                    break;
                case AchievementItems.DONUT:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.donut_achievement_id));
                    break;

                case AchievementItems.URANUS:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.uranus_achievement_id));
                    break;
                case AchievementItems.HOME:
                    Games.Achievements.unlock(googleApiClient, getString(R.string.home_achievement_id));
                    break;
            }
        }
    }

    @Override
    public void submitScore(int score) {
        //Gdx.app.log("ANDROID LAUNCHER", "Submitting Score: " + score);
        if(googleApiClient.isConnected()) {

            //Gdx.app.log("ANDROID LAUNCHER", "Connected and Sending Score!!" + score);
            Games.Leaderboards.submitScore(googleApiClient, getString(R.string.leaderboard_id), score);
        }
    }

    @Override
    public void showAchievements() {
        //Gdx.app.log("ANDROID LAUNCHER", "Requesting Achievements");
        if(googleApiClient.isConnected()) {
            lastAction = -1;
            startActivityForResult(Games.Achievements.getAchievementsIntent(googleApiClient),
                    REQUEST_ACHIEVEMENTS);
        }else{
            this.lastAction = REQUEST_ACHIEVEMENTS;
            connectToGameServices();
        }
    }

    @Override
    public void showLeaderBoard() {
        //Gdx.app.log("ANDROID LAUNCHER", "Requesting Leaderboards");
        if (isConnected()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(googleApiClient,
                    getString(R.string.leaderboard_id)), REQUEST_LEADERBOARD);
        }else{
            this.lastAction = REQUEST_LEADERBOARD;
            connectToGameServices();
        }
    }
}
