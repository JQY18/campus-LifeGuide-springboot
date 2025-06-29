package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.controller.admin.pojo.dto.DetailCommentDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.DetailComment;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.Location;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.LocationDetail;

import java.util.List;

/**
 * @author 86132
 */

@Mapper
public interface LocationMapper {

    @Select("select * from location order by created_time desc")
    List<Location> getAllLocations(Integer locationId);

    @Select("select * from location_detail where detail_id = #{detailId}")
    LocationDetail getDetailById(String detailId);

    @Select("select url from location_images where location_detail_id = #{locationId}")
    List<String> getImagesById(Integer locationId);


    @Select("select url from location_videos where location_detail_id = #{locationId}")
    List<String> getVideosById(Integer locationId);

    @Select("select id,username,content,create_time as time from detail_comment where detail_id = #{detailId} order by create_time desc")
    List<DetailComment> getCommentsById(String detailId);

    @Select("select nickname from user where username = #{username}")
    String getNickNameByUsername(String username);

    @Select("select username from user where id = #{userId}")
    String getUsernameByUserId(Integer userId);

    @Insert("insert into detail_comment (detail_id, username, content) value (#{detailId},#{username},#{content})")
    int submitComment(DetailComment detailComment);


    @Insert("insert into location (name,x,y,description,image,detail_id,category) value (#{name},#{x},#{y},#{description},#{image},#{detailId},#{category})")
    Integer addLocation(Location location);

    Integer updateLocation(Location location);


    @Delete("delete from location where id = #{id}")
    Integer deleteLocation(Long id);

    @Select("select id from location_detail where detail_id = #{detailId}")
    Integer selectDetailById(String detailId);



    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into location_detail (detail_id, name, description) value (#{detailId},#{name},#{description})")
    Integer addLocationDetails(LocationDetail locationDetail);

    Integer updateLocationDetails(LocationDetail locationDetail);


    Integer saveImages(Integer detailId, List<String> images);

    Integer saveVideos(Integer detailId, List<String> videos);

    @Delete("delete from location_images where url = #{imageUrl}")
    Integer deleteImage(String imageUrl);

    @Delete("delete from location_videos where url = #{videoUrl}")
    Integer deleteVideo(String videoUrl);

    @Delete("delete from detail_comment where id = #{id}")
    int deleteLocationCommentById(Integer id);

    List<DetailComment> queryCommentsByPage(DetailCommentDTO detailCommentDTO, int start, Integer pageSize);

    @Select("select count(*) from detail_comment where detail_id = #{detailId}")
    int queryCommentsByPageCount(DetailCommentDTO detailCommentDTO);
}
