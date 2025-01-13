package renko.jiang.campus_trade.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import renko.jiang.campus_trade.mapper.PostMapper;
import renko.jiang.campus_trade.pojo.dto.PostDTO;
import renko.jiang.campus_trade.pojo.dto.PostSearchDTO;
import renko.jiang.campus_trade.pojo.entity.Post;

import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.pojo.vo.PostVO;
import renko.jiang.campus_trade.service.PostService;
import renko.jiang.campus_trade.utils.FileUploadToURL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FileUploadToURL fileUploadToURL;

    /**
     * 根据id获取帖子
     * @param id
     * @return
     */
    @Override
    public PostVO getPostById(Integer id) {
        PostVO postVO = postMapper.getById(id);
        return postVO;
    }

    /**
     * 获取所有帖子
     * @return
     */
    @Override
    public List<PostVO> getAllPosts(Integer userId, Integer currentUserId) {
        List<PostVO> postVOS = postMapper.getAllPosts(userId);

        // 获取帖子点赞数和评论数
        for (PostVO postVO : postVOS){
            Integer likes = postMapper.getLikes(postVO.getId());
            postVO.setLikes(likes);

            Integer comments = postMapper.getComments(postVO.getId());
            postVO.setComments(comments);

            if (currentUserId != null){
                Integer isLiked = postMapper.isLiked(postVO.getId(), currentUserId);
                if (isLiked != null && isLiked != 0){
                    postVO.setIsLiked(true);
                }else {
                    postVO.setIsLiked(false);
                }
            }else{
                postVO.setIsLiked(false);
            }
        }
        return postVOS;
    }

    /**
     * 添加帖子图片
     * @param postId
     * @param images
     */
    @Override
    public void addImages(Integer postId, List<String> images) {
        postMapper.addImages(postId,images);
    }

    /**
     * 添加帖子
     * @param postDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addPost(PostDTO postDTO) {
        // 转换图片为静态资源映射地址
        List<String> images = fileUploadToURL.handleMultipleFileUpload(postDTO.getImages());

        Post post = new Post();
        BeanUtils.copyProperties(postDTO,post);
        // 存储帖子信息，并生成主键
        postMapper.addPost(post);

        // 存储图片
        postMapper.addImages(post.getId(),images);
    }

    /**
     * 搜索帖子
     * @param postSearchDTO
     * @return
     */
    @Override
    public List<PostVO> searchPost(PostSearchDTO postSearchDTO) {
        List<PostVO> postVOS = postMapper.searchPost(postSearchDTO);
        return postVOS;
    }

    @Override
    public Result likePost(Integer postId, Integer userId) {
        Integer liked = postMapper.isLiked(postId, userId);
        if (liked == null || liked == 0){
            postMapper.addLike(postId,userId);
            return Result.success();
        }else {
            postMapper.deleteLike(postId,userId);
            return Result.error("取消点赞");
        }
    }

}
