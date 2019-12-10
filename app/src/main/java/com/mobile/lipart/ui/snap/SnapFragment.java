package com.mobile.lipart.ui.snap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mobile.lipart.LivePreviewActivity;
import com.mobile.lipart.R;
import com.mobile.lipart.StillImageActivity;

import java.util.ArrayList;
import java.util.List;

public class SnapFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private SnapViewModel snapViewModel;
    private static final int PERMISSION_REQUESTS = 1;
    private static final String TAG = "ChooserActivity";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        snapViewModel =
                ViewModelProviders.of(this).get(SnapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_snap, container, false);

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        Button live = root.findViewById(R.id.liveButton);
        Button still = root.findViewById(R.id.stillButton);
        live.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), LivePreviewActivity.class));
                    }
                });
        still.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), StillImageActivity.class));
                    }
                });
        return root;
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getActivity().getPackageManager()
                            .getPackageInfo(this.getActivity().getPackageName(), PackageManager.GET_PERMISSIONS);
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
            if (!isPermissionGranted(getActivity(), permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getActivity(), permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    getActivity(), allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
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

}
