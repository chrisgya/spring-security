package com.chrisgya.springsecurity.model.querySpecs;

import com.chrisgya.springsecurity.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    // if name == null then specification is ignored
    public static Specification<Role> roleNameEquals(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
    }
}
