package com.chrisgya.springsecurity.model;

public enum PermissionEnum {
    CAN_READ_USERS("can_read_users"),
    CAN_LOCK_USER("can_lock_user"),
    CAN_UNLOCK_USER("can_unlock_user"),
    CAN_ENABLE_USER("can_enable_user"),
    CAN_DISABLE_USER("can_disable_user"),
    CAN_DELETE_USER("can_delete_user");

    private String name;

    PermissionEnum(String name) {
        this.name = name;
    }
}
