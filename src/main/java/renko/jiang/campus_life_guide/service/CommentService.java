package renko.jiang.campus_life_guide.service;

import renko.jiang.campus_life_guide.pojo.dto.CommentDTO;
import renko.jiang.campus_life_guide.pojo.vo.CommentVO;

import java.util.List;

public interface CommentService {
    List<CommentVO> getCommentsByPostId(Integer postId,Integer userId);

    void addComment(CommentDTO commentDTO);

    void likeComment(Integer commentId, Integer likerId,Integer to,boolean isLiked);
}
