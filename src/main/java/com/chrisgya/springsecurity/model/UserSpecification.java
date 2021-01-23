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
                        builder.equal(root.get("users").get("email"), email);
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
    public static Specification<User> userIsLockedEquals(Boolean locked) {
        return (root, query, builder) ->
                locked == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("locked"), locked);
    }

    // if isEnabled == null then specification is ignored
    public static Specification<User> userIsEnabledEquals(Boolean enabled) {
        return (root, query, builder) ->
                enabled == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("enabled"), enabled);
    }

    // if isConfirmed == null then specification is ignored
    public static Specification<User> userIsConfirmedEquals(Boolean confirmed) {
        return (root, query, builder) ->
                confirmed == null ?
                        builder.conjunction() :
                        builder.equal(root.get("users").get("confirmed"), confirmed);
    }

}
