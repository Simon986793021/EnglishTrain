package com.graduation.englishtrain.model;

import java.util.List;

/**
 * Created by Jiangbo on 2017/5/5.
 */

public class CourseList {
    public List<Course> rows;
    public class Course
    {
        public String courseName;
        public String content;
        public String count;//报名人数
        public String img1;
        public String id;
    }
}
