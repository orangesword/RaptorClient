package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

public class PlayerTravelEvent extends RaptorClientMainEvent {

    public float Strafe;
    public float Vertical;
    public float Forward;

    public PlayerTravelEvent(float Strafe, float Vertical, float Forward)
    {
        this.Strafe = Strafe;
        this.Vertical = Vertical;
        this.Forward = Forward;
    }

}
