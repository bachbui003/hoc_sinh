package com.bachbui.projectHS.service;

import com.bachbui.projectHS.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.bachbui.projectHS.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Lấy danh sách học sinh với phân trang và sắp xếp theo id
    public Page<Student> getAllStudents(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("id")));
        return studentRepository.findAll(sortedPageable);
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void updateStudent(Long id, Student student) {
        student.setId(id); // Đảm bảo ID không bị thay đổi
        studentRepository.save(student);
    }

    // Tìm kiếm học sinh theo tên hoặc ID
    public Page<Student> searchStudents(String searchTerm, Pageable pageable) {
        if (searchTerm.matches("\\d+")) { // Nếu là ID
            Long id = Long.parseLong(searchTerm);
            return studentRepository.findById(id, pageable);
        } else { // Nếu là tên học sinh
            return studentRepository.findByHoTenContainingIgnoreCase(searchTerm, pageable);
        }
    }

    // Lọc học sinh theo điểm
    public Page<Student> filterStudentsByDiem(double minDiem, Pageable pageable) {
        return studentRepository.findByDiemGreaterThanEqual(minDiem, pageable);
    }
    public List<Student> getAllStudentsWithoutPagination() {
        return studentRepository.findAll(); // Lấy tất cả học sinh từ cơ sở dữ liệu
    }

}
