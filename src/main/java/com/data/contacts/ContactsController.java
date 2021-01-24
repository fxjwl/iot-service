package com.data.contacts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contacts")
@Api(value = "联系人相关接口", description = "联系人相关接口")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @ApiOperation("添加或者修改联系人")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createOrUpdateContacts(@RequestBody ContactsCreateOrUpdateDto contactsCreateOrUpdateDto){
        contactsService.createOrUpdateContacts(contactsCreateOrUpdateDto);
    }
}
