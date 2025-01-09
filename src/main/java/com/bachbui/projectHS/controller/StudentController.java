package com.bachbui.projectHS.controller;

import com.bachbui.projectHS.model.Classroom;
import com.bachbui.projectHS.model.Student;
import com.bachbui.projectHS.model.Subject;
import com.bachbui.projectHS.service.ClassroomService;
import com.bachbui.projectHS.service.SubjectService;
import com.bachbui.projectHS.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private SubjectService subjectService;

    // List students with pagination
    @GetMapping
    public String getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.getAllStudents(pageable);
        model.addAttribute("students", students.getContent());
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("currentPage", page);
        return "index";
    }

    // Đổi từ @GetMapping("/students/add") thành @GetMapping("/addStudent")
    @GetMapping("/addStudent")
    public String showAddStudentForm(Model model) {
        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("student", new Student());
        return "test"; // Trả về trang test.html
    }

    // Add a new student
    @PostMapping
    public String addStudent(@ModelAttribute Student student) {
        studentService.addStudent(student);
        return "redirect:/students";
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    // Show form to edit a student
    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        // Lấy thông tin sinh viên từ ID
        Student student = studentService.getStudentById(id);

        // Lấy danh sách lớp học và môn học từ service
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        List<Subject> subjects = subjectService.getAllSubjects();

        // Thêm đối tượng vào model để hiển thị trong form
        model.addAttribute("student", student);
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("subjects", subjects);

        return "editStudent"; // Trả về view sửa học sinh
    }


    // Update a student's information
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        studentService.updateStudent(id, student);
        return "redirect:/students";
    }

    // Search students
    @GetMapping("/search")
    public String searchStudents(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.searchStudents(searchTerm, pageable);
        model.addAttribute("students", students.getContent());
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("searchTerm", searchTerm);
        return "index";
    }

    // Return to the student list
    @GetMapping("/back")
    public String backToAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return getAllStudents(page, size, model);
    }

    // Filter students by score
    @GetMapping("/filter")
    public String filterStudents(
            @RequestParam(defaultValue = "5") double minDiem,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.filterStudentsByDiem(minDiem, pageable);
        model.addAttribute("students", students.getContent());
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("minDiem", minDiem);
        return "index";
    }
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportStudentsToExcel(HttpServletResponse response) throws IOException {
        // Lấy tất cả học sinh mà không phân trang
        List<Student> allStudents = studentService.getAllStudentsWithoutPagination();

        if (allStudents.isEmpty()) {
            response.getWriter().write("No data available");
            return;

        }

        // Sắp xếp danh sách học sinh theo ID (sắp xếp tăng dần)
        allStudents.sort(Comparator.comparing(Student::getId));

        // Tạo workbook và sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Tạo tiêu đề cho các cột
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Họ tên");
        header.createCell(2).setCellValue("Ngày Sinh");
        header.createCell(3).setCellValue("Địa Chỉ");
        header.createCell(4).setCellValue("Lớp Học");
        header.createCell(5).setCellValue("Môn Học");
        header.createCell(6).setCellValue("Điểm");

        // Thêm dữ liệu học sinh vào sheet
        int rowNum = 1;
        for (Student student : allStudents) {
            Row row = sheet.createRow(rowNum++);

            // Kiểm tra null và xử lý với giá trị mặc định
            row.createCell(0).setCellValue(student.getId() != null ? student.getId() : 0);  // ID có thể là Long, xử lý với giá trị mặc định là 0
            row.createCell(1).setCellValue(student.getHoTen() != null ? student.getHoTen() : "N/A");  // Tên học sinh
            row.createCell(2).setCellValue(student.getNgaySinh() != null ? student.getNgaySinh().toString() : "N/A");  // Ngày sinh, kiểm tra null

            row.createCell(3).setCellValue(student.getDiaChi() != null ? student.getDiaChi() : "N/A");  // Địa chỉ
            row.createCell(4).setCellValue(student.getClassroom() != null ? student.getClassroom().getName() : "N/A");  // Lớp học
            row.createCell(5).setCellValue(student.getSubject() != null ? student.getSubject().getName() : "N/A");  // Môn học
            row.createCell(6).setCellValue(student.getDiem());  // Nếu diem là kiểu nguyên thủy (int, double)
        }

        // Thiết lập kiểu dữ liệu cho response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");

        // Ghi workbook vào response
        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
