package com.freebrio.manager;

import android.content.Context;

import com.freebrio.interfaces.DialogListener;
import com.freebrio.model.Item;
import com.freebrio.util.AppManager;
import com.freebrio.util.AppSettings;
import com.freebrio.util.DatabaseHelper;
import com.freebrio.viewutil.DesktopGestureListener;

public abstract class Setup {
    private static Setup _setup = null;

    public static boolean wasInitialised() {
        return _setup != null;
    }

    public static void init(Setup setup) {
        Setup._setup = setup;
    }

    public static Setup get() {
        if (_setup == null) {
            throw new RuntimeException("Setup has not been initialised!");
        }
        return _setup;
    }

    public static Context appContext() {
        return get().getAppContext();
    }

    public static AppSettings appSettings() {
        return get().getAppSettings();
    }

    public static DesktopGestureListener.DesktopGestureCallback desktopGestureCallback() {
        return get().getDesktopGestureCallback();
    }

    public static DatabaseHelper dataManager() {
        return get().getDataManager();
    }

    public static AppManager appLoader() {
        return get().getAppLoader();
    }

    public static EventHandler eventHandler() {
        return get().getEventHandler();
    }

    public abstract Context getAppContext();

    public abstract AppSettings getAppSettings();

    public abstract DesktopGestureListener.DesktopGestureCallback getDesktopGestureCallback();

    public abstract DatabaseHelper getDataManager();

    public abstract AppManager getAppLoader();

    public abstract EventHandler getEventHandler();

    public interface EventHandler {
        void showLauncherSettings(Context context);

        void showPickAction(Context context, DialogListener.OnActionDialogListener listener);

        void showEditDialog(Context context, Item item, DialogListener.OnEditDialogListener listener);

        void showDeletePackageDialog(Context context, Item item);
    }
}
