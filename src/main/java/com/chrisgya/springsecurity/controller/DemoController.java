package com.chrisgya.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/demos")
public class DemoController {

    //    hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<FakeUser> getAllFakeUsers() {
        System.out.println("getAllFakeUsers");
        return Arrays.asList(new FakeUser(1, "john"),
                new FakeUser(2, "sam"),
                new FakeUser(3, "doe"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void create(@RequestBody FakeUser fakeUsers) {
        System.out.println("create");
        System.out.println(fakeUsers);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void delete(@PathVariable Integer id) {
        System.out.println("delete");
        System.out.println(id);
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void update(@PathVariable Integer id, @RequestBody FakeUser fakeUser) {
        System.out.println("update");
        System.out.println(String.format("%s %s", id, fakeUser));
    }

}
