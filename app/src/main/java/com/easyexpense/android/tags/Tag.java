package com.easyexpense.android.tags;

/**
 * Created by Mani on 13/03/17.
 */

public class Tag {

    private String tagString;

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    private boolean isDefault;

}
