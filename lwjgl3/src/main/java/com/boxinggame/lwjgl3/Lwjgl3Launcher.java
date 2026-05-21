package com.boxinggame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.boxinggame.AtariBoxingGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Atari Boxing Prototype");
        config.setWindowedMode(960, 540);
        config.useVsync(true);
        new Lwjgl3Application(new AtariBoxingGame(), config);
    }
}
