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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mobile.lipart.common.GraphicOverlay;
import com.mobile.lipart.common.GraphicOverlay.Graphic;

import java.util.List;

/** Graphic instance for rendering detected label. */
public class CloudLabelGraphic extends Graphic {
  private final Paint textPaint;
  private final GraphicOverlay overlay;
  private final float x;
  private final float y;

  private List<String> labels;

  public CloudLabelGraphic(GraphicOverlay overlay, float x, float y){//, List<String> labels) {
    super(overlay);
    this.overlay = overlay;
//    this.labels = labels;
    textPaint = new Paint();
    textPaint.setColor(Color.BLACK);
//    textPaint.setTextSize(60.0f);
    this.x = x;
    this.y = y;
  }

  @Override
  public synchronized void draw(Canvas canvas) {
//    float x = overlay.getWidth() / 4.0f;
//    float y = overlay.getHeight() / 4.0f;
//
//    for (String label : labels) {
//      canvas.drawText(label, x, y, textPaint);
//      y = y - 62.0f;

    canvas.drawCircle(x, y, 20, textPaint);
  }
}
