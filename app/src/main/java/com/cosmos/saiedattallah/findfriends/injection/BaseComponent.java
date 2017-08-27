package com.cosmos.saiedattallah.findfriends.injection;

public class BaseComponent {
    protected BaseComponent() {
        GraphProvider graphProvider = GraphProvider.getInstance();
        graphProvider.getGraph().inject(this);
    }
}
