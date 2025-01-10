package renko.jiang.campus_trade.controller.admin.pojo.entity;


import lombok.Data;

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
}