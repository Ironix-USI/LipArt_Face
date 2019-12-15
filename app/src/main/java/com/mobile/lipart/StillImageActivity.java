// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.mobile.lipart;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;

import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.lipart.common.CloudLabelGraphic;
import com.mobile.lipart.common.GraphicOverlay;
import com.mobile.lipart.common.preference.SettingsActivity;
import com.mobile.lipart.common.preference.SettingsActivity.LaunchSource;
import com.mobile.lipart.model.Post;
import com.mobile.lipart.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Activity demonstrating different image detector features with a still image from camera. */
@KeepName
public final class StillImageActivity extends BaseActivity {

  private static final String TAG = "StillImageActivity";

  private static final String SIZE_PREVIEW = "w:max"; // Available on-screen width.
  private static final String SIZE_1024_768 = "w:1024"; // ~1024*768 in a normal ratio
  private static final String SIZE_640_480 = "w:640"; // ~640*480 in a normal ratio

  private static final String KEY_IMAGE_URI = "com.googletest.firebase.ml.demo.KEY_IMAGE_URI";
  private static final String KEY_IMAGE_MAX_WIDTH =
      "com.googletest.firebase.ml.demo.KEY_IMAGE_MAX_WIDTH";
  private static final String KEY_IMAGE_MAX_HEIGHT =
      "com.googletest.firebase.ml.demo.KEY_IMAGE_MAX_HEIGHT";
  private static final String KEY_SELECTED_SIZE =
      "com.googletest.firebase.ml.demo.KEY_SELECTED_SIZE";

  private static final int REQUEST_IMAGE_CAPTURE = 1001;
  private static final int REQUEST_CHOOSE_IMAGE = 1002;

  private Button getImageButton;
  private ImageView preview;
  private GraphicOverlay graphicOverlay;
  private String selectedSize = SIZE_PREVIEW;

  boolean isLandScape;

