package com.raptordev.raptor.api.event;

public interface MultiPhase<T extends RaptorClientMainEvent> {

    Phase getPhase();

    T nextPhase();
}