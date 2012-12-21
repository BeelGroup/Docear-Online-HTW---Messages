package controllers;

import configuration.SpringConfiguration;

public class ControllerFactory {

    /** Provides factory methods and should not instantiated. */
    private ControllerFactory() {
    }

    public static MindMap mindMap() {
        return SpringConfiguration.getBean(MindMap.class);
    }
}
