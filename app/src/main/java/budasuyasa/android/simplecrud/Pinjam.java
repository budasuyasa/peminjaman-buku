package budasuyasa.android.simplecrud;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import budasuyasa.android.simplecrud.Config.ApiEndpoint;
import budasuyasa.android.simplecrud.Models.APIResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Pinjam extends AppCompatActivity {

    @BindView(R.id.tvNamaBuku)
    TextView tvNamaBuku;
    @BindView(R.id.etLamaPinjam)
    TextInputEditText etLamaPinjam;
    @BindView(R.id.etNamaPeminjam)
    TextInputEditText etNamaPeminjam;
    @BindView(R.id.btnPinjam)
    Button btnPinjam;

    String bookId;

    //Define okhttp dengan network interceptor agar mudah debug dengan Chrome
    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    Gson gson = new Gson(); //gson untuk handling json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);
        ButterKnife.bind(this);
        setTitle("Pinjam Buku");

        tvNamaBuku.setText("Pinjam: "+getIntent().getStringExtra("title"));
        bookId = getIntent().getStringExtra("bookId");

        btnPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinjam();
            }
        });

    }

    private void pinjam() {
        String nama = etNamaPeminjam.getText().toString();
        String lamaPinjam = etLamaPinjam.getText().toString();
        String URL = ApiEndpoint.PINJAM;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("bookId", bookId)
                .addFormDataPart("nama", nama)
                .addFormDataPart("lamaPinjam", lamaPinjam)
                .build();
        Request request = new Request.Builder()
                .url(URL) //Ingat sesuaikan dengan URL
                .post(requestBody)
                .build();

        //Handle response dari request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Pinjam.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(Pinjam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    Pinjam.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            //Finish activity
                            APIResponse res =  gson.fromJson(response.body().charStream(), APIResponse.class);
                            //Jika response success, finish activity
                            if(StringUtils.equals(res.getStatus(), "success")){
                                Toast.makeText(Pinjam.this, "Buku berhasil dipinjam!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                //Tampilkan error jika ada
                                Toast.makeText(Pinjam.this, "Error: "+res.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JsonSyntaxException e) {
                            Log.e("MainActivity", "JSON Errors:"+e.getMessage());
                        } finally {
                            response.body().close();
                        }
                        }
                    });

                } else {
                    Pinjam.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Pinjam.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
