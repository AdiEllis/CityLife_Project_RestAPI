package com.project.Models;

public class Street extends BaseEntitie {
    private String name;
    private int colonyId;

    public Street() {}
    public Street(int oid, boolean deleted, String name, int colonyId) {
        super(oid, deleted);
        this.name = name;
        this.colonyId = colonyId;
    }
    public Street(boolean deleted, String name, int colonyId) {
        super(deleted);
        this.name = name;
        this.colonyId = colonyId;
    }
    public boolean objectIsEmpty() {
        if (isEmpty(this.name) || this.colonyId == 0) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColonyId() {
        return colonyId;
    }

    public void setColonyId(int colonyId) {
        this.colonyId = colonyId;
    }
}
