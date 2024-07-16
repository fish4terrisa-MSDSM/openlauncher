package com.freebrio.interfaces;

import com.freebrio.model.App;

import java.util.List;

public interface AppDeleteListener {
    boolean onAppDeleted(List<App> apps);
}
