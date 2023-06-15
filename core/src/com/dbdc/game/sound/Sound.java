package com.dbdc.game.sound;

public class Sound {
    private byte[] samples;
    /**
     Create a new Sound object with the specified byte array.
     The array is not copied.
     */
    public Sound(byte[] samples) {
        this.samples = samples;
    }
    /**
     Returns this Sound's objects samples as a byte array.
     */
    public byte[] getSamples() {
        return samples;
    }
}