package com.graduation.englishtrain.model;

import java.util.List;

/**
 * Created by jiangbo on 2017/5/9.
 */

public class TeacherAndCourse {
    public List<Teacher> teachers;
    public class Teacher
    {
        public String name;
        public String id;
    }
    public List<Course> courses;
    public class Course
    {
        public String courseName;
        public String id;
    }
}
