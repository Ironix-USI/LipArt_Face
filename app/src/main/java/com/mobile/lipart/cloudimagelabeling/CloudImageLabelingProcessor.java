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
package com.mobile.lipart.cloudimagelabeling;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.mobile.lipart.common.FrameMetadata;
import com.mobile.lipart.common.GraphicOverlay;
import com.mobile.lipart.VisionProcessorBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cloud Label Detector Demo.
 */
public class CloudImageLabelingProcessor
        extends VisionProcessorBase<List<FirebaseVisionImageLabel>> {
    private static final String TAG = "CloudImgLabelProc";

    private final FirebaseVisionImageLabeler detector;

    public CloudImageLabelingProcessor() {
        FirebaseVisionOnDeviceImageLabelerOptions.Builder optionsBuilder =
                new FirebaseVisionOnDeviceImageLabelerOptions.Builder();

        detector = FirebaseVision.getInstance().getOnDeviceImageLabeler(optionsBuilder.build());
    }

    @Override
    protected Task<List<FirebaseVisionImageLabel>> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionImageLabel> labels,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay,
            @NonNull TextView textView,
            @NonNull ImageView drawable) {
        graphicOverlay.clear();
        Log.d(TAG, "cloud label size: " + labels.size());
//        List<String> labelsStr = new ArrayList<>();
//        for (int i = 0; i < labels.size(); ++i) {
//            FirebaseVisionImageLabel label = labels.get(i);
//            Log.d(TAG, "cloud label: " + label);
//            if (label.getText() != null) {
//                labelsStr.add((label.getText()));
//            }
//        }
        if (labels.get(0).getText().equals("Lipstick") == false) {
            textView.setText("This is not a lipstick");
            textView.setTextColor(Color.WHITE);
            drawable.setVisibility(View.INVISIBLE);
        }
        else {
//        CloudLabelGraphic cloudLabelGraphic = new CloudLabelGraphic(graphicOverlay, labelsStr);
//        graphicOverlay.add(cloudLabelGraphic);
//        graphicOverlay.postInvalidate();
            textView.setText("");
            CloudLabelGraphic cloudLabelGraphic = new CloudLabelGraphic(graphicOverlay);
            graphicOverlay.add(cloudLabelGraphic);
            int pixel = originalCameraImage.getPixel(graphicOverlay.getWidth() / 2, graphicOverlay.getHeight() / 8);
            int r = Color.red(pixel);
            int g = Color.blue(pixel);
            int b = Color.green(pixel);
            String hex = String.format("#%02X%02X%02X", r, g, b);
            drawable.setVisibility(View.VISIBLE);
            drawable.setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC);
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Cloud Label detection failed " + e);
    }
}
