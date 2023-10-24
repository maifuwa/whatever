package com.example.study.repository.course;

import com.example.study.pojo.dto.course.ClassBuilding;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author maifuwa
 * @date 2023/10/13 下午3:43
 * @description 教学楼的持久化接口
 */
public interface ClassBuildingRepository extends JpaRepository<ClassBuilding, Integer> {

    ClassBuilding findClassBuildingByBuildingAndCampus(String building, String campus);

}
