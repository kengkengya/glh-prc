package org.example.rpc.consumer.controller;

import org.example.HelloService;
import org.example.rpc.client.annotation.RpcAutowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * hello world
 *
 * @author guolonghang
 */
@Controller
public class HelloWorldController {

    @RpcAutowired(version = "1.0")
    private HelloService helloService;

    @GetMapping("/hello/world")
    public ResponseEntity<String> pullServiceInfo(@RequestParam("name") String name) {
        return ResponseEntity.ok(helloService.sayHello(name));
    }


}