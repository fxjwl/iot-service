package com.data.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.data.contacts.Contacts;
import com.data.contacts.ContactsRepository;
import com.data.util.HttpClientUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private ContactsRepository contactsRepository;

    private static final String BASE_URL = "https://iot.inhand.com.cn";
    private static final String USER_NAME = "287357304@qq.com";
    private static final String PASSWORD = "shangzhi123456";

    @ApiOperation("用户登录")
    @RequestMapping(value = "/get-token", method = RequestMethod.GET)
    public DeviceTokenDto getToken() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("username", USER_NAME);
        map.put("password", PASSWORD);
        map.put("grant_type", "password");
        map.put("client_id", "000017953450251798098136");
        map.put("client_secret", "08E9EC6793345759456CB8BAE52615F3");
        map.put("password_type", "1");

        String url = BASE_URL + "/oauth2/access_token";

        Header header = new BasicHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

        String result = HttpClientUtil.doPost(url,map,header);

        JSONObject jsonpObject = JSON.parseObject(result);

        DeviceTokenDto deviceTokenDto = new DeviceTokenDto();

        deviceTokenDto.setAccessToken(jsonpObject.getString("access_token"));
        deviceTokenDto.setRefreshToken(jsonpObject.getString("refresh_token"));
        deviceTokenDto.setTokenType(jsonpObject.getString("token_type"));

        return deviceTokenDto;
    }

    @ApiOperation("获得设备信息")
    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public JSONObject getDevices(
            @RequestParam("name") String name,
            @RequestParam("number") String number,
            @RequestParam("status") String status
    ){
        DeviceTokenDto dto = getToken();

        String url = BASE_URL+"/api/devices?access_token="+dto.getAccessToken();

        url += "&cursor=0&limit=300&verbose=100";


        if(!StringUtils.isEmpty(name)) {
            url += "&name="+name;
        }

        if(!StringUtils.isEmpty(number)) {
            url += "&serial_number="+number;
        }

        if(status.equals("在线")){
            url += "&online=1";
        }

        if(status.equals("离线")){
            url += "&online=0";
        }

        String result = HttpClientUtil.doGet(url);

        JSONObject jsonpObject = JSON.parseObject(result);

        List<Contacts> contactsList = contactsRepository.findAll();

        if(contactsList == null || contactsList.size() == 0){
            return jsonpObject;
        }

        Map<String,Contacts> contactsMap = contactsList.stream().collect(Collectors.toMap(Contacts::getDeviceId,contacts -> contacts));

        JSONArray jsonArray = jsonpObject.getJSONArray("result");

        for(int i =0;i<jsonArray.size();i++) {
            String id = jsonArray.getJSONObject(i).getString("_id");
            Contacts contacts = contactsMap.get(id);
            String contactsName = contacts != null ? contacts.getContactsName() : "";
            String contactsMobile = contacts != null ? contacts.getContactsMobile() : "";

            jsonArray.getJSONObject(i).put("contactsName", contactsName);
            jsonArray.getJSONObject(i).put("contactsMobile", contactsMobile);
        }

        return jsonpObject;
    }


    @ApiOperation("获得所有设备总数、在线数、离线数")
    @RequestMapping(value = "/device-statistics", method = RequestMethod.GET)
    public DeviceStatisticsDto getDeviceStatistics(
    ){
        DeviceTokenDto dto = getToken();

        String url = BASE_URL+"/api/devices?access_token="+dto.getAccessToken();

        url += "&cursor=0&limit=300";

        String result = HttpClientUtil.doGet(url);

        JSONObject jsonpObject = JSON.parseObject(result);

        DeviceStatisticsDto deviceStatisticsDto = new DeviceStatisticsDto();

        JSONObject jo = jsonpObject.getJSONObject("stats");

        String total = jo.getString("total");
        String online = jo.getString("online");
        String offline = jo.getString("offline");

        deviceStatisticsDto.setTotal(Integer.parseInt(total));
        deviceStatisticsDto.setOnline(Integer.parseInt(online));
        deviceStatisticsDto.setOffline(Integer.parseInt(offline));

        return deviceStatisticsDto;
    }

    @ApiOperation("获得设备流量信息")
    @RequestMapping(value = "/netWork-flow-statistics", method = RequestMethod.GET)
    public List<NetworkFlowDto> getNetWorkFlowStatistics(){
        DeviceTokenDto dto = getToken();

        String url = BASE_URL+"/api/devices?access_token="+dto.getAccessToken();

        url += "&cursor=0&limit=300";

        String result = HttpClientUtil.doGet(url);

        JSONObject jsonpObject = JSON.parseObject(result);
        JSONArray jsonArray = jsonpObject.getJSONArray("result");

        String josn = "{\"resourceIds\":[";

        for (int n=0; n<jsonArray.size();n++) {
            JSONObject jo = jsonArray.getJSONObject(n);
            String id = jo.getString("_id");

            if(n+1 == jsonArray.size()) {
                josn += "\"" + id + "\"";
            }
            else {
                josn += "\"" + id + "\",";
            }
        }

        josn += "]}";

        List<NetworkFlowDto> list = new ArrayList<>();

        url = BASE_URL + "/api/traffic_month/list?access_token="+dto.getAccessToken()+"&month=";

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i =1; i<=12;i++) {
            String month =String.valueOf(year) + (i < 10 ? "0" + String.valueOf(i) : String.valueOf(i));

            result = HttpClientUtil.doPostJson(url+month,josn);

            JSONObject jsonpObject1 = JSON.parseObject(result);

            JSONArray jsonArray1 = jsonpObject1.getJSONArray("result");

            float total = 0.0f;

            for (int n=0; n<jsonArray1.size();n++) {
                JSONObject jo1 = jsonArray1.getJSONObject(n);
                String strTotal = jo1.getString("total");

                total+= Float.parseFloat(strTotal);
            }

            total =  Math.round(total/1024/1024 * 10) *0.1f;

            NetworkFlowDto  networkFlowDto = new NetworkFlowDto(i,total);

            list.add(networkFlowDto);
        }

        return list;
    }

    @ApiOperation("获得设备信息")
    @RequestMapping(value = "/network-flow/{month}/{id}", method = RequestMethod.GET)
    public List<NetworkFlowDto> getNetworkFlowByMonthAndId(
            @PathVariable("month") String month,
            @PathVariable("id") String id) throws ParseException {

        DeviceTokenDto dto = getToken();

        String url = BASE_URL+"/api/traffic_day?month="+month+"&device_id="+id+"&access_token="+dto.getAccessToken();

        String result = HttpClientUtil.doGet(url);

        JSONObject jsonpObject = JSON.parseObject(result);

        JSONArray jsonArray = jsonpObject.getJSONArray("result");

        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd");
        Date date = sdf.parse( month+"01" );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<NetworkFlowDto> networkFlowDtos = new ArrayList<>();

        for (int i=1;i<=days;i++) {
            String strDay = i > 9 ? String.valueOf(i) : "0"+String.valueOf(i);
            float total = 0.0f;

            for (int n=0; n<jsonArray.size();n++) {
                JSONObject jo = jsonArray.getJSONObject(n);
                String strDate = jo.getString("date");
                if(strDate.equals(month+strDay)) {
                    String strTotal = jo.getString("total");
                     total =  Math.round(Float.parseFloat(strTotal)/1024/1024 * 10) *0.1f;
                    break;
                }
            }

            networkFlowDtos.add(new NetworkFlowDto(i,total));
        }

        return networkFlowDtos;
    }

    @ApiOperation("获得设备信息的终端信息")
    @RequestMapping(value = "/clients//{deviceId}", method = RequestMethod.GET)
    public JSONObject getClientsByDeviceId(@PathVariable String deviceId){
        DeviceTokenDto dto = getToken();
        String url = BASE_URL+"/api/devices/"+deviceId+"/gateway/clients?access_token="+dto.getAccessToken();

        String result = HttpClientUtil.doGet(url);

        JSONObject jsonpObject = JSON.parseObject(result);

        return jsonpObject;
    }
}
