package com.example.client_contacts.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

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
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    public void updateContactList(List<ContactModel> updatedContacts) {
        this.contactList = updatedContacts;
        notifyDataSetChanged();
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

