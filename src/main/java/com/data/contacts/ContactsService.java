package com.data.contacts;

import com.data.exception.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContactsService {
    @Autowired
    private ContactsRepository contactsRepository;

    public void createOrUpdateContacts(ContactsCreateOrUpdateDto contactsCreateOrUpdateDto){

        if(StringUtils.isEmpty(contactsCreateOrUpdateDto.getDeviceId())){
            throw new BadRequestException("找不到设备ID，请联系技术人员");
        }

        if(StringUtils.isEmpty(contactsCreateOrUpdateDto.getContactsName())){
            throw new BadRequestException("联系人姓名得为空");
        }

        if(StringUtils.isEmpty(contactsCreateOrUpdateDto.getContactsMobile())){
            throw new BadRequestException("联系人手机不得为空");
        }

        if(contactsCreateOrUpdateDto.getContactsMobile().length() != 11){
            throw new BadRequestException("联系人手机应该为11位");
        }

        String regex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(contactsCreateOrUpdateDto.getContactsMobile());
        boolean isMatch = m.matches();
        if(!isMatch){
            throw new BadRequestException("联系人手机格式不正确");
        }

        Contacts contacts = contactsRepository.getContactsByDeviceId(contactsCreateOrUpdateDto.getDeviceId());
        if (contacts == null){
            contacts = new Contacts();
        }

        BeanUtils.copyProperties(contactsCreateOrUpdateDto,contacts);

        contactsRepository.save(contacts);
    }
}
