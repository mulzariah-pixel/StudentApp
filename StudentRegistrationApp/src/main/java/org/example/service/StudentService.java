package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;

public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public String registerStudent(
            String studentId,
            String firstName,
            String lastName,
            String program) {

        // Check for empty fields
        if (studentId == null || studentId.trim().isEmpty()) {
            return "ERROR: Student ID cannot be empty.";
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            return "ERROR: First name cannot be empty.";
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            return "ERROR: Last name cannot be empty.";
        }

        if (program == null || program.trim().isEmpty()) {
            return "ERROR: Program cannot be empty.";
        }

        // Check for duplicate Student ID
        if (repository.existsById(studentId.trim())) {
            return "ERROR: Student ID already exists.";
        }

        // Create and save student
        Student student = new Student(
                studentId.trim(),
                firstName.trim(),
                lastName.trim(),
                program.trim()
        );

        repository.save(student);

        return "SUCCESS: Student registered successfully.";
    }
}