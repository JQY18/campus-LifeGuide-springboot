package renko.jiang.campus_trade.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import renko.jiang.campus_trade.pojo.dto.PostSearchDTO;
import renko.jiang.campus_trade.pojo.entity.Post;
import renko.jiang.campus_trade.pojo.vo.PostVO;

import java.util.List;

@Mapper
public interface PostMapper {
    PostVO getById(Integer id);

    List<PostVO> getAllPosts(Integer userId);

    @Select("select url from images where post_id = #{id}")
    List<String> getImagesByPostId(Integer id);

    void addImages(Integer postId, List<String> images);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into post set category = #{category}, title = #{title},content = #{content},user_id = #{userId}")
    void addPost(Post post);

    List<PostVO> searchPost(PostSearchDTO postSearchDTO);

    @Select("select count(1) from post_likes where post_id = #{id}")
    Integer getLikes(Integer id);

    @Select("select count(1) from post_likes where post_id = #{id} and user_id = #{userId}")
    Integer isLiked(Integer id, Integer userId);

    @Insert("insert into post_likes set post_id = #{postId}, user_id = #{userId}")
    void addLike(Integer postId, Integer userId);

    @Insert("delete from post_likes where post_id = #{postId} and user_id = #{userId}")
    void deleteLike(Integer postId, Integer userId);

    @Select("select count(1) from comment where post_id = #{id}")
    Integer getComments(Integer id);

    @Select("select count(1) from post_collect where user_id = #{userId} and post_id = #{postId}")
    int isCollected(Integer userId, Integer postId);

    @Insert("insert into post_collect set user_id = #{userId}, post_id = #{postId}")
    void addCollect(Integer userId, Integer postId);

    @Insert("delete from post_collect where user_id = #{userId} and post_id = #{postId}")
    void deleteCollect(Integer userId, Integer postId);

    List<PostVO> getUserCollections(int userId);

    @Select("select count(1) from post_collect where user_id = #{userId}")
    int getUserCollectionsCount(Integer userId);
}
