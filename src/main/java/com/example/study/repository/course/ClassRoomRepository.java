package com.example.study.repository.course;

import com.example.study.pojo.dto.course.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author maifuwa
 * @date 2023/10/13 下午3:43
 * @description 教室的持久化接口
 */
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

    ClassRoom findClassRoomByLocation(String location);

    boolean existsByLocation(String location);

}
