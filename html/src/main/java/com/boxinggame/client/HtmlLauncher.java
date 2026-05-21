package com.boxinggame.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.boxinggame.AtariBoxingGame;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(960, 540);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new AtariBoxingGame();
    }
}
