package budasuyasa.android.simplecrud;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import budasuyasa.android.simplecrud.Adapter.BookAdapter;
import budasuyasa.android.simplecrud.Adapter.PinjamAdapter;
import budasuyasa.android.simplecrud.Adapter.RecyclerItemClickListener;
import budasuyasa.android.simplecrud.Config.ApiEndpoint;
import budasuyasa.android.simplecrud.Models.Book;
import budasuyasa.android.simplecrud.Models.Peminjaman;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class PinjamList extends AppCompatActivity {

    RecyclerView recyclerView;
    PinjamAdapter recycleAdapter;
    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
    private List<Peminjaman> bookList = new ArrayList<Peminjaman>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam_list);
        setTitle("Daftar Peminjaman");
        //Prepare RecycleView adapter
        recyclerView= (RecyclerView) findViewById(R.id.listViewPinjam);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),
                VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recycleAdapter = new PinjamAdapter(this, bookList );
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PinjamList.this,
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Peminjaman book = bookList.get(position);

//                Intent i = new Intent(MainActivity.this, Pinjam.class);
//                i.putExtra("bookId", book.getId());
//                i.putExtra("title", book.getName());
//                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        getBooks();
    }

    private void getBooks() {
        Request request = new Request.Builder()
                .url(ApiEndpoint.PINJAM)
                .build();

        //Handle response dari request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                PinjamList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(PinjamList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final ArrayList<Peminjaman> res = gson.fromJson(response.body().string(), new TypeToken<ArrayList<Peminjaman>>(){}.getType());
                        PinjamList.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bookList.clear();
                                bookList.addAll(res);
                                recycleAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        Log.e("MainActivity", "JSON Errors:"+e.getMessage());
                    } finally {
                        response.body().close();
                    }

                } else {
                    PinjamList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PinjamList.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
