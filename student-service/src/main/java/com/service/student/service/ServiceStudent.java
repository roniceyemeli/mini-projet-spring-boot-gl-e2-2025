package com.service.student.service;


import com.service.student.entity.Student;
import com.service.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceStudent implements IServiceStudent {
    StudentRepository studentRepository;
    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> allStudents() {
        return studentRepository.findAll();
    }
}
