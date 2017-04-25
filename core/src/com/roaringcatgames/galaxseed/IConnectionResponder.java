package com.roaringcatgames.galaxseed;

/**
 * Interface to define how to handle Connection Responses
 */
public interface IConnectionResponder {

    void onConnected();

    void onDisconnected();
}
