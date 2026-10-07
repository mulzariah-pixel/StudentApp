package org.example.repository;

import org.example.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public void save(Student student) {
        students.add(student);
    }

    public List<Student> findAll() {
        return students;
    }

    public boolean existsById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return true;
            }
        }

        return false;
    }
}