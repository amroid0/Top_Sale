package com.aelzohry.topsaleqatar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.aelzohry.topsaleqatar.App;
import com.aelzohry.topsaleqatar.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageCompress {
    Context mContext;
    boolean isMainImage;
    onFinishedCompressListListener listListener;
    List<String> imageUriList = new ArrayList<>();

    public ImageCompress(Context context) {
        this.mContext = context;
    }

    private static boolean deleteNon_EmptyDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteNon_EmptyDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void compressImages(boolean isMainImage, List<String> imageUriList, onFinishedCompressListListener listener) {
        this.imageUriList = imageUriList;
        this.isMainImage = isMainImage;
        this.listListener = listener;
        new ImageListCompressionAsyncTask().execute(imageUriList);
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentURI) {
        String result = null;

        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    public String makePictureToBase64(String image_path) {
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        //        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String type = "";
//        if (image_path.endsWith("jpg")) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        type = "data:image/jpeg;base64,";
//        } else if (image_path.endsWith("png")) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            type = "data:image/png;base64,"
//        } else
//            Toast.makeText(RaffleApp.getInstance(), "make_sure_extension", Toast.LENGTH_LONG).show()

        byte[] byteArrayImage = baos.toByteArray();
        //image.setImageBitmap(bitmap);
        return type + Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    public String checkImg(String path) {
        String newPath = path;
        if (!TextUtils.isEmpty(path)) {
            File f = new File(Uri.parse(path).getPath());
            if (f.exists()) {
                Bitmap resized = getResizedBitmap(Uri.parse(path).getPath(), 640f, 640f);
                if (resized != null) {
                    String npath = saveToFile(resized);
                    if (!TextUtils.isEmpty(npath)) {
                        newPath = npath;
                    }
                }
            }
        }

        return newPath;
    }

    private Bitmap getResizedBitmap(String path, Float widthRatio, Float heightRatio) {
        float scale = 1f;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Uri.parse(path).getPath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageHeight > imageWidth) {
            if (imageHeight > heightRatio) {
                scale = heightRatio / imageHeight;
            }

        } else {
            if (imageWidth > widthRatio) {
                scale = widthRatio / imageWidth;
            }
        }
        if (scale == 0f) return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap bm = BitmapFactory.decodeFile(Uri.parse(path).getPath());
        if (bm != null) {
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            bm.recycle();
            return resizedBitmap;
        }

        return null;
    }

    private String saveToFile(Bitmap bm) {
        File sd = getTempStoreDirectory(App.Companion.getContext());
        String path = null;
        FileOutputStream fOut = null;
        try {
            if (sd.canWrite()) {
                File temp = new File(sd, "temp" + System.currentTimeMillis() + ".png");
                fOut = new FileOutputStream(temp);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                path = temp.getPath();

                bm.recycle();
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return path;
    }

    private File getTempStoreDirectory(Context context) {
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        return context.getExternalFilesDir("temp").getAbsoluteFile();
    }

    private String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        try {

            float maxHeight = 3000.0f;
            float maxWidth = 3000.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

        } catch (ArithmeticException ex) {
//            listListener.onError(mContext.getString(R.string.failLoadImg));
            return null;
        }


        if (actualHeight != 0 && actualWidth != 0) {

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

        } else {
            listListener.onError(mContext.getString(R.string.failLoadImg));
            return null;

        }

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            return null;

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            return null;
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, actualWidth, actualHeight, matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return filename;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private String getFilename() {
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TopSaleApp" + "/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    public boolean deleteFolder() {
        boolean isDeleted = false;
        File dir = new File(Environment.getExternalStorageDirectory().getPath(), "TopSaleApp");
        try {
            isDeleted = deleteNon_EmptyDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public interface onFinishedCompressListListener {
        void onError(String error);

        void onSuccessMainImageCompress(List<String> photos);

        void onSuccessImageCompress(List<String> photos);
    }

    @SuppressLint("StaticFieldLeak")
    class ImageListCompressionAsyncTask extends AsyncTask<List<String>, Void, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... params) {
            List<String> photos = new ArrayList<>();
            for (int i = 0; i < params[0].size(); i++) {
                String image = compressImage(params[0].get(i));
                if (image != null) photos.add(image);
            }

            return photos;
        }

        @Override
        protected void onPostExecute(List<String> photos) {
            super.onPostExecute(photos);
            if (listListener != null) {
                if (photos != null) {
                    try {
                        if (isMainImage) {
                            listListener.onSuccessMainImageCompress(photos);
                        } else {
                            listListener.onSuccessImageCompress(photos);
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        listListener.onError(mContext.getString(R.string.failLoadImg));
                    }

                } else listListener.onError(mContext.getString(R.string.failLoadImg));
            }

        }
    }


     int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
