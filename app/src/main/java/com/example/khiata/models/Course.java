package com.example.khiata.models;

public class Course {
    //Campos
    private int id;
    private String title;
    private String category;
    private String duration;
    private double avaliation;
    private String thumbnailUrl;
    private String videoUrl;

    //Constructor
    public Course(String title, String category, String duration, double avaliation, String thumbnailUrl, String videoUrl) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.avaliation = avaliation;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(double avaliation) {
        this.avaliation = avaliation;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    //toString
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", duration='" + duration + '\'' +
                ", avaliation=" + avaliation +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
