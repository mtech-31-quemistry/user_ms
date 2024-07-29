//package com.quemistry.user_ms.repository.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//@Table(name = "student_class", schema = "member", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "class_id"}))
//public class StudentClass {
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Student student;
//
//    private int status;
//}
