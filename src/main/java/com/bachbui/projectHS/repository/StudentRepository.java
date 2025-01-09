package com.bachbui.projectHS.repository;

import com.bachbui.projectHS.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Tìm kiếm học sinh theo tên (không phân biệt chữ hoa/thường)
    Page<Student> findByHoTenContainingIgnoreCase(String hoTen, Pageable pageable);

    // Tìm kiếm học sinh theo ID
    Page<Student> findById(Long id, Pageable pageable);

    // Tìm kiếm học sinh theo mã sinh viên
    Page<Student> findByMaSinhVienContainingIgnoreCase(String maSinhVien, Pageable pageable);

    // Tìm kiếm học sinh theo ID hoặc mã sinh viên
    Page<Student> findByIdOrMaSinhVien(Long id, String maSinhVien, Pageable pageable);

    // Lọc học sinh có điểm trung bình lớn hơn hoặc bằng minDiem
    Page<Student> findByDiemGreaterThanEqual(double diem, Pageable pageable);

    // Lọc học sinh theo điểm trung bình
    Page<Student> findByDiem(double diem, Pageable pageable);
}
