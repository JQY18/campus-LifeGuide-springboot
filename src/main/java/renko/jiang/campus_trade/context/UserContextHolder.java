package renko.jiang.campus_trade.context;

public class UserContextHolder {
    private static final ThreadLocal<Integer> USER_CONTEXT = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        USER_CONTEXT.set(userId);
    }

    public static Integer getUserId() {
        return USER_CONTEXT.get();
    }

    public static void remove() {
        USER_CONTEXT.remove();
    }
}
