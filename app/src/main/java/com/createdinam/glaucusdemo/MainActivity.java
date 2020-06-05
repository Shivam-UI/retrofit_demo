package com.createdinam.glaucusdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SpyInterface {
    List<Emails> mEmails;
    RecyclerView recyclerView;
    EmailListAdapter emailListAdapter;
    Retrofit retrofit;
    // alert mail picker
    AlertDialog dialogDetails = null;
    String emailPattern;
    ProgressDialog progress;
    // add new btn
    FloatingActionButton upload_new_feed;
    // retrofit API
    EmailListApiHolder emailListApiHolder;
    // call interface
    private SpyInterface mInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmails = new ArrayList<>();
        // new btn
        upload_new_feed = findViewById(R.id.upload_new_feed);
        upload_new_feed.setOnClickListener(this);
        // progress dialog
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait... :) ");
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // recycler view
        recyclerView = (RecyclerView) findViewById(R.id.emails_list_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // adapter
        emailListAdapter = new EmailListAdapter(mEmails, getApplicationContext(),MainActivity.this);
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://devfrontend.gscmaven.com/wmsweb/webapi/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        emailListApiHolder = retrofit.create(EmailListApiHolder.class);
        // view all list
        progress.show();
        viewEmailList();
        // create new email
        //addItems();
        //updateEmail();
    }

    private void updateEmail(int id,String email) {
        progress.show();
        Emails emails = new Emails(email,"true");
        Call<Emails> call = emailListApiHolder.putEmail(id,emails);
        call.enqueue(new Callback<Emails>() {
            @Override
            public void onResponse(Call<Emails> call, Response<Emails> response) {
                if (response.isSuccessful()) {
                    Log.d("update",""+response.isSuccessful());
                    Emails data = response.body();
                    mEmails.add(data);
                    emailListAdapter.setEmailListAdapter(mEmails);
                    recyclerView.setAdapter(emailListAdapter);
                    emailListAdapter.notifyDataSetChanged();
                    progress.hide();
                } else {
                    Log.d("error_inner", "" + response.message());
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<Emails> call, Throwable t) {
                Log.d("error_main", "" + t.getStackTrace());
                progress.hide();
            }
        });
    }

    private void addItems(String mail) {
        Log.d("new",""+mail);
        Emails emails = new Emails(mail, "true");
        Call<Emails> call = emailListApiHolder.createPost(emails);
        call.enqueue(new Callback<Emails>() {
            @Override
            public void onResponse(Call<Emails> call, Response<Emails> response) {
                if (response.isSuccessful()) {
                    Emails data = response.body();
                    mEmails.add(data);
                    emailListAdapter.setEmailListAdapter(mEmails);
                    recyclerView.setAdapter(emailListAdapter);
                    emailListAdapter.notifyDataSetChanged();
                    progress.hide();
                } else {
                    Log.d("error_inner", "" + response.message());
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<Emails> call, Throwable t) {
                Log.d("error_main", "" + t.getStackTrace());
                progress.hide();
            }
        });
    }

    private void viewEmailList() {
        Call<List<Emails>> call = emailListApiHolder.getEmail();
        call.enqueue(new Callback<List<Emails>>() {
            @Override
            public void onResponse(Call<List<Emails>> call, Response<List<Emails>> response) {
                if (response.isSuccessful()) {
                    mEmails = response.body();
                    emailListAdapter.setEmailListAdapter(mEmails);
                    recyclerView.setAdapter(emailListAdapter);
                    progress.hide();
                } else {
                    Log.d("error_inner", "" + response.code());
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<List<Emails>> call, Throwable t) {
                Log.d("error_main", "" + t.getLocalizedMessage());
                progress.hide();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_new_feed:
                addNewEmailItems();
                break;
        }
    }

    private void addNewEmailItems() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert_layout, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = dialogView.findViewById(R.id.dialog_txt_name);
        final Button btnAdd = dialogView.findViewById(R.id.btn_add);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        dialogBuilder.setTitle("Add New Email");
        dialogBuilder.setMessage("Please Enter Correct Email!");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "enter email address", Toast.LENGTH_SHORT).show();
                } else {
                    if (edt.getText().toString().trim().matches(emailPattern)) {
                        dialogDetails.hide();
                        addItems(edt.getText().toString().trim());
                        progress.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetails.hide();
            }
        });
        dialogDetails = dialogBuilder.create();
        dialogDetails.show();
    }

    @Override
    public void foo(int id,String text) {
        Log.d("update_foo",id+" - "+text);
        updateEmail(id,text);
    }

    @Override
    public void fooDelete(int id) {
        deleteEmail(id);
    }

    private void deleteEmail(int id) {
        Call<Void> call = emailListApiHolder.deleteEmail(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("delete_resp",""+response.isSuccessful());
                    viewEmailList();
                    progress.hide();
                } else {
                    Log.d("d_error_inner", "" + response.message());
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("d_error_main", "" + t.getStackTrace());
                progress.hide();
            }
        });
    }
}