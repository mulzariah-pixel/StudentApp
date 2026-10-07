package org.example.model;

public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String program;

    public Student(String studentId, String firstName, String lastName, String program) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProgram() {
        return program;
    }

    @Override
    public String toString() {
        return studentId + " - " + firstName + " " + lastName + " - " + program;
    }
}