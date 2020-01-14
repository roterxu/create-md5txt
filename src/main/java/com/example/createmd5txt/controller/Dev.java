package com.example.createmd5txt.controller;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author xujie
 */
@Slf4j
public class Dev {
    public static void main(String[] args) throws IOException {

        String plainPath = "C:\\tmp/phone-sha256 - 副本.txt";
        File file = new File(plainPath);
        List<String> plainList = readAndRemoveFirstLines(file,5000);

    }

    public static void resolve(String filePath, int batchSize, FileResolveTemplate fileResolveTemplate) throws IOException, ExecutionException, InterruptedException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
            int count = 0;
            String line;
            List<String> tmpList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                tmpList.add(line);
                if (count % batchSize == 0) {
                    fileResolveTemplate.resolve(tmpList);
                    tmpList.clear();
                }
            }
            if (tmpList.size() > 0) {
                fileResolveTemplate.resolve(tmpList);
            }
        }

    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        List<String> phoneSha256 = new ArrayList<>(5000);
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            phoneSha256.add(tempString);
            if (line % 5000 == 0) {
                break;
            }
            line++;
        }
        reader.close();

        return phoneSha256;

    }


    private static List<String> readAndRemoveFirstLines(File file, int lineNum) throws IOException {
        List<String> strList = new ArrayList<String>();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            //Initial write position
            long writePosition = raf.getFilePointer();
            for (int i = 0; i < lineNum; i++) {
                String line = raf.readLine();
                if (line == null) {
                    break;
                }
                strList.add(line);
            }
            // Shift the next lines upwards.
            long readPosition = raf.getFilePointer();

            byte[] buff = new byte[1024];
            int n;
            while (-1 != (n = raf.read(buff))) {
                raf.seek(writePosition);
                raf.write(buff, 0, n);
                readPosition += n;
                writePosition += n;
                raf.seek(readPosition);
            }
            raf.setLength(writePosition);
        } catch (IOException e) {
            log.error("readAndRemoveFirstLines error", e);
            throw e;
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                log.error("close RandomAccessFile error", e);
                throw e;
            }
        }

        return strList;
    }


}
