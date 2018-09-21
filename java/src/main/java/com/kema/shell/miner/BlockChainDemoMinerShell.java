package com.kema.shell.miner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "com.kema.shell",
        excludeFilters = @ComponentScan.Filter(type= FilterType.REGEX, pattern="com\\.kema\\.shell\\.master\\..*"))
public class BlockChainDemoMinerShell {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BlockChainDemoMinerShell.class);
    }

}
