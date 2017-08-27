package com.cosmos.saiedattallah.findfriends;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.cosmos.saiedattallah.findfriends.injection.GraphProvider;
import com.cosmos.saiedattallah.findfriends.injection.InjectionModule;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class App extends MultiDexApplication {



    private ObjectGraph applicationGraph;

    @Inject
    public App(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GraphProvider graphProvider = GraphProvider.getInstance();
        graphProvider.addModules(getModules().toArray());
        this.applicationGraph = graphProvider.getGraph();
        this.applicationGraph.inject(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }

    /**
     * A list of modules to use for the application graph. Subclasses can override this method to
     * provide additional modules provided they call {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new InjectionModule(this));

    }

    ObjectGraph getApplicationGraph() {
        return this.applicationGraph;
    }


}