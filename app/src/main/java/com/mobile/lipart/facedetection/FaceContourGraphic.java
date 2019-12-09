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

  private static final float FACE_POSITION_RADIUS = 4.0f;

  private final Paint facePositionPaint;

  private final Paint lipPaint;

  private volatile FirebaseVisionFace firebaseVisionFace;

  public FaceContourGraphic(GraphicOverlay overlay, FirebaseVisionFace face) {
    super(overlay);

    this.firebaseVisionFace = face;
    final int selectedColor = Color.WHITE;

    facePositionPaint = new Paint();
    facePositionPaint.setColor(selectedColor);

    lipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//    int test = Color.parseColor("#99ff0000");
//    int darken = ColorUtil.darken(test, 30);
//    int light = ColorUtil.lighten(test, 30);
//    int hue = ColorUtil.hue(test, 5);
//    final int lipColor = ColorUtil.adjustAlpha(test, 0.3f);

//    final int lipColor = ColorUtil.HSLToColor(ColorUtil.colorToHSL(test));
    final int lipColor = Color.parseColor("#99ff0000");
    lipPaint.setStyle(Paint.Style.FILL);
    lipPaint.setColor(lipColor);
    lipPaint.setAlpha(50);
    lipPaint.setAntiAlias(true);
    lipPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

//    ColorFilter filter = new PorterDuffColorFilter(test, PorterDuff.Mode.SRC_IN);
//    lipPaint.setColorFilter(filter);


  }

  /** Draws the face annotations for position on the supplied canvas. */
  @Override
  public void draw(Canvas canvas) {
    FirebaseVisionFace face = firebaseVisionFace;
    if (face == null) {
      return;
    }

    // Draws a circle at the position of the detected face, with the face's track id below.
//    float x = translateX(face.getBoundingBox().centerX());
//    float y = translateY(face.getBoundingBox().centerY());

    FirebaseVisionFaceContour contour = face.getContour(FirebaseVisionFaceContour.ALL_POINTS);
    for (FirebaseVisionPoint point : contour.getPoints()) {
      float px = translateX(point.getX());
      float py = translateY(point.getY());
//      canvas.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint);
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
