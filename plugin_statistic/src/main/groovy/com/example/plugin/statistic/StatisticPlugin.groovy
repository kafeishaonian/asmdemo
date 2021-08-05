package com.example.plugin.statistic


import com.example.plugin.statistic.bp.BuryPointEntity
import com.example.plugin.statistic.bp.BuryPointTransform
import com.example.plugin.statistic.mt.MethodTimerEntity
import com.example.plugin.statistic.mt.MethodTimerTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension


class StatisticPlugin implements Plugin<Project> {

    public static Map<String, BuryPointEntity> BURY_POINT_MAP
    public static List<MethodTimerEntity> METHOD_TIMER_LIST

    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)
        //BuryPointTransform
        android.registerTransform(new BuryPointTransform())
        android.registerTransform(new MethodTimerTransform())

        // 获取gradle里面配置的埋点信息
        def statisticExtension = project.extensions.create('statistic', StatisticExtension)
        project.afterEvaluate {
            // 遍历配置的埋点信息，将其保存在BURY_POINT_MAP方便调用
            BURY_POINT_MAP = new HashMap<>()
            def buryPoint = statisticExtension.getBuryPoint()
            if (buryPoint != null){
                buryPoint.each { Map<String, Object> map ->
                    BuryPointEntity entity = new BuryPointEntity()
                    if (map.containsKey("isAnnotation")){
                        entity.isAnnotation = map.get("isAnnotation")
                    }
                    if (map.containsKey("isMethodExit")){
                        entity.isMethodExit = map.get("isMethodExit")
                    }
                    if (map.containsKey("agentOwner")) {
                        entity.agentOwner = map.get("agentOwner")
                    }
                    if (map.containsKey("agentName")) {
                        entity.agentName = map.get("agentName")
                    }
                    if (map.containsKey("agentDesc")) {
                        entity.agentDesc = map.get("agentDesc")
                    }
                    if (entity.isAnnotation) {
                        if (map.containsKey("annotationDesc")) {
                            entity.annotationDesc = map.get("annotationDesc")
                        }
                        if (map.containsKey("annotationParams")) {
                            entity.annotationParams = map.get("annotationParams")
                        }
                        BURY_POINT_MAP.put(entity.annotationDesc, entity)
                    } else {
                        if (map.containsKey("methodOwner")) {
                            entity.methodOwner = map.get("methodOwner")
                        }
                        if (map.containsKey("methodName")) {
                            entity.methodName = map.get("methodName")
                        }
                        if (map.containsKey("methodDesc")) {
                            entity.methodDesc = map.get("methodDesc")
                        }
                        BURY_POINT_MAP.put(entity.methodName + entity.methodDesc, entity)
                    }
                }
            }
            // 获取方法计时信息，将其保存在METHOD_TIMER_LIST方便调用
            METHOD_TIMER_LIST = new ArrayList<>()
            def methodTimer = statisticExtension.getMethodTimer()
            if (methodTimer != null) {
                methodTimer.each { Map<String, Object> map ->
                    MethodTimerEntity entity = new MethodTimerEntity()
                    if (map.containsKey("time")) {
                        entity.time = map.get("time")
                    }
                    if (map.containsKey("owner")) {
                        entity.owner = map.get("owner")
                    }
                    METHOD_TIMER_LIST.add(entity)
                }
            }
        }
    }
}