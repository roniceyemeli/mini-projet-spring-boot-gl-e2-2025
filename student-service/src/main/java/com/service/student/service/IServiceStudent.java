package com.service.student.service;


import com.service.student.entity.Student;

import java.util.List;

public interface IServiceStudent {
    public Student addStudent(Student student);
    public List<Student> allStudents();
}
