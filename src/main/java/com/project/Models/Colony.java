package com.project.Models;

public class Colony extends BaseEntitie{
    private String enColonyName;
    private String heColonyName;

    public Colony(int oid, String enColonyName, String heColonyName, boolean deleted) {
        super(oid, deleted);
        this.enColonyName = enColonyName;
        this.heColonyName = heColonyName;
    }

    public Colony(String enColonyName, String heColonyName ,boolean deleted) {
        super(deleted);
        this.enColonyName = enColonyName;
        this.heColonyName = heColonyName;
    }

    public Colony() {}
    public boolean objectIsEmpty() {
        if (isEmpty(this.enColonyName) ||  isEmpty(this.heColonyName)) {
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
    public String getEnColonyName() {
        return enColonyName;
    }

    public void setEnColonyName(String enColonyName) {
        this.enColonyName = enColonyName;
    }

    public String getHeColonyName() {
        return heColonyName;
    }

    public void setHeColonyName(String heColonyName) {
        this.heColonyName = heColonyName;
    }
}
