package com.mytestfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static final String RTAG = "Rooms";
    private static final String DTAG = "Devices";
    private FirebaseFirestore datab;

    public Database(FirebaseFirestore db){
        datab = db;
    }
    /////////////////////////////////////////////////////////
    /////////////DATABASE CODE///////////////////////////////
    /////////////////////////////////////////////////////////


    public void addNewRoom(String name) {
        //create new room with a name
        Map<String, Object> room = new HashMap<>();
        room.put("Name", name);
        // Add new room to collection with a new ID
        datab.collection("rooms")
                .add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(RTAG, "Added new room with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(RTAG, "Could not add new room", e);
                    }
                });
    }

    public void addNewDevice(final Device newDev, String room){
        //create new device with a name, type, and ID
        Map<String, Object> device = new HashMap<>();
        device.put("Name", newDev.name);
        device.put("Type", newDev.type);
        device.put("ID", newDev.idVal);
        device.put("data", newDev.data);
        device.put("device", newDev);
        // Add new device to subcollection of specified room with a new ID
        datab.collection("rooms").document(room).collection("devices")
                .add(device)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(DTAG, "Added new device with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DTAG, "Could not add new device", e);
                    }
                });
        //add a snapshot listener whenever a new device is added to listen for new data
        final DocumentReference deviceRef = datab.collection("rooms").document(room).collection("devices").document(newDev.name);
        deviceRef
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot deviceSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(RTAG, "Failed to listen", e);
                            return;
                        }
                        if(deviceSnapshot != null && deviceSnapshot.exists() && (newDev.type.equals("Thermometer"))){
                            Log.d(DTAG, "Current temperature: " + deviceSnapshot.get("data"));
                        }else if(deviceSnapshot != null && deviceSnapshot.exists() && (newDev.type.equals("Hygrometer"))){
                            Log.d(DTAG, "Current humidity: " + deviceSnapshot.get("data"));
                        }else{
                            Log.d(DTAG, "Null");
                        }
                    }
                });
    }

    public void deleteDevice(String name, String room) {
        //delete device from specified room subcollection
        final DocumentReference deviceRef = datab.collection("rooms").document(room).collection("devices").document(name);
        deviceRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DTAG, "Device was deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DTAG, "Device was not deleted");
                    }
                });
    }

    public void deleteRoom(String room) {
        //delete room from collection
        final DocumentReference roomRef =  datab.collection("rooms").document(room);
        roomRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(RTAG, "Room was deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(RTAG, "Room was not deleted");
                    }
                });
    }
}
