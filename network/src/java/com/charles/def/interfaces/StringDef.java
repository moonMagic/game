package com.charles.lee.cron.def.interfaces;

/**
 * 定义一系列的字符串常量，防止魔法值直接出现于代码之中
 *
 * @author charlesLee
 */
public interface StringDef {

    String JAR = ".jar";

    String CLASS = ".class";

    String TIMER_JOB = "job";

    String TRIGGER = "trigger";

    String ARGS = "args";

    String GET = "GET";

    String POST = "POST";

    String PORT = "port";


    String CONTENT_TYPE = "content-type";


    String FORM_DATA = "form_date";


    String CLIENT_IP = "ip";

    /**
     * 空数组定义
     */
    String[] NULL_ARRAYS = new String[]{};
    /**
     * xml类型配置文件的结尾类型
     */
    String END_WITH_XML_NAME = ".xml";

}
