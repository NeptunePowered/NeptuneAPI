package org.neptunepowered.lib;

import net.canarymod.plugin.Plugin;
import org.neptunepowered.lib.factory.Factory;

public class Neptune extends Plugin {

    private static Factory factory = new Factory();

    public static Factory getFactory() {
        return factory;
    }

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public void disable() {

    }
}
