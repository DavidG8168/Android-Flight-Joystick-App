package com.example.ex4;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
// The main activity, the login activity where the IP and PORT are given
// and the connection is started when pressing the SEND button.
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    // Starts the joystick activity when clicking the SEND button.
    // Server connection is handled in the joystick activity.
    public void StartConnection(View view) {
        // Create the intent.
        Intent intent = new Intent(this, JoyStick.class);
        // Get the text from the IP text box.
        EditText IP = findViewById(R.id.IP_INPUT);
        // Convert the IP to a string.
        String IP_STRING = IP.getText().toString();
        // Pass the IP to the intent.
        intent.putExtra("ip", IP_STRING);
        // Do the same for the PORT.
        EditText PORT = findViewById(R.id.PORT_INPUT);
        String PORT_STRING = PORT.getText().toString();
        intent.putExtra("port", PORT_STRING);
        // Start the intent.
        startActivity(intent);
    }
}