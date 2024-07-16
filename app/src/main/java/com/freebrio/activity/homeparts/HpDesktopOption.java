package com.freebrio.activity.homeparts;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.freebrio.R;
import com.freebrio.activity.HomeActivity;
import com.freebrio.interfaces.DialogListener;
import com.freebrio.manager.Setup;
import com.freebrio.model.Item;
import com.freebrio.util.AppSettings;
import com.freebrio.util.Definitions;
import com.freebrio.util.Tool;
import com.freebrio.viewutil.DialogHelper;
import com.freebrio.widget.CellContainer;
import com.freebrio.widget.Desktop;
import com.freebrio.widget.DesktopOptionView;

import java.util.List;

import static com.freebrio.activity.HomeActivity.REQUEST_CREATE_APPWIDGET;
import static com.freebrio.activity.HomeActivity.REQUEST_PICK_APPWIDGET;

public class HpDesktopOption implements DesktopOptionView.DesktopOptionViewListener, DialogListener.OnActionDialogListener {
    private HomeActivity _homeActivity;

    public HpDesktopOption(HomeActivity homeActivity) {
        _homeActivity = homeActivity;
    }

    public void onRemovePage() {
        if (_homeActivity.getDesktop().isCurrentPageEmpty()) {
            _homeActivity.getDesktop().removeCurrentPage();
            return;
        }
        DialogHelper.alertDialog(_homeActivity, _homeActivity.getString(R.string.remove), "This page is not empty. Those items will also be removed.", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                _homeActivity.getDesktop().removeCurrentPage();
            }
        });
    }

    public void onSetHomePage() {
        AppSettings appSettings = Setup.appSettings();
        appSettings.setDesktopPageCurrent(_homeActivity.getDesktop().getCurrentItem());
    }

    public void onPickWidget() {
        _homeActivity.ignoreResume = true;
        int appWidgetId = _homeActivity._appWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent("android.appwidget.action.APPWIDGET_PICK");
        pickIntent.putExtra("appWidgetId", appWidgetId);
        _homeActivity.startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    public void onPickAction() {
        Setup.eventHandler().showPickAction(_homeActivity, this);
    }

    public void onLaunchSettings() {
        Setup.eventHandler().showLauncherSettings(_homeActivity);
    }

    public void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt("appWidgetId", -1);
        AppWidgetProviderInfo appWidgetInfo = _homeActivity._appWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra("appWidgetId", appWidgetId);
            _homeActivity.startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = _homeActivity._appWidgetManager.getAppWidgetInfo(appWidgetId);
        Item item = Item.newWidgetItem(appWidgetInfo.provider, appWidgetId);
        Desktop desktop = _homeActivity.getDesktop();
        List<CellContainer> pages = desktop.getPages();
        item._spanX = (appWidgetInfo.minWidth - 1) / pages.get(desktop.getCurrentItem()).getCellWidth() + 1;
        item._spanY = (appWidgetInfo.minHeight - 1) / pages.get(desktop.getCurrentItem()).getCellHeight() + 1;
        Point point = desktop.getCurrentPage().findFreeSpace(item._spanX, item._spanY);
        if (point != null) {
            item._x = point.x;
            item._y = point.y;

            // add item to database
            _homeActivity._db.saveItem(item, desktop.getCurrentItem(), Definitions.ItemPosition.Desktop);
            desktop.addItemToPage(item, desktop.getCurrentItem());
        } else {
            Tool.toast(_homeActivity, R.string.toast_not_enough_space);
        }
    }

    @Override
    public void onAdd(int type) {
        Point pos = _homeActivity.getDesktop().getCurrentPage().findFreeSpace();
        if (pos != null) {
            _homeActivity.getDesktop().addItemToCell(Item.newActionItem(type), pos.x, pos.y);
        } else {
            Tool.toast(_homeActivity, R.string.toast_not_enough_space);
        }
    }
}
