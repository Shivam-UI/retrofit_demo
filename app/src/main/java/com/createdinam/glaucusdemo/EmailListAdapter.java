package com.createdinam.glaucusdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.EmailHolder> {
    List<Emails> emails;
    Context mContext;
    int id;
    String email = "";
    String emailPattern;
    AlertDialog dialogDetails = null;
    // call interface
    private SpyInterface mInterface;

    public EmailListAdapter(List<Emails> emails, Context mContext,SpyInterface spyInterface) {
        this.emails = emails;
        this.mContext = mContext;
        this.mInterface = spyInterface;
    }

    public void setEmailListAdapter(List<Emails> emails) {
        this.emails = emails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmailListAdapter.EmailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_items, parent, false);
        return new EmailHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailListAdapter.EmailHolder holder, final int position) {
        id = emails.get(position).getIdtableEmail();
        email = emails.get(position).getTableEmailEmailAddress();
        holder.emailText.setText(email);
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setTitle("Delete Email");
                dialogBuilder.setMessage("Delete : "+email);
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDetails.hide();
                    }
                });
                dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mInterface.fooDelete(id);
                    }
                });
                dialogDetails = dialogBuilder.create();
                dialogDetails.show();
            }
        });
        holder.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(mContext);
                final View dialogView = inflater.inflate(R.layout.dialog_alert_layout, null);
                dialogBuilder.setView(dialogView);
                final EditText edt = dialogView.findViewById(R.id.dialog_txt_name);
                edt.setText(emails.get(position).getTableEmailEmailAddress());
                final Button btnAdd = dialogView.findViewById(R.id.btn_add);
                final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
                dialogBuilder.setTitle("Update Email");
                dialogBuilder.setMessage("Please Enter Correct Email!");
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edt.getText().toString().isEmpty()) {
                            Toast.makeText(v.getContext(), "enter email address", Toast.LENGTH_SHORT).show();
                        } else {
                            if (edt.getText().toString().trim().matches(emailPattern)) {
                                Log.d("update", "" + edt.getText().toString().trim());
                                mInterface.foo(id,edt.getText().toString().trim());
                                dialogDetails.hide();
                            } else {
                                Toast.makeText(v.getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public int getItemCount() {
        if (emails != null) {
            return emails.size();
        }
        return 0;
    }

    public static class EmailHolder extends RecyclerView.ViewHolder {
        TextView emailText;
        Button updatebtn, deletebtn;

        public EmailHolder(@NonNull View itemView) {
            super(itemView);
            // init
            emailText = itemView.findViewById(R.id.emailText);
            updatebtn = itemView.findViewById(R.id.updatebtn);
            deletebtn = itemView.findViewById(R.id.deletebtn);
        }
    }
}
