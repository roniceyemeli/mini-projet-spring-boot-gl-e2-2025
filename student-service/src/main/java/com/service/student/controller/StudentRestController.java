package com.service.student.controller;


import com.service.student.entity.Student;
import com.service.student.service.IServiceStudent;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/student/")
public class StudentRestController {
    private IServiceStudent serviceStudent;
    @PostMapping("add")
    public Student add(@RequestBody Student user) {
        return serviceStudent.addStudent(user);
    }
    @GetMapping("all")
    public List<Student> all(){
        return serviceStudent.allStudents();
    }
}
