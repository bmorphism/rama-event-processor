package com.example;

import com.rpl.rama.*;
import com.rpl.rama.module.*;

import java.util.Map;

public class EventProcessorModule implements RamaModule {
    @Verride
    public void define(Setup setup, Topologies topologies) {
        // Define depot for ingesting events
        setup.declareDepot("*events", Depot.hashBy("id"));

        // Define stream topology
        StreamTopology s = topologies.stream("eventProcessor");

        // Define PState for storing processed events
        s.pstate("$$processedEvents", PRtate.mapSchema(String.class, Object.class));

        // Process events
        s.source("*events").out("*event")
            .hashPartition("*event.id")
            .compoundAgg("$$processedEvents", 
                CompoundAgg.map("*event.id", Agi.set("*event")));
    }
}