package com.coho.moki.ui.product;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.coho.moki.callback.ITakePhotoListener;

public class PhotoHandler implements PictureCallback {

    private static final String T = "PhotoHandler";

    private final Context context;
    private ITakePhotoListener listener;

    public PhotoHandler(Context context, ITakePhotoListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = getDir();
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Uri uri = Uri.fromFile(pictureFile);
            Log.d(T, "save success, url: " + filename);
            listener.onSuccess(uri);
        } catch (Exception error) {
            Log.d(T, "error save image");
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }

}