  private Uri imageUri;
  // Max width (portrait mode)
  private Integer imageMaxWidth;
  // Max height (portrait mode)
  private Integer imageMaxHeight;
  private Bitmap bitmapForDetection;
  private TextView textView;
  private ImageView drawable;
  private int status_bar;
  private Button saveColorButton;
  private String hex = "";
  private DatabaseReference mDatabase;
  private Button shareButton;
  private String mText = "";
  private TextView pickedColorText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_still_image);

    getImageButton = findViewById(R.id.getImageButton);
    getImageButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            // Menu for selecting either: a) take new photo b) select from existing
            PopupMenu popup = new PopupMenu(StillImageActivity.this, view);
            popup.setOnMenuItemClickListener(
                new OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                      case R.id.select_images_from_local:
                        startChooseImageIntentForResult();
                        return true;
                      case R.id.take_photo_using_camera:
                        startCameraIntentForResult();
                        return true;
                      default:
                        return false;
                    }
                  }
                });

            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.camera_button_menu, popup.getMenu());
            popup.show();
          }
        });
    preview = findViewById(R.id.previewPane);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.previewOverlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    drawable = findViewById(R.id.colorCircle);
    pickedColorText = findViewById(R.id.pickedcolortext);

    isLandScape =
        (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

    if (savedInstanceState != null) {
      imageUri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
      imageMaxWidth = savedInstanceState.getInt(KEY_IMAGE_MAX_WIDTH);
      imageMaxHeight = savedInstanceState.getInt(KEY_IMAGE_MAX_HEIGHT);
      selectedSize = savedInstanceState.getString(KEY_SELECTED_SIZE);

      if (imageUri != null) {
        tryReloadAndDetectInImage();
      }
    }

    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      status_bar = getResources().getDimensionPixelSize(resourceId);
    }

    mDatabase = FirebaseDatabase.getInstance().getReference();
    saveColorButton = findViewById(R.id.saveButton);
    saveColorButton.setOnClickListener(
            new OnClickListener() {
              @Override
              public void onClick(View view) {
                  saveColor();
              }
            });

    shareButton = findViewById(R.id.shareButton);
    shareButton.setOnClickListener(
            new OnClickListener() {
              @Override
              public void onClick(View view) {
                  sharePost();
                }
            });

    pickedColorText.setVisibility(View.INVISIBLE);
    saveColorButton.setVisibility(View.INVISIBLE);
    shareButton.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    tryReloadAndDetectInImage();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.still_image_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.STILL_IMAGE);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putParcelable(KEY_IMAGE_URI, imageUri);
    if (imageMaxWidth != null) {
      outState.putInt(KEY_IMAGE_MAX_WIDTH, imageMaxWidth);
    }
    if (imageMaxHeight != null) {
      outState.putInt(KEY_IMAGE_MAX_HEIGHT, imageMaxHeight);
    }
    outState.putString(KEY_SELECTED_SIZE, selectedSize);
  }

  private void startCameraIntentForResult() {
    // Clean up last time's image
    imageUri = null;
    preview.setImageBitmap(null);

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      ContentValues values = new ContentValues();
      values.put(MediaStore.Images.Media.TITLE, "New Picture");
      values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
      imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  private void startChooseImageIntentForResult() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      tryReloadAndDetectInImage();
    } else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK) {
      // In this case, imageUri is returned by the chooser, save it.
      imageUri = data.getData();
      tryReloadAndDetectInImage();
    }
  }

  private void tryReloadAndDetectInImage() {
    try {
      if (imageUri == null) {
        return;
      }

      // Clear the overlay first
      graphicOverlay.clear();

      Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

      // Get the dimensions of the View
      Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();

      int targetWidth = targetedSize.first;
      int maxHeight = targetedSize.second;

      // Determine how much to scale down the image
      float scaleFactor =
          Math.max(
              (float) imageBitmap.getWidth() / (float) targetWidth,
              (float) imageBitmap.getHeight() / (float) maxHeight);

      Bitmap resizedBitmap =
          Bitmap.createScaledBitmap(
              imageBitmap,
              (int) (imageBitmap.getWidth() / scaleFactor),
              (int) (imageBitmap.getHeight() / scaleFactor),
              true);

      preview.setImageBitmap(resizedBitmap);
      bitmapForDetection = resizedBitmap;

      pickedColorText.setVisibility(View.VISIBLE);
      saveColorButton.setVisibility(View.VISIBLE);
      shareButton.setVisibility(View.VISIBLE);

    } catch (IOException e) {
      Log.e(TAG, "Error retrieving saved image");
    }
  }

  // Returns max image width, always for portrait mode. Caller needs to swap width / height for
  // landscape mode.
  private Integer getImageMaxWidth() {
    if (imageMaxWidth == null) {
      // Calculate the max width in portrait mode. This is done lazily since we need to wait for
      // a UI layout pass to get the right values. So delay it to first time image rendering time.
      if (isLandScape) {
        imageMaxWidth =
            ((View) preview.getParent()).getHeight() - findViewById(R.id.controlPanel2).getHeight();
      } else {
        imageMaxWidth = ((View) preview.getParent()).getWidth();
      }
    }

    return imageMaxWidth;
  }

  // Returns max image height, always for portrait mode. Caller needs to swap width / height for
  // landscape mode.
  private Integer getImageMaxHeight() {
    if (imageMaxHeight == null) {
      // Calculate the max width in portrait mode. This is done lazily since we need to wait for
      // a UI layout pass to get the right values. So delay it to first time image rendering time.
      if (isLandScape) {
        imageMaxHeight = ((View) preview.getParent()).getWidth();
      } else {
        imageMaxHeight =
            ((View) preview.getParent()).getHeight() - findViewById(R.id.controlPanel2).getHeight();
      }
    }

    return imageMaxHeight;
  }

  // Gets the targeted width / height.
  private Pair<Integer, Integer> getTargetedWidthHeight() {
    int targetWidth;
    int targetHeight;

    switch (selectedSize) {
      case SIZE_PREVIEW:
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = isLandScape ? maxHeightForPortraitMode : maxWidthForPortraitMode;
        targetHeight = isLandScape ? maxWidthForPortraitMode : maxHeightForPortraitMode;
        break;
      case SIZE_640_480:
        targetWidth = isLandScape ? 640 : 480;
        targetHeight = isLandScape ? 480 : 640;
        break;
      case SIZE_1024_768:
        targetWidth = isLandScape ? 1024 : 768;
        targetHeight = isLandScape ? 768 : 1024;
        break;
      default:
        throw new IllegalStateException("Unknown size");
    }

    return new Pair<>(targetWidth, targetHeight);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event){

    int action = MotionEventCompat.getActionMasked(event);

    switch(action) {
      case (MotionEvent.ACTION_DOWN) :
      case (MotionEvent.ACTION_MOVE) :
      case (MotionEvent.ACTION_UP) :

        if (bitmapForDetection != null) {

          int x = (int) (event.getX());
          int y = (int) (event.getY() - status_bar);
          graphicOverlay.add(new CloudLabelGraphic(graphicOverlay, x, y));
          int pixel = bitmapForDetection.getPixel(x,y);
          int r = Color.red(pixel);
          int b = Color.blue(pixel);
          int g = Color.green(pixel);
          hex = String.format("#%02X%02X%02X", r, g, b);
          graphicOverlay.clear();
          drawable.setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC);
          pickedColorText.setVisibility(View.INVISIBLE);
        }
      default :
        return super.onTouchEvent(event);
    }
  }

  private void saveColor() {
    final String userId = getUid();
    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                  // User is null, error out
                  Log.e(TAG, "User " + userId + " is unexpectedly null");
                } else {
                  // Write new post
                  writeNewColor(userId);
                }
                // [END_EXCLUDE]
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // [END_EXCLUDE]
              }
            });
    // [END single_value_read]
  }

  // [START write_fan_out]
  private void writeNewColor(String userId) {
    // Create new post at /user-posts/$userid/$postid and at
    // /posts/$postid simultaneously
    String key = mDatabase.child("user-colors").push().getKey();
    Map<String, Object> colorValues = new HashMap<>();
    colorValues.put("color", hex);

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/user-colors/" + userId + "/" + key, colorValues);

    mDatabase.updateChildren(childUpdates);
    Toast.makeText(this, "Saved.",
            Toast.LENGTH_SHORT).show();
  }
  // [END write_fan_out]

  private void sharePost() {
    mDatabase = FirebaseDatabase.getInstance().getReference();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    // Set up the input
    final EditText input = new EditText(this);
    input.setHint("write...");
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mText = input.getText().toString();
        if (!mText.equals("")) {
          submitPost(mText, hex);
        }
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    builder.show();
  }

  private void submitPost(final String body, final String color) {
    final String userId = getUid();
    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                  // User is null, error out
                  Log.e(TAG, "User " + userId + " is unexpectedly null");
                } else {
                  // Write new post
                  writeNewPost(userId, user.username, body, color);
                }
                // [END_EXCLUDE]
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // [END_EXCLUDE]
              }
            });
    // [END single_value_read]
  }

  // [START write_fan_out]
  private void writeNewPost(String userId, String username, String body, String color) {
    // Create new post at /user-posts/$userid/$postid and at
    // /posts/$postid simultaneously
    String key = mDatabase.child("posts").push().getKey();
    Post post = new Post(userId, username, body, color, key);
    Map<String, Object> postValues = post.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/posts/" + key, postValues);
    childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

    mDatabase.updateChildren(childUpdates);
    Toast.makeText(this, "Posted.",
            Toast.LENGTH_SHORT).show();
  }
  // [END write_fan_out]
}
