package com.ccq.pojo.response;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class ResCardDTO {

    private int id;
    private String name;
    private String description;
    private boolean isActive;
    private Date dueDate;
    private Date reminderDate;
    private int position;
    private int listId;
    private ResUserDTO user;

    public ResCardDTO(int id, String name, String description, boolean isActive, Date dueDate, Date reminderDate, int position, int listId, ResUserDTO user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.dueDate = dueDate;
        this.reminderDate = reminderDate;
        this.position = position;
        this.listId = listId;
        this.user = user;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the reminderDate
     */
    public Date getReminderDate() {
        return reminderDate;
    }

    /**
     * @param reminderDate the reminderDate to set
     */
    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the listId
     */
    public int getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(int listId) {
        this.listId = listId;
    }

    public ResUserDTO getUser() {
        return user;
    }

    public void setUser(ResUserDTO user) {
        this.user = user;
    }
}
