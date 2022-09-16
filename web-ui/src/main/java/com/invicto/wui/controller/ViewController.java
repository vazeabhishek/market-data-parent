package com.invicto.wui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.text.DecimalFormat;

@Controller
public class ViewController {

    DecimalFormat df = new DecimalFormat("#.####");


    @GetMapping({"/", "/prediction"})
    public String optionHome(Model model) {
        return "prediction";
    }

    @GetMapping("/future/long")
    public String futureLong(Model model) {
        return "future-long";
    }

    @GetMapping("/future/short")
    public String futureShort(Model model) {
        return "future-long";
    }


    @GetMapping("/equity")
    public String equity(Model model) {
        return "future-long";
    }

    /*@Autowired
    private DataService dataService;

    @GetMapping({"/", "/option"})
    public String optionHome(Model model) {
        List<OptionData> optionDataList = dataService.fetchOptionRows();
        Map<String, List<OptionData>> optionData = optionDataList.stream().collect(Collectors.groupingBy(OptionData::getIdentifier, Collectors.toList()));
        model.addAttribute("optionRows", optionData);
        return "option-home";
    }

    @GetMapping("/equity")
    public String fetchEquity(Model model) {
        List<EquityVo> equityVoList = dataService.currentEquitySnap();
        Map<String, List<EquityVo>> equityData = equityVoList.stream().collect(Collectors.groupingBy(EquityVo::getSymbol, Collectors.toList()));
        Map<String, List<EquityVo>> treeMap = new TreeMap<>(equityData);
        model.addAttribute("equityRows", treeMap);
        return "equity-home";
    }

    @GetMapping("/equity-daily/long")
    public String fetchEquityDailyLongs(Model model) {
        List<BhavDailyVo> dailyVoLongList = dataService.getLongs();
        Map<LocalDate, List<BhavDailyVo>> records = dailyVoLongList.stream()
                .collect(Collectors.groupingBy(
                        BhavDailyVo::getAnalytics_date,
                        Collectors.mapping(
                                BhavDailyVo::getThis, Collectors.toList()
                        )));
        TreeMap sortedMap = new TreeMap(Collections.reverseOrder());
        sortedMap.putAll(records);
        model.addAttribute("records", sortedMap);
        return "bhav-daily";

    }

    @GetMapping("/equity-daily/short")
    public String fetchEquityDailyShorts(Model model) {
        List<BhavDailyVo> dailyVoShortList = dataService.getShorts();
        Map<LocalDate, List<BhavDailyVo>> records = dailyVoShortList.stream().sorted()
                .collect(Collectors.groupingBy(
                        BhavDailyVo::getAnalytics_date,
                        Collectors.mapping(
                                BhavDailyVo::getThis, Collectors.toList()
                        )));
        TreeMap sortedMap = new TreeMap(Collections.reverseOrder());
        sortedMap.putAll(records);
        model.addAttribute("records", sortedMap);
        return "bhav-daily";

    }

    @GetMapping("/equity-daily/history/{symbol}")
    public String fetchEquityHistory(Model model, @PathVariable("symbol")String symbol) {
        List<EquityBhavVo> history = dataService.getEquityDailyHistory(symbol);
        model.addAttribute("data",history);
        return "equity-bhav-daily";
    }*/


}
