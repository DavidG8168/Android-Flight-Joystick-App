package com.example.ex4;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
// The class draws and animates the joystick while notifying the observer of changes made.
// This class is an observable class, notifies the JoyStick class to send commands to the simulator.
public class JoyStickView extends View implements Observable {
    // X and Y variables to keep track of the current touch position on the screen.
    private float x;
    private float y;
    // Sizes for the oval.
    private float startOfWidth;
    private float startOfHeight;
    private float endOfWidth;
    private float endOfHeight;
    // The oval shape.
    private RectF oval;
    // Radius of the circle.
    private float radius;
    // Boolean used to tell if we can move the joystick position.
    private Boolean canMove = false;
    // A list for the observer.
    private List<Observer> observers = new LinkedList<>();
    // Initializes the radius of the joystick circle.
    public JoyStickView(Context context) {
        super(context);
        this.radius = 120;
    }
    // ***
    // Moves the joystick when touched. Handles the touch event.
    @SuppressWarnings("deprecation")
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
            // Check if we're touching the small joystick circle, then we can start moving.
            if (action == MotionEvent.ACTION_DOWN) {
                // If yes we can move (Check if the X and Y are inside of the circle shape).
                if(InsideCircle(event.getX(), event.getY())) {
                    this.canMove = true;
                }
                return true;
            }
            // Checks for movement.
            if (action == MotionEvent.ACTION_MOVE) {
                // If we can't move do nothing.
                if (!this.canMove) {
                    return true;
                }
                // Otherwise, notify the observer of the joystick movement.
                // Checks if the moving circle is inside the oval.
                // If yes, we can move it and UpdateInformation the values.
                if (InsideOval(event.getX(), event.getY())) {
                    // Get x/y values of the touch location.
                    this.x = event.getX();
                    this.y = event.getY();
                    // Notify.
                    notifyObservers(new Information(AileronNormalize(this.x),
                            ElevatorNormalize(this.y)));
                    // Redraw joystick after movement.
                    invalidate();
                }
                return true;
            }
            // If we stop touching the screen.
            if (action ==  MotionEvent.ACTION_UP) {
                // Can't move anymore, set to false.
                this.canMove = false;
                // Snap the joystick back to the center.
                SnapToPosition();
                // Redraw the joystick.
                invalidate();
                return true;
            }
        return true;
    }
    // ***
    // Draws the joystick on the canvas.
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // The rectangle will color the screen.
        Paint painter = new Paint();
        painter.setColor(Color.rgb(212,78,40));
        painter.setStrokeWidth(10);
        canvas.drawRect(0,0,getWidth(),getHeight(),painter);
        // The oval shape the joystick circle is inside.
        painter.setColor(Color.rgb(100,62,70));
        painter.setStrokeWidth(10);
        canvas.drawOval(this.oval, painter);
        // The joystick circle that can be moved.
        painter.setColor(Color.rgb(36,32,33));
        painter.setStrokeWidth(10);
        canvas.drawCircle(this.x, this.y, this.radius, painter);
    }
    // ***
    // Snaps the joystick back to the center position when let go.
    public void SnapToPosition() {
        // Get the center x and y positions of the screen.
        this.x = (float) getWidth() / 2;
        this.y = (float) getHeight() / 2;
        // Change the position.
        notifyObservers(new Information(AileronNormalize(this.x), ElevatorNormalize(this.y)));
    }
    // ***
    // Checks if we're touching the circle of the joystick.
    Boolean InsideCircle(float posX, float posY) {
        double distance = Math.sqrt((this.x - posX) * (this.x - posX) + (this.y - posY)
                * (this.y - posY));
        return (distance <= this.radius);
    }
    // ***
    // Change the sizes of the oval to fit every type of screen size.
    // by setting the oval sizes using the width and height of the screen.
    public void onSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.onSizeChanged(width, height, prevWidth, prevHeight);
        SnapToPosition();
        // Set sizes for the oval.
        // Start of the oval width.
        this.startOfWidth = (float) getWidth() / 8;
        // End of the oval width.
        this.endOfWidth = (float) getWidth() - ( (float) getWidth() / 8);
        // Start of the oval height.
        this.startOfHeight = (float) getHeight() / 8;
        // End of the oval height.
        this.endOfHeight = getHeight() - ((float)getHeight() / 8);
        // Initialize the oval with the set sizes.
        this.oval = new RectF(this.startOfWidth,this.startOfHeight, this.endOfWidth,
                this.endOfHeight);
    }
    // ***
    // Return true if we are inside the oval.
    Boolean InsideOval(float posX, float posY) {
        // Checks if the small circle shape is contained fully inside of the oval shape.
        return (this.oval.contains(posX, posY) &&
                this.oval.contains(posX, posY + radius) &&
                this.oval.contains(posX, posY - radius) &&
                this.oval.contains(posX + radius, posY) &&
                this.oval.contains(posX - radius, posY));
    }
    // ***
    // Normalize the Aileron.
    public float AileronNormalize(float x) {
        return (x - ((this.startOfWidth + this.endOfWidth)/2)) / ((this.endOfWidth
                - this.startOfWidth) / 2);
    }
    // ***
    // Normalize the Elevator.
    public float ElevatorNormalize(float y) {
        return (y - ((this.startOfHeight + this.endOfHeight)/2)) / ((this.startOfHeight
                - this.endOfHeight) / 2);
    }
    // ***
    // Adds this object as an observer.
    public void addToObserver(Observer observer) {
        this.observers.add(observer);
    }
    // ***
    // Notify of changes made in this object.
    public void notifyObservers(Information information) {
        for(Observer observer : this.observers) {
            observer.UpdateInformation(information);
        }
    }
}