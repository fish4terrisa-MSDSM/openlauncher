package com.freebrio.activity.homeparts;

import android.content.Context;
import android.support.annotation.NonNull;

import com.freebrio.AppObject;
import com.freebrio.manager.Setup;
import com.freebrio.util.AppManager;
import com.freebrio.util.AppSettings;
import com.freebrio.util.DatabaseHelper;
import com.freebrio.viewutil.DesktopGestureListener.DesktopGestureCallback;

public final class HpInitSetup extends Setup {
    private final AppManager _appLoader;
    private final DatabaseHelper _dataManager;
    private final HpGestureCallback _desktopGestureCallback;
    private final HpEventHandler _eventHandler;
    private final AppSettings _appSettings;

    public HpInitSetup(Context context) {
        _appSettings = AppSettings.get();
        _desktopGestureCallback = new HpGestureCallback(_appSettings);
        _dataManager = new DatabaseHelper(context.getApplicationContext());
        _appLoader = AppManager.getInstance(context.getApplicationContext());
        _eventHandler = new HpEventHandler();
    }

    @NonNull
    public Context getAppContext() {
        return AppObject.get();
    }

    @NonNull
    public AppSettings getAppSettings() {
        return _appSettings;
    }

    @NonNull
    public DesktopGestureCallback getDesktopGestureCallback() {
        return _desktopGestureCallback;
    }

    @NonNull
    public DatabaseHelper getDataManager() {
        return _dataManager;
    }

    @NonNull
    public AppManager getAppLoader() {
        return _appLoader;
    }

    @NonNull
    public EventHandler getEventHandler() {
        return _eventHandler;
    }
}