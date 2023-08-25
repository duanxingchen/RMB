package dzr.transaction.service.impl;


import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.JianCang;
import dzr.transaction.entity.JianCangDto;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.mapper.JianCangMapper;
import dzr.transaction.mapper.TranDetailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/10/26 14:38
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JianCangServiceImpl {
    private final TranDetailMapper tranDetailMapper;
    private final HolderMapper holderMapper;
    private final JianCangMapper jianCangMapper;

    List<JianCangDto> jianCangDtos = new ArrayList<>();

    public void calculate(SecurityCode securityCode) {
        log.info(securityCode.toString());
        jianCangDtos.clear();
        List<TranDetail> tranDetails = tranDetailMapper.selectByCode(securityCode.getCode());
        Holder holder = holderMapper.selectOneByCode(securityCode.getCode());

        tranDetails.stream().collect(Collectors.groupingBy(TranDetail::getReportDate))
            .forEach((date, tranDetails1) -> {
                tranDetails1.stream().collect(Collectors.groupingBy(TranDetail::getNature))
                        .forEach((nature, tranDetails2) -> {
                            JianCangDto jianCangDto = new JianCangDto();
                            jianCangDto.setCode(securityCode.getCode());
                            jianCangDto.setName(securityCode.getName());
                            jianCangDto.setReportDate(date);
                            double sumChangedPrice = tranDetails2.stream().mapToDouble(TranDetail::getChangedPrice).sum();
                            jianCangDto.setSumMemory(sumChangedPrice);
                            jianCangDto.setNature(nature);
                            jianCangDto.setTimes(tranDetails2.size());
                            jianCangDtos.add(jianCangDto);
                        });
            });
        jianCangDtos.sort(Comparator.comparing(JianCangDto::getReportDate).reversed());
        JianCang jianCang = new JianCang();
        jianCang.setCode(securityCode.getCode());
        jianCang.setName(securityCode.getName());
        jianCang.setDay3InMemory(inMemory(holder,jianCangDtos,3));
        jianCang.setDay3InTimes(inTimes(jianCangDtos,3));
        jianCang.setDay5InMemory(inMemory(holder, jianCangDtos,5));
        jianCang.setDay5InTimes(inTimes(jianCangDtos,5));
        jianCang.setDay10InMemory(inMemory(holder, jianCangDtos,10));
        jianCang.setDay10InTimes(inTimes(jianCangDtos,10));
        jianCang.setDay30InMemory(inMemory(holder, jianCangDtos,30));
        jianCang.setDay30InTimes(inTimes(jianCangDtos,30));
        jianCang.setDay60InMemory(inMemory(holder, jianCangDtos,60));
        jianCang.setDay60InTimes(inTimes(jianCangDtos,60));
        jianCangMapper.delete(jianCang);
        jianCangMapper.insert(jianCang);

    }

    private Double inMemory(Holder holder, List<JianCangDto> jianCangDtos, int days) {
        if (jianCangDtos.size() < days*2 || holder == null){
            return  null;
        }
        double inMemory = 0.0;
        for (int i = 0; i < days * 2; i++) {
            JianCangDto jianCangDto = jianCangDtos.get(i);
            if (jianCangDto.getNature().equals("M")){
                inMemory = inMemory + (jianCangDto.getSumMemory());
            }else {
                inMemory = inMemory - (jianCangDto.getSumMemory());
            }
        }
        return MathUtils.doubleRetain2Bit(inMemory/(holder.getFlowMarket()*100000000)*100);
    }

    private Double inTimes(List<JianCangDto> jianCangDtos, int days) {
        if (jianCangDtos.size() < days*2){
            return  null;
        }
        AtomicInteger inTimes = new AtomicInteger(0);
        jianCangDtos.stream().limit(days * 2).collect(Collectors.groupingBy(JianCangDto::getReportDate))
                .forEach((date, jianCangDtos1) -> {
                    if (jianCangDtos1.size()!=2){
                        return ;
                    }
                    if (jianCangDtos1.get(0).getNature().equals("M")) {
                        if (jianCangDtos1.get(0).getSumMemory() - jianCangDtos1.get(1).getSumMemory() > 0) {
                            inTimes.getAndIncrement();
                        }
                    } else {
                        if (jianCangDtos1.get(1).getSumMemory() - jianCangDtos1.get(0).getSumMemory() > 0) {
                            inTimes.getAndIncrement();
                        }
                    }
                });
        return MathUtils.doubleRetain2Bit(Double.valueOf(inTimes.get())/days*100);
    }
}
