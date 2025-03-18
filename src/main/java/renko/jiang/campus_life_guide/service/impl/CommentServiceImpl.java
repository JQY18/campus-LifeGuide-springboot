package renko.jiang.campus_life_guide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renko.jiang.campus_life_guide.mapper.CommentMapper;
import renko.jiang.campus_life_guide.pojo.dto.CommentDTO;
import renko.jiang.campus_life_guide.pojo.vo.CommentVO;
import renko.jiang.campus_life_guide.service.CommentService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    //    排序规则是：总体按照like属性降序排列；
    //    加之规则：创建时间2小时之内的评论应当优先展示，并且按照like属性降序排列
    @Override
    public List<CommentVO> getCommentsByPostId(Integer postId,Integer userId) {
        List<CommentVO> comments = commentMapper.getCommentsByPostId(postId,userId);

        return sortComments(comments);
    }
    private List<CommentVO> sortComments(List<CommentVO> comments) {
        LocalDateTime now = LocalDateTime.now();
        Duration twoHours = Duration.ofHours(2);

        // 创建一个Comparator来比较like属性，并且保证降序排列
        Comparator<CommentVO> likeComparator = Comparator.comparing(CommentVO::getLike, Comparator.reverseOrder());

        // 分割列表为两部分：最近两小时内创建的评论和其他评论
        List<CommentVO> recentComments = comments.stream()
                .filter(comment -> Duration.between(comment.getCreateTime(), now).compareTo(twoHours) <= 0)
                .sorted(likeComparator)
                .toList();

        List<CommentVO> olderComments = comments.stream()
                .filter(comment -> Duration.between(comment.getCreateTime(), now).compareTo(twoHours) > 0)
                .sorted(likeComparator)
                .toList();

        // 合并两个列表
        List<CommentVO> sortedComments = Stream.concat(recentComments.stream(), olderComments.stream())
                .collect(Collectors.toList());

        return sortedComments;
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        commentMapper.addComment(commentDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeComment(Integer commentId, Integer likerId,Integer to,boolean isLiked) {
        // 点之前的状态
        if (!isLiked){
            // 点赞
            to = to + 1;
            commentMapper.likeComment(commentId,likerId);
        }else{
            //取消点赞
            to = to - 1;
            commentMapper.deleteCommentLike(commentId,likerId);
        }
        // 数量变化
        commentMapper.addLikeCount(commentId,to);
    }
}
