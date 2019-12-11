package com.mobile.lipart.facedetection;

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
//    int test = Color.parseColor("#99ff0000");
//    int darken = ColorUtil.darken(test, 30);
//    int light = ColorUtil.lighten(test, 30);
//    int hue = ColorUtil.hue(test, 5);
//    final int lipColor = ColorUtil.adjustAlpha(test, 0.3f);

//    final int lipColor = ColorUtil.HSLToColor(ColorUtil.colorToHSL(test));
//    final int lipColor = Color.parseColor("#99ff0000");

//    Color Blending
    final int lipColor = Color.parseColor(color);
    lipPaint.setStyle(Paint.Style.FILL);
    lipPaint.setColor(lipColor);
//    opacity
    lipPaint.setAlpha(90);
    lipPaint.setAntiAlias(true);
//    Blur
    lipPaint.setMaskFilter(new BlurMaskFilter(9, BlurMaskFilter.Blur.NORMAL));
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
