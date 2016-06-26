package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.libgdxjam.screens.IScreenDispatcher;
import com.roaringcatgames.libgdxjam.screens.MenuScreen;
import com.roaringcatgames.libgdxjam.screens.SpaceScreen;
import com.roaringcatgames.libgdxjam.values.GameState;

import java.util.ArrayList;

/**
 * Created by barry on 12/22/15 @ 7:23 PM.
 */
public class ScreenDispatcher implements IScreenDispatcher {

    private IGameProcessor gameProcessor;
    public ArrayList<Screen> screens;
    private boolean isCurrenScreenEnded = false;
    private int currentIndex = 0;


    public ScreenDispatcher(IGameProcessor game){
        this.gameProcessor = game;
        screens = new ArrayList<>();
    }

    public void AddScreen(Screen screen){
        screens.add(screen);
    }

    @Override
    public void endCurrentScreen() {
        isCurrenScreenEnded = true;
    }

    @Override
    public Screen getNextScreen() {
        if(isCurrenScreenEnded){
            isCurrenScreenEnded = false;
            //Do logic to pick the next screen
            currentIndex++;

            if(screens.size() <= currentIndex){
                currentIndex = 0;
                if(App.getState() == GameState.MENU){
                    currentIndex++;
                }
            }

            if(currentIndex == 1){
                screens.set(1, new MenuScreen(gameProcessor, this));
            }
            if(currentIndex == 2){
                screens.set(2, new SpaceScreen(gameProcessor, this));
            }
        }

        return screens.get(currentIndex);

    }
}
