package renko.jiang.campus_trade.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import renko.jiang.campus_trade.controller.admin.pojo.entity.DetailComment;
import renko.jiang.campus_trade.controller.admin.pojo.entity.Location;
import renko.jiang.campus_trade.controller.admin.pojo.entity.LocationDetail;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationVO;

import java.util.List;

/**
 * @author 86132
 */

@Mapper
public interface LocationMapper {

    @Select("select * from location")
    List<Location> getAllLocations();

    @Select("select * from location_detail where detail_id = #{detailId}")
    LocationDetail getDetailById(String detailId);

    @Select("select url from location_images where id = #{imageId}")
    List<String> getImagesById(Integer imageId);


    @Select("select url from location_videos where id = #{videoId}")
    List<String> getVideosById(Integer videoId);

    @Select("select id,username,content,create_time as time from detail_comment where detail_id = #{detailId} order by create_time desc")
    List<DetailComment> getCommentsById(String detailId);

    @Select("select nickname from user where username = #{username}")
    String getNickNameByUsername(String username);

    @Select("select username from user where id = #{userId}")
    String getUsernameByUserId(Integer userId);

    @Insert("insert into detail_comment (detail_id, username, content) value (#{detailId},#{username},#{content})")
    int submitComment(DetailComment detailComment);
}
