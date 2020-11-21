package com.chrisgya.springsecurity.model;

import com.chrisgya.springsecurity.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class PermissionSpecification {
    // if name == null then specification is ignored
    public static Specification<User> permissionsNameEquals(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.equal(root.get("permissions").get("name"), name);
    }
}
