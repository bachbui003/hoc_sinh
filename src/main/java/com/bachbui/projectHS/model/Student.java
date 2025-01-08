package com.bachbui.projectHS.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hoc_sinh")
public class Student {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hoTen;

    private LocalDate ngaySinh; // Sử dụng LocalDate thay cho Date

    private String gioiTinh;

    private String diaChi;

    private String lop;

    private double diem;

    @ManyToOne
    @JoinColumn(name = "class_id")
    public Classroom classroom;

    public Classroom getClassroom() {
        if (classroom == null) {
            classroom = new Classroom(); // Hoặc một giá trị mặc định
        }
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @ManyToOne
    @JoinColumn(name = "subject_id")
    public Subject subject;

    public Subject getSubject() {
        if (subject == null) {
            subject = new Subject(); // Hoặc một giá trị mặc định
        }
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }
}
