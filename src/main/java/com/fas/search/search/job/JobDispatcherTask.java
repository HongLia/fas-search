package com.fas.search.search.job;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobDispatcherTask implements JobRunner, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobDispatcherTask.class);
    private static final ConcurrentHashMap<String, JobRunner> JOB_RUNNER_MAP = new ConcurrentHashMap<String, JobRunner>();


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        //根据type选择对应的JobRunner运行
        Job job = jobContext.getJob();
        String type = job.getParam("name");
        if (type == null) type = "";
        JobRunner jobRunner = JOB_RUNNER_MAP.get(type.toUpperCase().trim());
        if (jobRunner == null) {
            return new Result(Action.EXECUTE_FAILED, "执行失败，请输入name参数。目前注册的任务参数有: " + getKeys());
        }
        return jobRunner.run(jobContext);
    }

    private List<String> getKeys() {
        List<String> l = new ArrayList<String>();
        for (String s : JOB_RUNNER_MAP.keySet()) {
            l.add(s);
        }
        return l;
    }

    /**
     * 从IOC容器中将JobRunner类型的bean放入map中
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, JobRunner> map = applicationContext.getBeansOfType(JobRunner.class);
        for (Map.Entry<String, JobRunner> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(this.getClass().getSimpleName())) continue;
            String name = generateJobRunnerName(entry);
            LOGGER.info("setup " + name + ".....");
            JOB_RUNNER_MAP.put(name.toUpperCase(), entry.getValue());
        }
    }

    /**
     * <p>查找译名</p>
     *
     * @param entry
     * @return
     */
    private String generateJobRunnerName(Map.Entry<String, JobRunner> entry) {
        String name = entry.getKey();
        return name;
    }
}
