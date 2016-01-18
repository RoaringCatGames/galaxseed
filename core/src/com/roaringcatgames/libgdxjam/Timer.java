package com.roaringcatgames.libgdxjam;

/**
 * Created by barry on 1/14/16 @ 9:08 PM.
 */
public class Timer {

    public float elapsedTime = 0f;
    public float lastTriggerTime = 0f;

    private float secondsBetweenTriggers;

    public Timer(float triggersPerSecond){
        this.secondsBetweenTriggers = 1f/triggersPerSecond;
        this.elapsedTime = 0f;
        this.lastTriggerTime = 0f;
    }

    public boolean doesTriggerThisStep(float deltaStep){
        elapsedTime += deltaStep;
        boolean doesTrigger = (elapsedTime - lastTriggerTime >= secondsBetweenTriggers);

        if(doesTrigger){
            lastTriggerTime = elapsedTime;
        }
        return doesTrigger;
    }
}
