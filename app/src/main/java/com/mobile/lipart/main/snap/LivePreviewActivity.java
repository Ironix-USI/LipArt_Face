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
package com.mobile.lipart.main.snap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mobile.lipart.R;
import com.mobile.lipart.common.BaseActivity;
import com.mobile.lipart.common.CameraSource;
import com.mobile.lipart.common.CameraSourcePreview;
import com.mobile.lipart.common.GraphicOverlay;
import com.mobile.lipart.common.preference.SettingsActivity;
import com.mobile.lipart.common.preference.SettingsActivity.LaunchSource;
import com.mobile.lipart.common.facedetection.FaceContourDetectorProcessor;
import com.mobile.lipart.model.Post;
import com.mobile.lipart.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source.
 */
@KeepName
public final class LivePreviewActivity extends BaseActivity
        implements OnRequestPermissionsResultCallback {

    private static final String FACE_CONTOUR = "Face Contour";
    private static final String TAG = "LivePreviewActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = FACE_CONTOUR;
    private Button shareButton;
    private DatabaseReference mDatabase;
    private String mText = "";
    private String hex = "";
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Button loadButton;
    private int focusButton = 0;
    private TextView textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_live_preview);

        preview = findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        shareButton = findViewById(R.id.shareLiveButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharePost();
            }
        });
        shareButton.setVisibility(View.INVISIBLE);

        textColor = findViewById(R.id.colorText);

        InputStream is = getResources().openRawResource(R.raw.color);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e("Unhandled exception", e.toString());
        }
        finally {
            try {
                is.close();
            } catch (Exception e) {
                Log.e("JSON closing", e.toString());
            }
        }

        String jsonString = writer.toString().trim();

        JsonArray convertedObject = new Gson().fromJson(jsonString, JsonObject.class).get("brands").getAsJsonArray();

//        final ArrayList<String> lipstickColor = new ArrayList<>();
        final Map<String, String> mapColor = new HashMap<String, String>();

        for (JsonElement element : convertedObject) {
            String brand_name = ((JsonObject) element).get("name").getAsString();
            for(JsonElement serie: ((JsonObject) element).get("series").getAsJsonArray()) {
                for(JsonElement lipstick: ((JsonObject) serie).get("lipsticks").getAsJsonArray()) {
                    String color_name = ((JsonObject) lipstick).get("name").getAsString();
                    String lColor = ((JsonObject) lipstick).get("color").getAsString();
                    mapColor.put(brand_name + " - " + color_name, lColor);
//                    lipstickColor.add(lColor);
                }
            }
        }



        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("user-colors/" + getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    mapColor.put("Your color collections", item_snapshot.child("color").getValue().toString());
//                    lipstickColor.add(item_snapshot.child("color").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        loadButton = findViewById(R.id.loadButton);
        /***
         * Prepare pallete color buttons
         ***/
        loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /***
                 * Set buttons linear layout attributes (margin, gravity, etc)
                 ***/
                LinearLayout palette = findViewById(R.id.palette);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 5, 20);
                params.gravity = Gravity.CENTER_VERTICAL;
                int i = 0;

                /***
                 * Set buttons
                 ***/
                for (String key : mapColor.keySet()) {
                    final ImageView iv = new ImageView(getApplicationContext());

                    /***
                     * Set param button attributes ( padding, color, shape, etc)
                     ***/
                    iv.setImageResource(R.drawable.circle_palette);
                    iv.setColorFilter(Color.parseColor(mapColor.get(key)));
                    iv.setPadding(5,5,5,5);
                    iv.setLayoutParams(params);
                    iv.setId(i+1);
                    final String finalColor = mapColor.get(key);
                    final String finalKey = key;

                    /***
                     * Set button on click listener
                     ***/
                    iv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            preview.stop();
                            hex = finalColor;
                            // Add pallete border to show focus button
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setColor(Color.TRANSPARENT);
                            drawable.setShape(GradientDrawable.OVAL);
                            drawable.setStroke(4, Color.parseColor("#e1e2e3"));
                            drawable.setSize(2, 2);
                            textColor.setText(finalKey);
                            textColor.setTextColor(Color.WHITE);
                            textColor.setPadding(60, 30, 60, 30);
                            // remove current focused border
                            if(focusButton != 0){
                                final ImageView focusiv = findViewById(focusButton);
                                focusiv.setBackgroundResource(0);
                            }
                            // add new focus border
                            focusButton = iv.getId();
                            iv.setBackground(drawable);
                            createCameraSource(FACE_CONTOUR, hex);
                            startCameraSource();
                        }
                    });
                    /***
                     * add each buttons to the layout
                     ***/
                    palette.addView(iv);
                    i++;
                }
                loadButton.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.VISIBLE);
            }
        });
    }

    /***
     * navigation bar try menu is created open live_preview_menu
     ***/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_preview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * turn on live front camera source
     ***/
    private void createCameraSource(String model, String color) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
        }

        try {
            switch (model) {
                case FACE_CONTOUR:
                    Log.i(TAG, "Using Face Contour Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new FaceContourDetectorProcessor(color));
                    break;
                default:
                    Log.e(TAG, "Unknown model: " + model);
            }
        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + model, e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    /***
     * GET USERS PERMISSION
     ***/
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel, "00FFFFFF");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    /***
     * Share button function
     ***/
    private void sharePost() {

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

    /***
     * Submit Post of share button function
     ***/
    private void submitPost(final String body, final String color) { ;
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
