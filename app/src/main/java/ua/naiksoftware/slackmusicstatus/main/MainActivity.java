package ua.naiksoftware.slackmusicstatus.main;

import android.app.Activity;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import ua.naiksoftware.slackmusicstatus.changemusic.ChangeStatusService;
import ua.naiksoftware.slackmusicstatus.Config;
import ua.naiksoftware.slackmusicstatus.login.AuthException;
import ua.naiksoftware.slackmusicstatus.shared.DataStorage;
import ua.naiksoftware.slackmusicstatus.login.LoginActivity;
import ua.naiksoftware.slackmusicstatus.R;
import ua.naiksoftware.slackmusicstatus.settings.SettingsActivity;
import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 23.06.17.
 */

public class MainActivity extends LifecycleActivity {

    private TextView mTextViewName;
    private TextView mTeamName;
    private ImageView mAvatar;
    private ImageView mTeamAvatar;

    private AsyncTask<Void, Void, Bitmap[]> mLoadImagesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewName = (TextView) findViewById(R.id.name);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mTeamName = (TextView) findViewById(R.id.team);
        mTeamAvatar = (ImageView) findViewById(R.id.team_avatar);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getErrors().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                if (throwable == null) return;
                if (throwable instanceof AuthException) {
                    startLogin();
                } else {
                    Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                bindInfo(user);
            }
        });
    }

    private void bindInfo(final User user) {
        mTextViewName.setText(user.name);
        mTeamName.setText(user.teamName);
        mLoadImagesTask = new AsyncTask<Void, Void, Bitmap[]>() {
            @Override
            protected Bitmap[] doInBackground(Void... params) {
                Bitmap[] result = new Bitmap[2];
                try {
                    result[0] = BitmapFactory.decodeStream(new URL(user.avatar).openStream());
                    result[1] = BitmapFactory.decodeStream(new URL(user.teamAvatar).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap[] bitmaps) {
                if (bitmaps[0] != null) mAvatar.setImageBitmap(bitmaps[0]);
                if (bitmaps[1] != null) mTeamAvatar.setImageBitmap(bitmaps[1]);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_enabled);
        Switch switchEnabled = (Switch) item.getActionView();
        switchEnabled.setChecked(DataStorage.getEnabled(this));
        switchEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataStorage.setEnabled(MainActivity.this, isChecked);
                if (!isChecked)
                    ChangeStatusService.startHandleChange(MainActivity.this, "", Config.Slack.DEFAULT_STATUS_EMOJI, true);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void openSettings(View view) {
        startActivityForResult(new Intent(this, SettingsActivity.class), 0);
    }

    public void logout(View view) {
        DataStorage.clear(this);
        startLogin();
    }

    @Override
    protected void onDestroy() {
        if (mLoadImagesTask != null) mLoadImagesTask.cancel(true);
        super.onDestroy();
    }
}
