package ua.naiksoftware.slackmusicstatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import ua.naiksoftware.slackmusicstatus.model.AuthResponse;
import ua.naiksoftware.slackmusicstatus.model.FetchProfileResponse;
import ua.naiksoftware.slackmusicstatus.model.FetchTeamResponse;
import ua.naiksoftware.slackmusicstatus.model.User;

public class LoginActivity extends Activity {

    private AsyncTask mLoadDataTask;

    private enum LoginError {
        AUTH(R.string.error_auth),
        FETCH_USER_INFO(R.string.error_fetch_user_info),
        FETCH_TEAM_INFO(R.string.error_fetch_team_info);

        final int errorString;

        LoginError(int errorString) {
            this.errorString = errorString;
        }
    }

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Config.Slack.AUTH_URL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                if (url.getHost().equals(Config.Slack.REDIRECT_URL_HOST)) {
                    String code = url.getQueryParameter("code");
                    mLoadDataTask = NetworkHelper.getAsync(new Callback<AuthResponse>() {
                        @Override
                        public void call(AuthResponse authResponse) {
                            if (authResponse == null) onLoginError(LoginError.AUTH);
                            else onAuthSuccess(authResponse);
                        }
                    }, AuthResponse.class).execute(String.format(Config.Slack.FETCH_TOKEN_URL, code));
                    showProgress(true);
                } else {
                    view.loadUrl(url.toString());
                }
                return false;
            }
        });
    }

    private void onAuthSuccess(final AuthResponse response) {
        mLoadDataTask = NetworkHelper.getAsync(new Callback<FetchProfileResponse>() {
            @Override
            public void call(FetchProfileResponse profileResponse) {
                if (profileResponse == null) onLoginError(LoginError.FETCH_USER_INFO);
                else onFetchProfileSuccess(response, profileResponse);
            }
        }, FetchProfileResponse.class).execute(String.format(Config.Slack.FETCH_PROFILE_URL, response.accessToken));
    }

    private void onFetchProfileSuccess(final AuthResponse authResponse, final FetchProfileResponse profileResponse) {
        mLoadDataTask = NetworkHelper.getAsync(new Callback<FetchTeamResponse>() {
            @Override
            public void call(FetchTeamResponse teamResponse) {
                if (teamResponse == null) onLoginError(LoginError.FETCH_TEAM_INFO);
                else onFetchTeamSuccess(authResponse, profileResponse, teamResponse);
            }
        }, FetchTeamResponse.class).execute(String.format(Config.Slack.FETCH_TEAM_URL, authResponse.accessToken));
    }

    private void onFetchTeamSuccess(AuthResponse authResponse, FetchProfileResponse profileResponse, FetchTeamResponse teamResponse) {
        User user = new User(
                authResponse.accessToken,
                profileResponse.profile.firstName + " " + profileResponse.profile.lastName,
                profileResponse.profile.avatar,
                teamResponse.team.name,
                teamResponse.team.icon.image_132
        );
        DataStorage.storeUser(this, user);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void onLoginError(LoginError error) {
        showProgress(false);
        Toast.makeText(this, error.errorString, Toast.LENGTH_LONG).show();
        mWebView.loadUrl(Config.Slack.AUTH_URL);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mWebView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (mLoadDataTask != null) mLoadDataTask.cancel(true);
        super.onDestroy();
    }
}
