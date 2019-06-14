package com.example.ex4;
// Class will hold the information we are interested in from the simulator.
public class Information {
    private Float elevator;
    private Float aileron;
    // The class constructor.
    public Information(float a, float e) {
        this.elevator = e;
        this.aileron = a;
    }
    // Get the elevator.
    public String getElevator() {
        return this.elevator.toString();
    }
    // Get the aileron.
    public String getAileron() {
        return this.aileron.toString();
    }
}
