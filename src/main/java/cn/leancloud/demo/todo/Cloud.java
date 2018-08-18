package cn.leancloud.demo.todo;

import cn.leancloud.EngineFunction;
import cn.leancloud.EngineFunctionParam;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.HashMap;
import java.util.Map;

public class Cloud {

    @EngineFunction("hello")
    public static String hello(@EngineFunctionParam("name") String name) {
        if (name == null) {
            return "What is your name?";
        }
        return String.format("Hello %s!", name);
    }

    private final static Map<String, Integer> clockUsers = new HashMap<>();

    /**
     * 重置积分计算签到
     */
    @EngineFunction("resetIntegral")
    public static void resetIntegral() {
        clockUsers.clear();
    }


    /**
     * 签到
     */
    @EngineFunction("clock")
    public static int clock(@EngineFunctionParam("userId") String userId) {
        if(!clockUsers.containsKey(userId)) {
            clockUsers.put(userId, 1);
            AVObject todo = AVObject.createWithoutData("_User", userId);
            todo.put("integral", todo.getInt("integral") + 1);
            todo.saveInBackground();
            return 1;
        } else {
            return 0;
        }
    }


}
