package com.service.school.controller;

import com.service.school.entity.School;
import com.service.school.service.IServiceSchool;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/school/")
public class SchoolRestController {
    private IServiceSchool serviceSchool;
    @PostMapping("add")
    public School add(@RequestBody School school) {
        return serviceSchool.addSchool(school);
    }
    @GetMapping("all")
    public List<School> all(){
        return serviceSchool.allSchools();
    }
}
