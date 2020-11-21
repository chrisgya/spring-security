package com.chrisgya.springsecurity.model;

import com.chrisgya.springsecurity.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    // if username == null then specification is ignored
    public static Specification<User> userUsernameEquals(String username) {
        return (root, query, builder) ->
                username == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("username"), username);
    }

    // if email == null then specification is ignored
    public static Specification<User> userEmailEquals(String email) {
        return (root, query, builder) ->
                email == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("username"), email);
    }

    // if firstname == null then specification is ignored
    public static Specification<User> userFirstnameEquals(String firstName) {
        return (root, query, builder) ->
                firstName == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("first_name"), firstName);
    }

    // if lastname == null then specification is ignored
    public static Specification<User> userLastnameEquals(String lastName) {
        return (root, query, builder) ->
                lastName == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("last_name"), lastName);
    }

    // if isLocked == null then specification is ignored
    public static Specification<User> userIsLockedEquals(Boolean isLocked) {
        return (root, query, builder) ->
                isLocked == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("is_locked"), isLocked);
    }

    // if isEnabled == null then specification is ignored
    public static Specification<User> userIsEnabledEquals(Boolean isEnabled) {
        return (root, query, builder) ->
                isEnabled == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("is_enabled"), isEnabled);
    }

    // if isConfirmed == null then specification is ignored
    public static Specification<User> userIsConfirmedEquals(Boolean isConfirmed) {
        return (root, query, builder) ->
                isConfirmed == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("is_confirmed"), isConfirmed);
    }

    // if isDeleted == null then specification is ignored
    public static Specification<User> userIsDeleted(Boolean isDeleted) {
        return (root, query, builder) ->
                isDeleted == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("is_deleted"), isDeleted);
    }

}
