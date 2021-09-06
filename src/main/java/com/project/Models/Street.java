package com.project.Models;

public class Street extends BaseEntitie {
    private String name;
    private int colonyID;

    public Street() {}
    public Street(int oid, boolean deleted, String name, int colonyID) {
        super(oid, deleted);
        this.name = name;
        this.colonyID = colonyID;
    }
    public Street(boolean deleted, String name, int colonyID) {
        super(deleted);
        this.name = name;
        this.colonyID = colonyID;
    }
    public boolean objectIsEmpty() {
        if (isEmpty(this.name) || this.colonyID == 0) {
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

    public int getColonyID() {
        return colonyID;
    }

    public void setColonyID(int colonyID) {
        this.colonyID = colonyID;
    }
}
