package com.graduation.englishtrain.model;

import java.util.List;

/**
 * Created by jiangbo on 2017/5/10.
 */

public class CourseDetail {
    public List<Chapters> chapters;
    public Course course;
    public class Chapters
    {
        public String chapterTitle;
        public String content;
    }
    public class Course
    {
        public String content;
        public String img2;
    }
}
