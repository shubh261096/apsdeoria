package com.pb.apszone.view.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.pb.apszone.utils.AppConstants.KEY_PDF_SUBJECT_NAME;
import static com.pb.apszone.utils.AppConstants.KEY_PDF_URL;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class RemotePDFActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_PDF = "temp_preview.pdf";
    @BindView(R.id.toolbar_pdf)
    Toolbar toolbarPdf;
    @BindView(R.id.pdf_preview_image_view)
    ImageView pdfPreviewImageView;
    @BindView(R.id.previous_page_view)
    ImageButton previousPageView;
    @BindView(R.id.pdf_page_counter_text_view)
    TextView pdfPageCounterTextView;
    @BindView(R.id.next_page_view)
    ImageButton nextPageView;
    @BindView(R.id.pdf_control_panel)
    LinearLayout pdfControlPanel;


    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;

    /**
     * Maintain status of PDF is loaded or failed to load.
     */
    protected boolean isPdfLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_pdf);
        ButterKnife.bind(this);
        loadData();
        toolbarPdf.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Entry point for loading intent information.
     * Override it to handle specialized mode in case of extending this class.
     */
    protected void loadData() {
        String title = getIntent().getStringExtra(KEY_PDF_SUBJECT_NAME);
        toolbarPdf.setTitle(title);
        String url = getIntent().getStringExtra(KEY_PDF_URL);
        if (!TextUtils.isEmpty(url)) {
            loadPdfPreviewUrl(url);
            return;
        }
        pdfPreviewImageView.setScaleType(ImageView.ScaleType.CENTER);
    }

    public final void loadPdfPreviewUrl(String url) {
        showProgress(this, getString(R.string.msg_please_wait));
        final File file = new File(getCacheDir(), FILE_PDF);
        OkHttpClient client = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> showPreviewFailed());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                handlePDFResponse(file, response);
            }
        });
    }

    private void handlePDFResponse(final File file, Response response) {
        ResponseBody body = response.isSuccessful() ? response.body() : null;
        if (body != null) {
            try {
                InputStream inputStream = body.byteStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[64 * 1024];
                int count;
                while ((count = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> loadPdfPreviewFile(file));
            } catch (IOException io) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(this::showPreviewFailed);
            }
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(this::showPreviewFailed);
        }
    }

    public final void loadPdfPreviewFile(File file) {
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            // This is the PdfRenderer we use to render the PDF.
            if (mFileDescriptor != null) {
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
                mPdfRenderer.shouldScaleForPrinting();
                isPdfLoaded = true;
                showPage(0);
            }
        } catch (Exception e) {
            showPreviewFailed();
        }
        hideProgress();
    }

    protected void showPreviewFailed() {
        hideProgress();
        isPdfLoaded = false;
        Toast.makeText(this, getString(R.string.msg_failed_load_pdf), Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the {@link PdfRenderer} and related resources.
     *
     * @throws IOException When the PDF file cannot be closed.
     */
    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        if (mPdfRenderer != null) {
            mPdfRenderer.close();
        }
        if (mFileDescriptor != null) {
            mFileDescriptor.close();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                closeRenderer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }

        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).

        Bitmap bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics().densityDpi * mCurrentPage.getWidth() / 72,
                getResources().getDisplayMetrics().densityDpi * mCurrentPage.getHeight() / 72, Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
        // We are ready to show the Bitmap to user.
        pdfPreviewImageView.setImageBitmap(bitmap);
        updateUIOnPageShown();
    }

    /**
     * Called when PDF page rendered on Image.
     */
    protected void updateUIOnPageShown() {
        if (getPageCount() > 1) {
            pdfControlPanel.setVisibility(View.VISIBLE);
            previousPageView.setVisibility(mCurrentPage.getIndex() > 0 ? View.VISIBLE : View.INVISIBLE);
            nextPageView.setVisibility(mCurrentPage.getIndex() < getPageCount() - 1 ? View.VISIBLE : View.INVISIBLE);
            pdfPageCounterTextView.setText(String.format(Locale.getDefault(), "%d/%d", mCurrentPage.getIndex() + 1, getPageCount()));
        } else {
            pdfControlPanel.setVisibility(View.GONE);
        }
    }

    /**
     * Gets the number of pages in the PDF.
     *
     * @return The number of pages.
     */
    public final int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

    @Override
    public void onClick(View view) {
        if (mCurrentPage != null) {

            int viewId = view.getId();
            if (viewId == R.id.previous_page_view) {
                showPage(mCurrentPage.getIndex() - 1);
            } else if (viewId == R.id.next_page_view) {
                showPage(mCurrentPage.getIndex() + 1);
            }
        }
    }

    /**
     * Launch PDF Renderer for URL (Public) PDF file
     *
     * @param context calling context
     * @param title   activity title
     * @param url     URL string
     */
    public static void launchForUrl(Context context, String title, String url) {
        Intent intent = new Intent(context, RemotePDFActivity.class);
        intent.putExtra(KEY_PDF_URL, url);
        intent.putExtra(KEY_PDF_SUBJECT_NAME, title);
        context.startActivity(intent);
    }

    @OnClick({R.id.previous_page_view, R.id.next_page_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previous_page_view:
                if (mCurrentPage != null) {
                    showPage(mCurrentPage.getIndex() - 1);
                }
                break;
            case R.id.next_page_view:
                if (mCurrentPage != null) {
                    showPage(mCurrentPage.getIndex() + 1);
                }
                break;
        }
    }
}
