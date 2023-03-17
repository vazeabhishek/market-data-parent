package com.invicto.wui.controller;

import com.invicto.wui.model.BuildUpVo;
import com.invicto.wui.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    DecimalFormat df = new DecimalFormat("#.####");
    DataService dataService;

    @Autowired
    public ViewController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping({"/", "/prediction"})
    public String optionHome(Model model) {
        model.addAttribute("data", dataService.getPrediction(LocalDate.now()));
        return "prediction";
    }

    @GetMapping("/future/long")
    public String futureLong(Model model) {
        List<BuildUpVo> buildUpVoList =  dataService.getLongsForNDays(10l);
        groupbyDate(model, buildUpVoList);
        return "fo-view";
    }

    @GetMapping("/future/short")
    public String futureShort(Model model) {
        List<BuildUpVo> buildUpVoList =  dataService.getShortsForNDays(10l);
        groupbyDate(model, buildUpVoList);
        return "fo-view";
    }

    @GetMapping("/equity")
    public String equity(Model model, @RequestParam("symbol") String symbol) {
        model.addAttribute("data", dataService.getEquityHistory(symbol));
        return "equity-view";
    }

    private void groupbyDate(Model model, List<BuildUpVo> buildUpVoList) {
        Map<LocalDate, List<BuildUpVo>> records = buildUpVoList.stream().sorted()
                .collect(Collectors.groupingBy(
                        BuildUpVo::getAnalyticsDate,
                        Collectors.mapping(BuildUpVo::self, Collectors.toList()
                        )));
        TreeMap sortedMap = new TreeMap(Collections.reverseOrder());
        sortedMap.putAll(records);
        model.addAttribute("records",sortedMap);
    }


}
