package com.example.createmd5txt.controller;

/**
 * @author chenpeng
 * @date 2018/11/2
 */
public interface LineFormatTemplate {
    /**
     * line格式化
     *
     * @param line 当前行
     * @return 格式化后行
     */
    String format(String line);
}
