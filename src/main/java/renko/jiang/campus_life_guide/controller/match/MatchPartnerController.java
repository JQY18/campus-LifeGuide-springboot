package renko.jiang.campus_life_guide.controller.match;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.pojo.dto.MatchPartnerDTO;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.InterestVO;
import renko.jiang.campus_life_guide.pojo.vo.MatchPartnerVO;
import renko.jiang.campus_life_guide.service.ChatRoomService;
import renko.jiang.campus_life_guide.service.MatchPartnerService;
import renko.jiang.campus_life_guide.utils.RedisUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 86132
 */
@Slf4j
@RestController
@RequestMapping("/match")
public class MatchPartnerController {
    @Autowired
    private MatchPartnerService matchPartnerService;

    @Autowired
    private ChatRoomService chatRoomService;

    // 存储所有的SSE连接
    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();


    @Autowired
    private RedisUtil redisUtil;

    /**
     * 建立SSE连接
     */
    @GetMapping("/realtime/events/{userId}")
    public SseEmitter subscribe(@PathVariable Integer userId, Integer interestId, Integer limited) {

        // 创建超时时间为1分钟的emitter
        SseEmitter emitter = new SseEmitter(60000L);

        String zSetKey = redisUtil.buildKey("realtime.match",
                interestId.toString(), limited.toString());


        // 注册回调函数
        emitter.onCompletion(() -> {
            log.info("用户{}断开连接", userId);
            clearUserIdFromZSet(zSetKey, userId);
        });
        emitter.onTimeout(() -> {
            log.info("用户{}超时", userId);
            clearUserIdFromZSet(zSetKey, userId);
        });
        emitter.onError(e -> {
            if (e instanceof IOException) {
                // 客户端断开连接
                clearUserIdFromZSet(zSetKey, userId);
            }
        });

        // 存储emitter
        emitters.put(userId, emitter);

        // 发送初始连接成功事件
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("连接成功"));
        } catch (IOException e) {
            emitters.remove(userId);
            return null;
        }


        Long zSize = redisUtil.zSize(zSetKey);

        // 通知其他用户实时的人数更新
        Set<Object> notifiedUsers = redisUtil.zRange(zSetKey, 0, zSize - 1);
        for (Object notifiedUser : notifiedUsers) {
            Integer notifiedUserId = (Integer) notifiedUser;
            sendMatchUpdate(notifiedUserId, zSize);
        }

        // 如果人数达到上限，则创建群聊
        if (zSize >= limited) {
            // 将匹配成功的用户从ZSet中取出并创建群聊
            List<Object> userIds = redisUtil.zPoll(zSetKey, 0, limited - 1);
            List<Integer> userIdsInt = userIds.stream().map(o -> (Integer) o).filter(o -> o.compareTo(userId) != 0).toList();
            //获取兴趣名称，用于建立群聊
            String interestName = matchPartnerService.getInterestNameById(interestId);
            chatRoomService.createGroupChatForMatch(userId, userIdsInt, interestName);
            log.info("匹配成功，创建群聊成功！{}", userIds);
            //通知所有用户，匹配成功
            for (Object notifiedUser : userIds) {
                Integer notifiedUserId = (Integer) notifiedUser;
                sendMatchSuccess(notifiedUserId);
            }
        }

        return emitter;
    }

    @PostMapping("/realtime/start")
    public Result startMatch(@RequestBody MatchPartnerDTO matchPartnerDTO) {
        Integer userId = UserContextHolder.getUserId();
        // 将信息放入Redis中
        String zSetKey = redisUtil.buildKey("realtime.match",
                matchPartnerDTO.getInterestId().toString(), matchPartnerDTO.getLimited().toString());
        double score = System.currentTimeMillis();
        redisUtil.zAdd(zSetKey, userId, score);

        //保存用户以及表示映射关系

        return Result.success(userId);
    }

    @GetMapping("/realtime/status")
    public Result getMatchStatus() {
        Integer userId = UserContextHolder.getUserId();
        return Result.success();
    }

    @PostMapping("/realtime/cancel")
    public Result cancelMatch(@RequestBody MatchPartnerDTO matchPartnerDTO) {
        Integer userId = UserContextHolder.getUserId();
        emitters.remove(userId);
        String key = redisUtil.buildKey("realtime.match", matchPartnerDTO.getInterestId().toString(), matchPartnerDTO.getLimited().toString());
        // 从ZSet中删除该用户
        redisUtil.zDel(key, userId);
        //发送匹配状态更新
        Set<Object> userIdSet = redisUtil.zRange(key, 0, -1);
        Long zSize = (long) userIdSet.size();
        for (Object userIdObj : userIdSet) {
            Integer notifiedUserId = (Integer) userIdObj;
            sendMatchUpdate(notifiedUserId, zSize);
        }
        return Result.success();
    }


    /**
     * 向指定用户发送匹配状态更新
     */
    public void sendMatchUpdate(Integer userId, Long zSize) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("matchUpdate")
                        .data(zSize));
            } catch (IOException e) {
                log.error("向指定用户发送匹配状态更新失败", e);
            }
        }
    }

    /**
     * 向指定用户发送匹配成功事件
     */
    public void sendMatchSuccess(Integer userId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("matchSuccess")
                        .data("匹配成功"));

                // 匹配成功后完成此连接
                emitter.complete();
            } catch (IOException e) {
                log.error("匹配成功后完成此连接失败", e);
            } finally {
                emitters.remove(userId);
            }
        }
    }


    /**
     * 向指定用户发送匹配超时事件
     */
    public void clearUserIdFromZSet(String key, Integer userId) {
        emitters.remove(userId);
        redisUtil.zDel(key, userId);
        //        SseEmitter emitter = emitters.get(userId);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("matchTimeout")
//                        .data("匹配超时"));
//
//                // 匹配超时后完成此连接
//                emitter.complete();
//                userIdMap.remove(uuid);
//                emitters.remove(userId);
//                //将用户从redis的ZSet中删除
//            } catch (IOException e) {
//                emitters.remove(userId);
//            }
//        }
    }


    /**
     * 获取所有兴趣
     *
     * @return
     */
    @GetMapping("/interests")
    public Result<List<InterestVO>> getInterests() {
        return matchPartnerService.getInterests();
    }

    /**
     * 发布匹配
     *
     * @param matchPartnerDTO
     * @return
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody MatchPartnerDTO matchPartnerDTO) {
        return matchPartnerService.publish(matchPartnerDTO);
    }

    /**
     * 根据兴趣获取匹配列表，若interestId为null，则获取所有匹配
     *
     * @param interestId
     * @return
     */
    @GetMapping("/list")
    public Result<List<MatchPartnerVO>> getList(Integer interestId) {
        return matchPartnerService.getList(interestId);
    }

    /**
     * 获取我发布的匹配
     *
     * @return
     */
    @GetMapping("/publish/mine")
    public Result<List<MatchPartnerVO>> getMyList() {
        return matchPartnerService.getMyList();
    }

    /**
     * 获取我加入的匹配
     *
     * @return
     */
    @GetMapping("/join/mine")
    public Result<List<MatchPartnerVO>> getMyJoinList() {
        return matchPartnerService.getMyJoinList();
    }

    /**
     * 加入匹配
     *
     * @param matchId
     * @return
     */
    @PostMapping("/join/{matchId}")
    public Result join(@PathVariable("matchId") Integer matchId) {
        return matchPartnerService.join(matchId);
    }

    /**
     * 退出匹配
     */
    @PostMapping("/quit/{matchId}")
    public Result quit(@PathVariable("matchId") Integer matchId) {
        return matchPartnerService.quit(matchId);
    }

    /**
     * 取消发布
     */
    @PostMapping("/cancel/{matchId}")
    public Result cancelPublish(@PathVariable("matchId") Integer matchId) {
        return matchPartnerService.unpublish(matchId);
    }

}
