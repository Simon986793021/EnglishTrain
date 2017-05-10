package com.graduation.englishtrain.model;

import java.util.List;

/**
 * Created by jiangbo on 2017/5/10.
 */

public class LessonOrderList {
    public List<LessonOrderList.LessonOrder> rows;
    public  class LessonOrder
    {
        public String courseName;
        public String teacherName;
    }
}
