package com.graduation.englishtrain.model;

import java.util.List;

/**
 * Created by Simon on 2017/5/8.
 */

public class LessonList {
    public List<Lesson> rows;
    public class Lesson
    {
        public String courseName;
        public String teacherName;
        public String lessonStartTime;
        public String id;
    }
}
