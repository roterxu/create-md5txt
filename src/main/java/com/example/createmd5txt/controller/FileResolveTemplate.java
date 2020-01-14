package com.example.createmd5txt.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author chenpeng
 * @date 2018/11/2
 */
public interface FileResolveTemplate {
    /**
     * 批量处理
     */
    void resolve(List<String> lines) throws ExecutionException, InterruptedException, IOException;
}
