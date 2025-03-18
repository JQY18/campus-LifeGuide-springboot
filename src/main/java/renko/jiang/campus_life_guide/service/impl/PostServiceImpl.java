package renko.jiang.campus_life_guide.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.mapper.PostMapper;
import renko.jiang.campus_life_guide.mapper.UserMapper;
import renko.jiang.campus_life_guide.pojo.dto.PostDTO;
import renko.jiang.campus_life_guide.pojo.dto.PostSearchDTO;
import renko.jiang.campus_life_guide.pojo.entity.Post;

import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.PostVO;
import renko.jiang.campus_life_guide.service.PostService;
import renko.jiang.campus_life_guide.utils.FileUploadToURL;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PostVO> getAllPosts(Integer userId) {
        List<PostVO> postVOS = postMapper.getAllPosts(userId);

        Integer currentUserId = UserContextHolder.getUserId();
        fillPostVOS(currentUserId, postVOS);

        return postVOS;
    }

    private void fillPostVOS(Integer currentUserId, List<PostVO> postVOS) {
        // 获取帖子点赞数和评论数
        for (PostVO postVO : postVOS){
            Integer likes = postMapper.getLikes(postVO.getId());
            postVO.setLikes(likes);

            Integer comments = postMapper.getComments(postVO.getId());
            postVO.setComments(comments);

            //获取帖子的图片
            List<String> imagesByPostId = postMapper.getImagesByPostId(postVO.getId());
            postVO.setImages(imagesByPostId);

            //帖子的用户信息
            User user = userMapper.queryUserById(postVO.getUserId());
            postVO.setUsername(user.getNickname());
            postVO.setAvatar(user.getAvatar());

            if (currentUserId != null){
                // 判断是否点赞
                Integer isLiked = postMapper.isLiked(postVO.getId(), currentUserId);
                if (isLiked != null && isLiked != 0){
                    postVO.setIsLiked(true);
                }else {
                    postVO.setIsLiked(false);
                }
                // 判断是否收藏
                int count = postMapper.isCollected(currentUserId, postVO.getId());
                if (count != 0){
                    postVO.setIsCollected(true);
                }else {
                    postVO.setIsCollected(false);
                }
            }else{
                postVO.setIsLiked(false);
                postVO.setIsCollected(false);
            }
        }
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
        List<String> images = Collections.emptyList();
        if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()){
            images = fileUploadToURL.handleMultipleFileUpload(postDTO.getImages());
        }
        Post post = new Post();
        BeanUtils.copyProperties(postDTO,post);
        // 存储帖子信息，并生成主键
        postMapper.addPost(post);

        // 存储图片
        if (!images.isEmpty()){
            postMapper.addImages(post.getId(),images);
        }
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

    @Override
    public Result collectPost(Integer postId) {
        //首先去数据库查找
        Integer userId = UserContextHolder.getUserId();
        if(postMapper.isCollected(userId,postId) > 0){
            //已经收藏了，取消收藏
            postMapper.deleteCollect(userId, postId);
        }else{
            postMapper.addCollect(userId,postId);
        }
        return Result.success();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<PostVO>> getCollections(Integer userId) {
        //获取当前登录用户
        int currentUserId = UserContextHolder.getUserId();
        List<PostVO> postVOS = postMapper.getUserCollections(userId);
        fillPostVOS(currentUserId, postVOS);
        return Result.success(postVOS);
    }

    @Override
    public Result<PageResult<PostVO>> pageQueryPost(PostDTO postDTO) {
        PageResult<PostVO> pageResult = new PageResult<>();
        //开启分页
        PageHelper.startPage(postDTO.getPageNo(), postDTO.getPageSize());
        //查询分页数据
        List<PostVO> postVOS =  postMapper.pageQueryPost(postDTO);
        PageInfo<PostVO> pageInfo = new PageInfo<>(postVOS);

        //封装结果集
        pageResult.setRecords(pageInfo.getList());
        int total = (int)pageInfo.getTotal();
        pageResult.setTotal(total);
        log.info("分页查询结果：{}",pageResult);
        return Result.success(pageResult);
    }

    @Override
    public Result deletePost(Integer id) {
        int count = postMapper.deletePost(id);
        if(count == 0){
            return Result.error("删除失败");
        }
        return Result.success();
    }
}
