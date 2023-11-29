package com.example.client_contacts.views;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.interfaces.ContactDeletedListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.services.NetworkService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactModel> contactList;
    private Context context;

    public ContactAdapter(List<ContactModel> contactList, Context context) {
        this.contactList = contactList != null ? contactList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactModel contact = contactList.get(position);
        holder.bind(contact);

        holder.itemView.setOnLongClickListener(v -> {
            deleteItem(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    public void updateContactList(List<ContactModel> updatedContacts) {
        this.contactList = updatedContacts;
        notifyDataSetChanged();
    }

    private void deleteItem(int position) {
        NetworkService networkService = new NetworkService();
        ContactModel contactToBeDeleted = contactList.get(position);

        networkService.deleteContact(contactToBeDeleted.getId(), new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    contactList.remove(position);
                    notifyItemRemoved(position);
                    showDeletionFeedback();
                    Log.i("Success", "Contact Deleted");
                    return;
                }
                Log.i("Failed", "Contact Not Deleted!");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Failed", "Contact Not Deleted! " + t.getMessage());
            }
        });
    }

    private void showDeletionFeedback() {
        Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                "Contact deleted", Snackbar.LENGTH_SHORT).show();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtPhoneNumber;
        private TextView txtEmail;
        private ImageView contactPhoto;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            contactPhoto = itemView.findViewById(R.id.contactPhoto);
        }

        public void bind(ContactModel contact) {
            txtName.setText(contact.getContactName());
            txtPhoneNumber.setText(contact.getPhoneNumber());
            txtEmail.setText(contact.getEmail());

            // Set the photo if available
            if (contact.getPhoto() != null) {
                contactPhoto.setImageBitmap(contact.getPhoto());
            } else {
                contactPhoto.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

}

