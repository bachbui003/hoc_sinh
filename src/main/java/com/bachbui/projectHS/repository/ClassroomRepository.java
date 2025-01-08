package com.bachbui.projectHS.repository;

import com.bachbui.projectHS.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository  extends JpaRepository<Classroom, Long> {

}
