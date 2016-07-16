/** 
* @title IndexController.java
* @author Andy Zhou/周海汉  
* @date：2016年4月11日 下午3:03:21 
* Copyright 2016 All right reserved.
*  
*/
package com.abloz;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    StringBuffer sbOut = new StringBuffer();
    String tempFileName = "/tmp/Test.java";

    private int writeTemp(String program) {

        try {
            FileWriter fw = new FileWriter(tempFileName);
            fw.write(program);
            fw.close();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            sbOut.append(e.getStackTrace());
            return -1;
        }
        return 0;
    }

    private int compile() {
        String cmd = "cd /tmp & javac -g -encoding UTF-8 -sourcepath /tmp " + tempFileName;

        return runShell(cmd);
    }

    private int runProgram() {
        String cmd = "java -cp /tmp Test";
        return runShell(cmd);
    }

    private int runShell(String cmd) {
        Process process = null;
        // "ps -aux"
        // List<String> processList = new ArrayList<String>();
        String[] command = { "/bin/sh", "-c", cmd };
        // StringBuffer sb = new StringBuffer();
        try {
            // ProcessBuilder pb = new ProcessBuilder(command);
            // process = pb.start();
            process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // logger.error("error:"+error.readLine());
            char[] cbuf = new char[1024];
            if (input.read(cbuf) >= 0) {
                sbOut.append(cbuf);
                logger.debug(cbuf.toString());
                while (input.read(cbuf) != -1) {
                    sbOut.append(cbuf);
                }
            } else {
                logger.error("input is empty");
                while (error.read(cbuf) != -1) {
                    sbOut.append(cbuf);
                }

            }

            input.close();
            error.close();
        } catch (IOException e) {
            e.printStackTrace();
            sbOut.append(e.getStackTrace());
            return -1;
        }

        // logger.debug("output:"+sb.toString());

        return 0;

    }

    private int handleProgram(String program) {
        int ret = writeTemp(program);
        if (ret < 0) {
            return ret;
        }
        ret = compile();
        if (ret < 0) {
            return ret;
        }
        ret = runProgram();
        if (ret < 0) {
            return ret;
        }
        return 0;
    }

    @RequestMapping(value = "cmd", method = RequestMethod.POST)
    @ResponseBody
    public String cmd(HttpServletRequest req, HttpServletResponse response, ModelMap m) {
        sbOut.setLength(0);
        runShell(req.getParameter("cmd"));
        m.addAttribute("result", sbOut);
        return sbOut.toString();
    }

    @RequestMapping(value = "program", method = RequestMethod.POST)
    @ResponseBody
    public String program(HttpServletRequest req, HttpServletResponse response, ModelMap m) {
        // response.setContentType("application/json;charset=UTF-8");
        sbOut.setLength(0);

        handleProgram(req.getParameter("cmd"));
        m.addAttribute("result", sbOut);
        return sbOut.toString();
    }
 
}
