package cn.leancloud.demo.todo;

import cn.leancloud.EngineFunction;
import cn.leancloud.EngineFunctionParam;
import com.avos.avoscloud.*;
import org.apache.http.util.TextUtils;

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
        if (!clockUsers.containsKey(userId)) {
            clockUsers.put(userId, 1);
            AVObject todo = AVObject.createWithoutData("_User", userId);
            todo.put("integral", String.valueOf(Integer.valueOf(todo.getString("integral")) + 1));
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
    public static String setUserACL(@EngineFunctionParam("userId") String userId) throws Exception {
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        AVUser avUser = userQuery.get(userId);
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
        acl.setWriteAccess(userId, true);// 为当前用户赋予「写」权限，有且仅有当前用户可以修改这条 Post
        avUser.setACL(acl);// 将 ACL 实例赋予 Post对象
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    try {
                        throw e;
                    } catch (AVException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });// 保存
        return userId + " 修改权限成功";

    }

    /**
     * 设置用户积分
     */
    @EngineFunction("setUserJiFen")
    public static String setUserJiFen(@EngineFunctionParam("userId") String userId, @EngineFunctionParam("num") String num) throws Exception {
        if(TextUtils.isEmpty(num)){
            return "无效值";
        }
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        AVUser avUser = userQuery.get(userId);
        avUser.put("integral", num);
        avUser.saveInBackground();
        return userId + " 修改积分成功";

    }


    /**
     * 设置用户设备
     */
    @EngineFunction("setInstallationId")
    public static String setInstallationId(@EngineFunctionParam("userId") String userId, @EngineFunctionParam("installationId") String installationId) throws Exception {
        if(TextUtils.isEmpty(installationId)){
            return "无效值";
        }
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        AVUser avUser = userQuery.get(userId);
        avUser.put("installationId", installationId);
        avUser.saveInBackground();
        return userId + " 修改设备ID成功";

    }




}
