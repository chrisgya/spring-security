package com.chrisgya.springsecurity.model.querySpecs;

import com.chrisgya.springsecurity.entity.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class RoleSpecification {

    // if name == null then specification is ignored
    public static Specification<Role> roleNameEquals(String name) {
        return (root, query, builder) ->
                !StringUtils.hasText(name) ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
    }
}
