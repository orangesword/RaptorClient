package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

public class ReachDistanceEvent extends RaptorClientMainEvent {

    private float distance;

    public ReachDistanceEvent(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}