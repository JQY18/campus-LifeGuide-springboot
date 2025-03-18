package renko.jiang.campus_life_guide.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_life_guide.pojo.dto.PostDTO;
import renko.jiang.campus_life_guide.pojo.dto.PostSearchDTO;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.PostVO;
import renko.jiang.campus_life_guide.service.PostService;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 管理端分页查询帖子
     */
    @GetMapping("/page")
    public Result<PageResult<PostVO>> pageQueryPost(PostDTO postDTO){
        return postService.pageQueryPost(postDTO);
    }
//    {
//            pageNo: 1,        // 当前页码
//            pageSize: 10,     // 每页数量
//            username: '',     // 用户名搜索
//            content: '',      // 内容关键词
//            startTime: '',    // 开始时间
//            endTime: ''       // 结束时间
//    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return postService.deletePost(id);
    }

    /**
     * 添加帖子
     * @param postDTO
     * @return
     */
    @PostMapping("/addPost")
    public Result addPost(PostDTO postDTO) {
        postService.addPost(postDTO);
        return Result.success();
    }

    /**
     * 获取所有帖子
     * @return
     */
    @GetMapping("/all")
    public Result<List<PostVO>> getPosts(Integer userId) {
        System.out.println("userId = " + userId);
        List<PostVO> list = postService.getAllPosts(userId);
        return Result.success(list);
    }


    /**
     * 用户收藏列表
     */
    @GetMapping("/current/collections")
    public Result<List<PostVO>> getCollections(Integer userId) {
        return postService.getCollections(userId);
    }



    @PostMapping("/{postId}/like/{userId}")
    Result likePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        return postService.likePost(postId, userId);
    }


    /**
     * 根据id获取帖子
     * @param id
     * @return
     */
    @GetMapping
    public Result<PostVO> getPostById(Integer id) {
        PostVO postVO = postService.getPostById(id);
        return Result.success(postVO);
    }

    /**
     * 工具搜索框搜索帖子
     * @param postSearchDTO
     */
    @GetMapping("/search")
    public Result<List<PostVO>> searchPost(PostSearchDTO postSearchDTO) {
        List<PostVO> list = postService.searchPost(postSearchDTO);
        return Result.success(list);
    }

    /**
     * 收藏/取消收藏帖子
     *
     */
    @PostMapping("/{postId}/collect")
    public Result collectPost(@PathVariable Integer postId) {
        return postService.collectPost(postId);
    }
}
