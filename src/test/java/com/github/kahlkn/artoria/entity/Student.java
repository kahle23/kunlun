package com.github.kahlkn.artoria.entity;

public class Student extends Person {

    private static final Integer PASSING_SCORE = 60;

    private Long studentId;
    private String schoolName;
    private Integer nationalLanguageScore;
    private Integer mathematicsScore;
    private Integer englishScore;
    private boolean graduate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getNationalLanguageScore() {
        return nationalLanguageScore;
    }

    public void setNationalLanguageScore(Integer nationalLanguageScore) {
        this.nationalLanguageScore = nationalLanguageScore;
    }

    public Integer getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(Integer mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    public Integer getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(Integer englishScore) {
        this.englishScore = englishScore;
    }

    public boolean isGraduate() {
        return graduate;
    }

    public void setGraduate(boolean graduate) {
        this.graduate = graduate;
    }

}
