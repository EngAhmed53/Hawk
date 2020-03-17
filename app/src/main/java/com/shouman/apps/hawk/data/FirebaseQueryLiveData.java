package com.shouman.apps.hawk.data;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String TAG = "FirebaseQueryLiveData";
    private final Query query;
    private final MyValueEventListener myValueEventListener = new MyValueEventListener();
    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(myValueEventListener);
            listenerRemovePending = false;
        }
    };

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference ref) {
        this.query = ref;
    }

    @Override
    protected void onActive() {
        Log.e(TAG, "onActive: ");
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else {
            query.addValueEventListener(myValueEventListener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        Log.e(TAG, "onInactive: ");
        handler.postDelayed(removeListener, 10000);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "onCancelled: can't read from this reference");
        }
    }
}
