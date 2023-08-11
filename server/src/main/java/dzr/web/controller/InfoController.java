package dzr.web.controller;



import dzr.holder.entity.Holder;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.plate.entity.PlateStock;
import dzr.plate.mapper.PlateStockMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author dzr
 * @Date 2022/10/9 17:31
 * @Version 1.0
 * @Description:
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {
    private final CompanyInfoMapper companyInfoMapper;
    private final HolderMapper holderMapper;
    private final HolderNumMapper holderNumMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;
    private final TransactionMapper transactionMapper;
    private final SecurityCodeMapper securityCodeMapper;
    private final PlateStockMapper plateStockMapper;


    @RequestMapping(method = RequestMethod.GET,value = "/baseInfo/{code}")
    public CompanyInfo selectCompanyInfo(@PathVariable("code") String code) {
        return companyInfoMapper.selectOneByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/holder/{code}")
    public Holder selectHolder(@PathVariable("code") String code) {
        return holderMapper.selectOneByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/holderNum/{code}")
    public List<HolderNum> selectHolderNum(@PathVariable("code") String code) {
        return holderNumMapper.selectByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/organization/{code}")
    public List<OrganizationDetailsHolder> selectOrganizationDetailsHolder(@PathVariable("code") String code) {
        return organizationDetailsHolderMapper.selectByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/transaction/{code}")
    public List<Transaction> selectTransaction(@PathVariable("code") String code) {
        return transactionMapper.selectByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/securityCode/all")
    public List<SecurityCode> selectSecurityCode() {
        return securityCodeMapper.selectAll();
    }

    @RequestMapping(method = RequestMethod.GET,value = "/plateStock/{code}")
    public List<PlateStock> selectPlateStock(@PathVariable("code") String code) {
        return plateStockMapper.selectByCode(code);
    }
}
