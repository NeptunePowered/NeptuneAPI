package org.neptunepowered.lib.factory;

public class Factory {

    private BanFactory banFactory = new BanFactory();

    public BanFactory getBanFactory() {
        return banFactory;
    }
}
