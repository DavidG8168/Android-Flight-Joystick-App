package com.example.ex4;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// The Joystick class is in charge of starting the connection to the
// simulator and apart from displaying the joystick, sending the commands
// to the simulator.
// This class is an  observer, it is notified from the JoyStickView class when to send updates to
// the simulator.
public class JoyStick extends AppCompatActivity implements Observer {
    // Create a client.
    private ServerConnection serverConnection;
    // ***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        Intent intent = getIntent();
        // Try connecting
        try {
            // Initialize the client.
            this.serverConnection = new ServerConnection();
            // Start the asynchronous connection.
            this.serverConnection.execute(intent.getStringExtra("ip"),
                    intent.getStringExtra("port"), null);
        }
        // Catch failure.
        catch(Exception e) {
            System.out.println("" + e.getMessage());
        }
        // Create a joystick using this activity as a context.
        JoyStickView joyStickView = new JoyStickView(this);
        // Add the joystick as an observer.
        joyStickView.addToObserver(this);
        // Start the joystick.
        setContentView(joyStickView);
    }
    // ***
    // Send the movements to the client to be sent to the server.
    public void UpdateInformation(Information information){
        serverConnection.Send("set /controls/flight/aileron " + information.getAileron());
        serverConnection.Send("set /controls/flight/elevator " + information.getElevator());
    }
    // ***
    // When the activity is stopped, close the client.
    public void onDestroy() {
        this.serverConnection.HaltConnection();
        super.onDestroy();
    }
}
