package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roaringcatgames.libgdxjam.screens.IScreenDispatcher;
import com.roaringcatgames.libgdxjam.screens.SpaceScreen;
import com.roaringcatgames.libgdxjam.values.GameState;

import java.util.ArrayList;

/**
 * Created by barry on 12/22/15 @ 7:23 PM.
 */
public class ScreenDispatcher implements IScreenDispatcher {

    public ArrayList<Screen> screens;
    private boolean isCurrenScreenEnded = false;
    private int currentIndex = 0;

    private SpriteBatch batch;

    public ScreenDispatcher(SpriteBatch batch){
        this.batch = batch;
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
            if(currentIndex == 2){
                screens.set(2, new SpaceScreen(batch, this));
            }
        }

        if(screens.size() > currentIndex){
            return screens.get(currentIndex);
        }else{
            currentIndex = 0;
            if(App.getState() == GameState.MENU) {
                currentIndex++;
            }

            return screens.get(currentIndex);
        }
    }
}
