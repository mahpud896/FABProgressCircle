package com.github.mahpud896.fabprogresscircle;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import com.github.mahpud896.fabprogresscircle;
import com.github.mahpud896.fabprogresscircle.executor.ThreadExecutor;
import com.github.mahpud896.fabprogresscircle.interactor.MockAction;
import com.github.mahpud896.fabprogresscircle.interactor.MockActionCallback;
import com.github.mahpud896.fabprogresscircle.picasso.GrayscaleCircleTransform;
import com.github.mahpud896.listeners.FABProgressListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends Activity implements MockActionCallback, FABProgressListener {

  private FABProgressCircle fabProgressCircle;
  private boolean taskRunning;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    loadAvatar();
    attachListeners();
  }

  private void initViews() {
    fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
  }

  private void loadAvatar() {
    ImageView avatarView = (ImageView) findViewById(R.id.avatar);
    Picasso.with(this)
        .load(R.drawable.avatar)
        .transform(new GrayscaleCircleTransform())
        .into(avatarView);
  }

  private void attachListeners() {
    fabProgressCircle.attachListener(this);

    findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (!taskRunning) {
          fabProgressCircle.show();
          runMockInteractor();
        }
      }
    });
  }

  private void runMockInteractor() {
    ThreadExecutor executor = new ThreadExecutor();
    executor.run(new MockAction(this));
    taskRunning = true;
  }

  @Override public void onMockActionComplete() {
    taskRunning = false;
    fabProgressCircle.beginFinalAnimation();
    //fabProgressCircle.hide();
  }

  @Override public void onFABProgressAnimationEnd() {
    Snackbar.make(fabProgressCircle, R.string.cloud_upload_complete, Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show();
  }
}