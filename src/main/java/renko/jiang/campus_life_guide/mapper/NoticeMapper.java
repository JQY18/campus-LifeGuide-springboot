package renko.jiang.campus_life_guide.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import renko.jiang.campus_life_guide.controller.admin.pojo.dto.NoticeDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper {
    @Insert("insert into notice(title,content,type,publisher) values(#{title},#{content},#{type},#{publisher})")
    int addNotice(Notice notice);

    int pageQueryNoticeCount(NoticeDTO noticeDTO);

    List<Notice> pageQueryNotice(NoticeDTO noticeDTO, Integer start, Integer pageSize);

    int updateNotice(Notice notice);

    @Delete("delete from notice where id = #{id}")
    int deleteNotice(Integer id);
}
