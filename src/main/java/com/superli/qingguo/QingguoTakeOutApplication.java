package com.superli.qingguo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class QingguoTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingguoTakeOutApplication.class, args);
        log.info("项目启动成功");
    }

}
