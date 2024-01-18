package com.aelzohry.topsaleqatar.ui.new_ad;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otaliastudios.transcoder.ThumbnailerListener;
import com.otaliastudios.transcoder.ThumbnailerOptions;
import com.otaliastudios.transcoder.internal.utils.Logger;
import com.otaliastudios.transcoder.resize.FractionResizer;
import com.otaliastudios.transcoder.source.DataSource;
import com.otaliastudios.transcoder.source.TrimDataSource;
import com.otaliastudios.transcoder.source.UriDataSource;
import com.otaliastudios.transcoder.thumbnail.UniformThumbnailRequest;

import java.util.List;
import java.util.concurrent.Future;

import kotlin.collections.ArraysKt;


public class VideoTranscoder {

    private static final Logger LOG = new Logger("TranscoderActivity");

    private static final int PROGRESS_BAR_MAX = 1000;

    private boolean mIsTranscoding;
    private Future<Void> mTranscodeFuture;





    private void transform(Context  context, ThumbnailerListener listener, @NonNull Uri... uris) {
        LOG.e("Building sources...");
        List<DataSource> sources = ArraysKt.map(uris, uri -> new UriDataSource(context, uri));
        long trimStart = 0, trimEnd = 0;
        sources.set(0, new TrimDataSource(sources.get(0), trimStart, trimEnd));

        LOG.e("Building options...");
        ThumbnailerOptions.Builder builder = new ThumbnailerOptions.Builder();
        builder.addThumbnailRequest(new UniformThumbnailRequest(8));
        builder.setListener(listener);
        for (DataSource source : sources) {
            builder.addDataSource(source);
        }
        float aspectRatio =0F;

        float fraction = 1F;

        builder.addResizer(new FractionResizer(fraction));
        int rotation = 0;

        builder.setRotation(rotation);

        // Launch the transcoding operation.
        LOG.e("Starting transcoding!");
        setIsTranscoding(true, null);
        mTranscodeFuture = builder.thumbnails();
    }
    public  void cancel(){
        mTranscodeFuture.cancel(true);
    }



    private void setIsTranscoding(boolean isTranscoding, @Nullable String message) {
        mIsTranscoding = isTranscoding;
    }
}