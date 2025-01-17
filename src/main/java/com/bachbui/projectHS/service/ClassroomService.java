package com.bachbui.projectHS.service;

import com.bachbui.projectHS.model.Classroom;
import com.bachbui.projectHS.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClassroomService {

   @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }
}
