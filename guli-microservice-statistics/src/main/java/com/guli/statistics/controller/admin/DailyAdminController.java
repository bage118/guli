package com.guli.statistics.controller.admin;

import com.guli.common.vo.R;
import com.guli.statistics.service.DailyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "统计分析")
@CrossOrigin
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyAdminController {

    @Autowired
    private DailyService dailyService;

    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }


    //对于表单的话用@GetMapping: 助于下次查询
    @GetMapping("show-chart/{begin}/{end}/{type}")
    public R showChart(
            @PathVariable String begin,
            @PathVariable String end,
            @PathVariable String type){

        Map<String,Object> map = dailyService.getCharData(begin,end,type);
        return R.ok().data(map);
    }
}
