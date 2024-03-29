package com.mobile.lipart.common.facedetection;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.mobile.lipart.common.GraphicOverlay;
import com.mobile.lipart.common.GraphicOverlay.Graphic;

import java.util.List;

/** Graphic instance for rendering face contours graphic overlay view. */
public class FaceContourGraphic extends Graphic {


  private final Paint lipPaint;

  private volatile FirebaseVisionFace firebaseVisionFace;

  public FaceContourGraphic(GraphicOverlay overlay, FirebaseVisionFace face, String color) {
    super(overlay);

    this.firebaseVisionFace = face;

    lipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Color Blending
    int lipColor = Color.parseColor(color);

    // darkening the color
    float[] hsv = new float[3];
    Color.colorToHSV(lipColor, hsv);
    hsv[2] = 1.0f - 0.8f * (1.0f - hsv[2]); // value component
    lipColor = Color.HSVToColor(hsv);

    // canvas coloring method
    lipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    lipPaint.setColor(lipColor);

    // set stroke and dither around lip to make neat and smooth to see
    lipPaint.setStrokeWidth(3);
    lipPaint.setDither(true);

    // opacity and anti alias(smooth the jadge edges)
    lipPaint.setAlpha(150);
    lipPaint.setAntiAlias(true);
    lipPaint.clearShadowLayer();
    // Blur
    lipPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
  }

  /** Draws the face annotations for position on the supplied canvas. */
  @Override
  public void draw(Canvas canvas) {
    FirebaseVisionFace face = firebaseVisionFace;
    if (face == null) {
      return;
    }

    List<FirebaseVisionPoint> upperLipBottomContour =
            face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();
    List<FirebaseVisionPoint> upperLipTopContour =
            face.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints();
    List<FirebaseVisionPoint> lowerLipBottomContour =
            face.getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).getPoints();
    List<FirebaseVisionPoint> lowerLipTopContour =
            face.getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints();

    Path lipUpperPath = new Path();
    Path lipLowerPath = new Path();

    if (upperLipBottomContour.size() != 0 && lowerLipBottomContour.size() != 0 && upperLipTopContour.size() != 0 && lowerLipTopContour.size() != 0) {
      lipUpperPath.moveTo(translateX(upperLipTopContour.get(0).getX()), translateY(upperLipTopContour.get(0).getY()));
      for (FirebaseVisionPoint pt : upperLipTopContour) {
        lipUpperPath.lineTo(translateX(pt.getX()), translateY(pt.getY()));
      }
      for (int i = upperLipBottomContour.size() - 1; i >= 0; i--) {
        lipUpperPath.lineTo(translateX(upperLipBottomContour.get(i).getX()), translateY(upperLipBottomContour.get(i).getY()));
      }
      lipUpperPath.close();

      lipLowerPath.moveTo(translateX(lowerLipTopContour.get(0).getX()), translateY(lowerLipTopContour.get(0).getY()));
      for (FirebaseVisionPoint pt : lowerLipTopContour) {
        lipLowerPath.lineTo(translateX(pt.getX()), translateY(pt.getY()));
      }
      for (int i = lowerLipBottomContour.size() - 1; i >= 0; i--) {
        lipLowerPath.lineTo(translateX(lowerLipBottomContour.get(i).getX()), translateY(lowerLipBottomContour.get(i).getY()));
      }
      lipLowerPath.close();

//      Matrix scaleMatrix = new Matrix();
//      RectF rectF = new RectF();
//      lipUpperPath.computeBounds(rectF, true);
//      scaleMatrix.setScale(0.9f, 0.5f,rectF.centerX(),rectF.centerY());
//      lipUpperPath.transform(scaleMatrix);
//
//      scaleMatrix = new Matrix();
//      rectF = new RectF();
//      lipLowerPath.computeBounds(rectF, true);
//      scaleMatrix.setScale(0.9f, 0.5f,rectF.centerX(),rectF.centerY());
//      lipLowerPath.transform(scaleMatrix);

      canvas.drawPath(lipUpperPath, lipPaint);
      canvas.drawPath(lipLowerPath, lipPaint);
    }
  }
}
