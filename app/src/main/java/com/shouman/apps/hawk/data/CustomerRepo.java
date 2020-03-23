package com.shouman.apps.hawk.data;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.model.Customer;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference salesMemberReference = database.getReference().child("sales_members");
    private static DatabaseReference customersReference = database.getReference().child("customers");


    //add new customer to database
    public static void addNewCustomerToDatabase(Context context, Customer customer) {
        String userPath = UserPreference.getUserType(context);
        String userUID = UserPreference.getUserUID(context);

        //get the new branch added UID;
        String newCustomerKey = customersReference.push().getKey();

        //add customer to the  node
        customersReference.child(newCustomerKey).setValue(customer);

        //add this new customer to salesMember customersList
        //get the date
        String date = SimpleDateFormat.getDateInstance().format(new Date());
        //update the values in database
        Map<String, Object> newValue = new HashMap<>();
        newValue.put(newCustomerKey, customer.getCn());
        database.getReference()
                .child(userPath)
                .child(userUID)
                .child("customersLog")
                .child(date)
                .updateChildren(newValue);
    }


    //return sales member customers list reference
    public static DatabaseReference getCustomersList(String salesUID) {
        return salesMemberReference.child(salesUID).child("customersLog");
    }

}
