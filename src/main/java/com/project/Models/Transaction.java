package com.project.Models;

public class Transaction extends BaseEntitie {
    private String date;
    private String typeAction;
    private String descriptionAction;
    private int colonyID;

    public Transaction() {}

    public Transaction(int oid, boolean deleted, String date, String typeAction, String descriptionAction, int colonyID) {
        super(oid, deleted);
        this.date = date;
        this.typeAction = typeAction;
        this.descriptionAction = descriptionAction;
        this.colonyID = colonyID;
    }
    public Transaction(boolean deleted, String date, String typeAction, String descriptionAction, int colonyID) {
        super(deleted);
        this.date = date;
        this.typeAction = typeAction;
        this.descriptionAction = descriptionAction;
        this.colonyID = colonyID;
    }
    public boolean objectIsEmpty() {
        if (isEmpty(this.date) ||  isEmpty(this.typeAction) || isEmpty(this.descriptionAction) || this.colonyID == 0) {
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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public String getDescriptionAction() {
        return descriptionAction;
    }

    public void setDescriptionAction(String descriptionAction) {
        this.descriptionAction = descriptionAction;
    }

    public int getColonyID() {
        return colonyID;
    }

    public void setColonyID(int colonyID) {
        this.colonyID = colonyID;
    }
}
