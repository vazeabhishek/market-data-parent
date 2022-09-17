package com.invicto.wui.service;

import com.invicto.wui.constants.Query;
import com.invicto.wui.model.BuildUpVo;
import com.invicto.wui.model.EquityVo;
import com.invicto.wui.model.PredictionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.invicto.wui.constants.Query.*;

@Service
@Slf4j
public class DataService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PredictionVO> getPrediction(LocalDate date) {
        return jdbcTemplate.query(getPredictionQuery(date), (resultSet, i) -> mapPredictionVo(resultSet, i));
    }

    public List<BuildUpVo> getLongsForNDays(Long days) {
        LocalDate from = LocalDate.now().minusDays(days);
        return jdbcTemplate.query(getGetLongBuildUpQuery(from), (resultSet, i) -> mapBuildUpVo(resultSet, i));
    }

    public List<BuildUpVo> getShortsForNDays(Long days) {
        LocalDate from = LocalDate.now().minusDays(days);
        return jdbcTemplate.query(getGetShortBuildUpQuery(from), (resultSet, i) -> mapBuildUpVo(resultSet, i));
    }

    public List<EquityVo> getEquityHistory(String equitySym) {
        return jdbcTemplate.query(getGetEquityHistory(equitySym), (resultSet, i) -> mapEquityVo(resultSet, i));
    }

    private BuildUpVo mapBuildUpVo(ResultSet resultSet, int i) {
        BuildUpVo buildUpVo = new BuildUpVo();
        try {
            buildUpVo.setSymbol(resultSet.getString("symbol"));
            buildUpVo.setAnalyticsDate(resultSet.getDate("analytics_date").toLocalDate());
            buildUpVo.setContractId(resultSet.getLong("contractId"));
            buildUpVo.setBuyersWonCount(resultSet.getLong("buyers_won_count"));
            buildUpVo.setSellersWonCount(resultSet.getLong("sellers_won_count"));
            buildUpVo.setLowerLowCount(resultSet.getLong("lower_low_count"));
            buildUpVo.setHigherHighCount(resultSet.getLong("higher_high_count"));
            buildUpVo.setExpiryDate(resultSet.getDate("expiry_dt").toLocalDate().format(dateTimeFormatter));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return buildUpVo;
    }

    private EquityVo mapEquityVo(ResultSet resultSet, int i) {
        EquityVo equityVo = new EquityVo();
        try {
            equityVo.setClose(resultSet.getDouble("close"));
            equityVo.setOpen(resultSet.getDouble("open"));
            equityVo.setHigh(resultSet.getDouble("high"));
            equityVo.setCollectionDate(resultSet.getDate("collection_date").toLocalDate());
            equityVo.setTicker(resultSet.getString("ticker"));
            equityVo.setPrevClose(resultSet.getDouble("prev_close"));
            equityVo.setLow(resultSet.getDouble("low"));
            equityVo.setVol(resultSet.getDouble("total_traded_qty"));
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }


        return equityVo;
    }

    private PredictionVO mapPredictionVo(ResultSet resultSet, int i) {
        PredictionVO predictionVO = new PredictionVO();

        return predictionVO;
    }
}
