package renko.jiang.campus_life_guide.controller.admin.pojo.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Location {
    private Long id;
    private String name;
    private Double[] coords;
    private String description;
    private String image;
    private String detailId;
    private String category;
    private Double x;
    private Double y;
    private LocalDateTime createdTime;
}