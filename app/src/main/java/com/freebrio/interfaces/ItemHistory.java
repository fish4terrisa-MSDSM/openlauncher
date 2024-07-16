package com.freebrio.interfaces;

import android.view.View;

import com.freebrio.model.Item;

public interface ItemHistory {
    void setLastItem(Item item, View view);

    void revertLastItem();

    void consumeLastItem();
}
