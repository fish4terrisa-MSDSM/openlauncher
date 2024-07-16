package com.freebrio.interfaces;

import com.freebrio.model.App;

import java.util.List;

public interface AppUpdateListener {
    boolean onAppUpdated(List<App> apps);
}
