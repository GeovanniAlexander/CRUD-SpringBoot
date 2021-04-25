package com.springboot.app.springbootweb.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentRepository.findByEmail(student.getEmail());
        if (studentByEmail.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException("The student does not exists " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, Student studentUpdate) {

        String name = studentUpdate.getName();
        String email = studentUpdate.getEmail();
        Student student = studentRepository
                            .findById(studentId)
                            .orElseThrow(() -> new IllegalStateException("The student does not exists" + studentId));
        
        if (name != null && !Objects.equals(name, student.getName())) {
            student.setName(name);
        }
        if (email != null && !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentByEmail = studentRepository.findByEmail(email);
            if( studentByEmail.isPresent() ) {
                throw new IllegalStateException("Email is taken");
            }
            student.setEmail(email);
        }                   
    }
}
