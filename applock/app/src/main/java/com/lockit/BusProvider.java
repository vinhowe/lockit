package com.lockit;

public class BusProvider {
    static MainThreadBus BUS = new MainThreadBus();

    public static MainThreadBus bus() {
        return BUS;
    }
}
