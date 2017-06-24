package ua.naiksoftware.slackmusicstatus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 23.06.17.
 */

public class MainActivity extends Activity {

    private TextView mTextViewName;
    private TextView mTeamName;
    private ImageView mAvatar;
    private ImageView mTeamAvatar;

    private AsyncTask<Void, Void, Bitmap[]> mLoadImagesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = DataStorage.getUser(this);
        if (user != null) {
            setContentView(R.layout.activity_main);
            mTextViewName = (TextView) findViewById(R.id.name);
            mAvatar = (ImageView) findViewById(R.id.avatar);
            mTeamName = (TextView) findViewById(R.id.team);
            mTeamAvatar = (ImageView) findViewById(R.id.team_avatar);
            bindInfo(user);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
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
    protected void onDestroy() {
        if (mLoadImagesTask != null) mLoadImagesTask.cancel(true);
        super.onDestroy();
    }
}
