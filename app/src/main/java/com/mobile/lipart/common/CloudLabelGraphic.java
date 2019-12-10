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
package com.mobile.lipart.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mobile.lipart.common.GraphicOverlay.Graphic;

/** Graphic instance for rendering detected label. */
public class CloudLabelGraphic extends Graphic {
  private final Paint textPaint;
  private final float x;
  private final float y;

  public CloudLabelGraphic(GraphicOverlay overlay, float x, float y){
    super(overlay);
    textPaint = new Paint();
    textPaint.setColor(Color.BLACK);
    this.x = x;
    this.y = y;
  }

  @Override
  public synchronized void draw(Canvas canvas) {
    canvas.drawCircle(x, y, 20, textPaint);
  }
}
