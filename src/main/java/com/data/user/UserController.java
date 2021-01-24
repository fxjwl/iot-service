package com.data.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public  String login(@RequestParam("username") String name,
                         @RequestParam("password") String password) {
        if (name.equals("admin") && password.equals("T@123456")) {
            return "admin";
        } else {
            return "";
        }
    }

    @ApiOperation("用户登录")
    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    public UserDto getInfo(@RequestParam("token") String token){
        if(!token.equals("admin"))
            return null;


        UserDto dto = new UserDto();
        dto.setName(token);
        dto.setName(token);
        return dto;
    }

    @ApiOperation("用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.PUT)
    public String logOut(){
        return null;
    }
}
