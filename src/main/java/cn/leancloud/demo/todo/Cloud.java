package cn.leancloud.demo.todo;

import cn.leancloud.EngineFunction;
import cn.leancloud.EngineFunctionParam;
import com.avos.avoscloud.*;

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
            todo.put("integral",String.valueOf( Integer.valueOf(todo.getString("integral")) + 1));
            todo.saveInBackground();
            return 1;
        } else {
            return 0;
        }
    }



    /**
     * 设置用户权限
     */
    @EngineFunction("setUserACL")
    public static String setUserACL(@EngineFunctionParam("userId") String userId) {
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.getInBackground(userId, new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                //新建一个 ACL 实例
                AVACL acl = new AVACL();
                acl.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
                acl.setWriteAccess(userId, true);// 为当前用户赋予「写」权限，有且仅有当前用户可以修改这条 Post
                avUser.setACL(acl);// 将 ACL 实例赋予 Post对象
                avUser.saveInBackground();// 保存
            }
        });

        return "修改权限成功";

    }





}
