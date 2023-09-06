package com.example.notificationsender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase (you can do this in your Application class as well)
         FirebaseApp.initializeApp(this);

        // Retrieve the FCM token when the app is opened
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Toast.makeText(MainActivity.this, "Your device registration token is " + token, Toast.LENGTH_SHORT).show();

                        // Store the token in Firestore
                        storeTokenInFirestore(token);
                    }
                });
    }
    public class TokenData {
        private String token;

        // Default constructor (required for Firestore)
        public TokenData() {
            // Default constructor required for Firestore
        }

        public TokenData(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }


    private void storeTokenInFirestore(String token) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TokenData tokenData = new TokenData(token);
        String collectionName = "tokens";
        String documentId = "myUCPFcRfcCT2KheZfIr"; // Implement this function

        // Store the token in Firestore
        db.collection("tokens").document("myUCPFcRfcCT2KheZfIr")
                .set(new TokenData(token))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Token stored successfully
                            Toast.makeText(MainActivity.this, "Token stored in Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            // Token storage failed
                            Toast.makeText(MainActivity.this, "Failed to store token in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private String generateUniqueDocumentId() {
        // Implement your logic to generate a unique ID here
        // You can use the user's ID if available or generate a random one
        return "unique_document_id"; // Replace with your logic
    }
}

