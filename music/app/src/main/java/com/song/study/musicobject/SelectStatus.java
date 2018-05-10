package com.song.study.musicobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hhsj1 on 2018/5/10.
 */

public class SelectStatus implements Serializable {
    public static final int SELECTED = 1;
    public static final int UNSELECTED = 0;
    int isSelected = 0;

    public int isSelected() {
        return isSelected;
    }

    public void setSelected(int b) {
        this.isSelected = b;
    }

}
