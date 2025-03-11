package renko.jiang.campus_trade.service;

import renko.jiang.campus_trade.pojo.dto.PostDTO;
import renko.jiang.campus_trade.pojo.dto.PostSearchDTO;
import renko.jiang.campus_trade.pojo.result.PageResult;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.pojo.vo.PostVO;

import java.util.List;

public interface PostService {
    PostVO getPostById(Integer id);

    List<PostVO> getAllPosts(Integer userId);

    void addImages(Integer postId, List<String> url);

    void addPost(PostDTO postDTO);

    List<PostVO> searchPost(PostSearchDTO postSearchDTO);

    Result likePost(Integer postId, Integer userId);

    Result collectPost(Integer postId);

    Result<List<PostVO>> getCollections(Integer userId);

    Result<PageResult<PostVO>> pageQueryPost(PostDTO postDTO);

    Result deletePost(Integer id);
}
