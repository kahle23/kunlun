package com.apyhs.artoria.entity;

public class EntityUtils {

    public static Student getZhangSan() {
        Student student = new Student();
        student.setName("zhangsan");
        student.setAge(19);
        student.setSex(1);
        student.setHeight(170d);
        student.setWeight(140d);
        student.setStudentId(209909017789L);
        student.setSchoolName("Mars Human Studies School");
        student.setNationalLanguageScore(87);
        student.setMathematicsScore(50);
        student.setEnglishScore(88);
        return student;
    }

    public static Student getLisi() {
        Student student = new Student();
        student.setName("lisi");
        student.setAge(22);
        student.setSex(1);
        student.setHeight(180d);
        student.setWeight(140d);
        student.setStudentId(209909018574L);
        student.setSchoolName("Mars Human Studies School");
        student.setNationalLanguageScore(93);
        student.setMathematicsScore(81);
        student.setEnglishScore(79);
        return student;
    }

}
