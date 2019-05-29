package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.*;
import com.baiyajin.materials.mapper.PageMaterialUpdateMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baiyajin.util.u.JsonUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PageMaterialUpdateService extends ServiceImpl<PageMaterialUpdateMapper,PageMaterialUpdata> implements PageMaterialUpdateInterface {

    private static final String token = null;
    @Autowired
    private PageMunitUnifiedRuleInterface pageMunitUnifiedRuleInterface;
    @Autowired
    private  PageMaterialPriceInterface pageMaterialPriceInterface;
    @Autowired
    PageAreaInterface pageAreaInterface;
    @Override
    public int receiveMaterialtPrice(Map<String,Object> map) {
        String dataSt = map.get("data").toString();
//            map.get("token").toString();
        //  String dataSt = "\"[{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2014\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"120\",\"mdate\":\"2019-04-30},{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2011\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"100\",\"mdate\":\"2019-04-30}]\"";
        //解析json内容
        List<PageMaterialUpdata> list = JsonUtil.jsonToList(dataSt, PageMaterialUpdata.class);
//            //保存
        this.insertBatch(list);
//            for (PageMaterialUpdata l : list) {
//                baseMapper.insert(l);
//            }
        return 0;
    }




    @Transactional(rollbackFor = Exception.class)
    public void  updatePrice(){
        Integer year = 2018;
        Integer month = 2;

        updatePriceByMonth(year,month);

//        updatePriceByMonth(2018,9);
//        updatePriceByMonth(2018,10);
//        updatePriceByMonth(2018,11);
//        updatePriceByMonth(2018,12);
//        updatePriceByMonth(2019,1);
//        updatePriceByMonth(2019,2);
//        updatePriceByMonth(2019,3);

    }

    @Transactional(rollbackFor = Exception.class)
    public void  updatePriceByMonth(Integer year,Integer month){
        System.out.println("开始更新" + year + "年" + month + "月数据");
        Map<String,Object> map = new HashMap<>();
        map.put("year",year);
        map.put("month",month);
        //获取条件中年月的材料价格信息（统计到各区域c3）
        System.out.println("开始查询");
        List<BasePrice> c3BasePriceList = baseMapper.getPinfbPrice(map);
        if(c3BasePriceList.size()==0){
            System.out.println("没有需要处理的数据");
            return;
        }
        //材料名称列表
        List<MaterialCategory> matInfoList = baseMapper.getMaterialCategory();
        //获取地址信息
        List<PageArea> areaInfoList = pageAreaInterface.selectByMap(new HashMap<>());
        //计算好的数据列表
        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(new HashMap<>());
        System.out.println("结束查询");
        //获取单位转换规则
        List<PageMunitUnifiedRule> ruleList = pageMunitUnifiedRuleInterface.selectByMap(new HashMap<String,Object>());
        System.out.println("开始计算");
        //处理并统计各材料各区域平均价格
        List<PageMaterialPrice> basePriceList = monthAvgPrice(c3BasePriceList,ruleList);
        System.out.println("结束计算");
        System.out.println("更新开始");
        //保存或更新月度价格数据
        updatePrice(basePriceList,matInfoList,areaInfoList,baseList);
        System.out.println( year + "年" + month + "月数据更新结束");

    }



    /**
     * 处理并统计各材料各区域平均价格
     * @param basePriceList
     * @return
     */
    public  List<PageMaterialPrice> monthAvgPrice(List<BasePrice> basePriceList,List<PageMunitUnifiedRule> ruleList){
        List<PageMaterialPrice> pageMaterialPriceList = new ArrayList<>();
        //统计到各区域c3并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,3,3));
        //统计到各区域c2并统一单位
        pageMaterialPriceList.addAll( computePrice(basePriceList,ruleList,2,3));
        //统计到各区域c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,3));
        //统计到各市级c3并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,3,2));
        //统计到各市级c2并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,2,2));
        //统计到各市级c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,2));
        //统计省级c3并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,3,3));
        //统计省级c2并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,2,3));
        //统计省级c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,3));
        return pageMaterialPriceList;
    }

    /**
     * O 统计材料价格
     * @param basePriceList
     * @param ruleList
     * @param matlevel
     * @param areaLevel
     * @return
     */
    public List<PageMaterialPrice> computePrice(List<BasePrice> basePriceList,List<PageMunitUnifiedRule> ruleList,Integer matlevel,Integer areaLevel ){
        List<PageMaterialPrice> pageMaterialPriceList = new ArrayList<>();
        Map<Integer, Map<String, List<BasePrice>>> priceListMap = new HashMap<>();
        if(matlevel==3 && areaLevel==3){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC3,Collectors.groupingBy(BasePrice::getArea)));
        }
        if(matlevel==2 && areaLevel==3){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC2,Collectors.groupingBy(BasePrice::getArea)));
        }
        if(matlevel==1 && areaLevel==3){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC1,Collectors.groupingBy(BasePrice::getArea)));
        }

        if(matlevel==3 && areaLevel==2){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC3,Collectors.groupingBy(BasePrice::getCity)));
        }
        if(matlevel==2 && areaLevel==2){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC2,Collectors.groupingBy(BasePrice::getCity)));
        }
        if(matlevel==1 && areaLevel==2){
            priceListMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC1,Collectors.groupingBy(BasePrice::getCity)));
        }
        Map<Integer,List<BasePrice>> priceMap = new HashMap<>();

        Map<String,List<BasePrice>> tempMap =  new HashMap<>();
        if(matlevel==3 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC3));
            for(Integer key:priceMap.keySet()){
                tempMap.clear();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }
        }
        if(matlevel==2 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC2));
            for(Integer key:priceMap.keySet()){
                tempMap.clear();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }
        }
        if(matlevel==1 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC1));
            for(Integer key:priceMap.keySet()){
                tempMap.clear();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }
        }

        for(Integer key1:priceListMap.keySet()){
            for(String key2:priceListMap.get(key1).keySet()){
                List<BasePrice> list = priceListMap.get(key1).get(key2);
                Integer pid = 0;
                if(matlevel==3){
                    pid = list.get(0).getC2();
                }
                if(matlevel==2){
                    pid = list.get(0).getC1();
                }
                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(list,key1,pid,matlevel.toString(),"0",ruleList);
                pageMaterialPriceList.add(pageMaterialPrice);
            }
        }
        return pageMaterialPriceList;
    }




    //计算平均价格
    public PageMaterialPrice getMaterialtAvgPrice(List<BasePrice> basePriceList,Integer id,Integer pid,String level,String type,List<PageMunitUnifiedRule> ruleList){
        //统一单位
        basePriceList =  munitUnified(basePriceList,ruleList);
        BigDecimal avg =  basePriceList.stream().map(BasePrice::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add).divide(BigDecimal.valueOf(basePriceList.size()),BigDecimal.ROUND_DOWN);
//mat_pid,level,type
        PageMaterialPrice pageMaterialPrice = new PageMaterialPrice(basePriceList.get(0));
        pageMaterialPrice.setMid(id);
        pageMaterialPrice.setMat_pid(pid.toString());
        pageMaterialPrice.setLevel(level);
        pageMaterialPrice.setType(type);
        pageMaterialPrice.setPrice(avg);
        return pageMaterialPrice;
    }

    /**
     * 单位统一
     * @param list
     * @return
     */
    private List<BasePrice>  munitUnified(List<BasePrice> list,List<PageMunitUnifiedRule> ruleList ){
        //统一单位
        for(BasePrice l:list){
            for(PageMunitUnifiedRule rl:ruleList){
                if(rl.getMatchedNames().indexOf(l.getMunit())!=-1){
                    l.setMunit(rl.getMunitName());
                    l.setPrice(l.getPrice().multiply(rl.getCoefficient()));
                }
            }
        }
        return list;
    }


//    @Transactional(rollbackFor = Exception.class)
//    public void  updatePrice_bak(){
//        //获取待处理数据列表（需要更新的价格材料）
//        List<PageMaterialUpdata>  list = getPendingDisposalList();
//        System.out.println("开始数据更新");
//        if(list.size()==0) {
//            System.out.println("没有需要更新的数据");
//            System.out.println("数据更新结束");
//            return;
//        }
////        List<PageMaterialUpdata>  newList = new ArrayList<>();
//        //获取相关材料列表并统计计算各材料各区域平均价格(原始数据，月度)
//        //材料名称列表
//        List<MaterialCategory> matInfoList = baseMapper.getMaterialCategory();
//        //获取地址信息
//        List<PageArea> areaInfoList = pageAreaInterface.selectByMap(new HashMap<>());
//        //单位统一规则
//        List<PageMunitUnifiedRule> ruleList = pageMunitUnifiedRuleInterface.selectByMap(new HashMap<String,Object>());
//        //查询价格数据
//        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(new HashMap<>());
//       Map<String, List<PageMaterialPrice>> baseListMap = baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getType));
//
//        for(PageMaterialUpdata l:list){
//            System.out.println("开始月度更新");
//            List<PageMaterialPrice>  materialtPriceList = getMaterialtPriceList(l,"0",ruleList);
//            //更新月度
//            updatePrice(materialtPriceList,matInfoList,areaInfoList,baseListMap.get("0"));
//            System.out.println("结束月度更新");
//            System.out.println("开始季度更新");
//            //更新季度
//            List<PageMaterialPrice>  materialtPriceListQuarter = getMaterialtPriceList(l,"1",ruleList);
//            System.out.println(materialtPriceListQuarter.size());
//            updatePrice(materialtPriceListQuarter,matInfoList,areaInfoList,baseListMap.get("1"));
//            System.out.println("结束季度更新");
//
//            System.out.println("开始年度更新");
//            //更新年度
//            List<PageMaterialPrice>  materialtPriceListYear = getMaterialtPriceList(l,"2",ruleList);
//            updatePrice(materialtPriceListYear,matInfoList,areaInfoList,baseListMap.get("2"));
//            System.out.println("结束年度更新");
//            l.setState(1);
//        }
//        System.out.println("数据更新结束");
//        //更新状态为已处理
//        this.updateBatchById(list);
//    }

    /**
     * 更新月度数据
     * 数据列表，材料基本信息列表（更新材料名称）
     */
    public void updatePrice(List<PageMaterialPrice>  materialtPriceList, List<MaterialCategory> matInfoList, List<PageArea> areaInfoList, List<PageMaterialPrice> baseList){
        Map<String,Object> baseSelMap  = new HashMap<>();
        //计算数据
        List<PageMaterialPrice> materialtPriceListCom = dataComputer(materialtPriceList,baseList);
        //更新名称
        Map<Integer,List<MaterialCategory>> materialCategoryMap = matInfoList.stream().collect(Collectors.groupingBy(MaterialCategory::getId));
        //地址名称信息
        Map<String,List<PageArea>> areaInfoListMap = areaInfoList.stream().collect(Collectors.groupingBy(PageArea::getId));

        List<PageMaterialPrice> materialtPriceListUpdateName = new ArrayList<>();
        //更新材料名称及地址名称
        for(PageMaterialPrice mp:materialtPriceListCom){
            List<MaterialCategory> ll = materialCategoryMap.get(mp.getMid());
            List<PageArea> al = areaInfoListMap.get(mp.getArea());
            if(ll.size()>0) {
                mp.setMat_name( ll.get(0).getName());
            }
            if(al.size()>0){
                mp.setArea_name(al.get(0).getName());
            }
            materialtPriceListUpdateName.add(mp);
        }
        //更新变动的数据
        updatePriceData(materialtPriceListUpdateName,baseList);
    }





//    /**
//     * 更新季度数据
//     */
//    public void updatePriceQuarter(List<PageMaterialPrice>  materialtPriceList,PageMaterialUpdata uPageMaterialUpdata){
//        Map<String,Object> baseSelMap  = new HashMap<>();
//        baseSelMap.put("type",1);
//        System.out.println("开始查询---");
//        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(baseSelMap);
//        System.out.println("结束查询---");
//        List<PageMaterialPrice> ll = dataComputer(materialtPriceList,baseList);
//        //更新变动的数据
//        updatePriceData(ll,baseList);
//    }
//
//    /**
//     * 更新年度数据
//     */
//    public void updatePriceYear(List<PageMaterialPrice>  materialtPriceList,PageMaterialUpdata uPageMaterialUpdata){
//        Map<String,Object> baseSelMap  = new HashMap<>();
//        baseSelMap.put("type",2);
//        System.out.println("开始查询---");
//        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(baseSelMap);
//        System.out.println("结束查询---");
//        List<PageMaterialPrice> ll = dataComputer(materialtPriceList,baseList);
//
//        //更新变动的数据
//        updatePriceData(ll,baseList);
//    }

    /**
     * 更新价格数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePriceData(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList){
        List<PageMaterialPrice> inertList = new ArrayList<>();
        List<PageMaterialPrice> upList = new ArrayList<>();
        if(baseList.size()==0){
            inertList.addAll(pageMaterialPriceList);
        }else {
            Map<Integer, Map<String, List<PageMaterialPrice>>> ml = baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getMid,
                    Collectors.groupingBy(PageMaterialPrice::getArea
                    )));

            Map<String, PageMaterialPrice> map = new HashMap<>();
            for (PageMaterialPrice pageMaterialPrice : pageMaterialPriceList) {
                Map<String, List<PageMaterialPrice>> a = ml.get(pageMaterialPrice.getMid());
                if (ml.get(pageMaterialPrice.getMid()) == null || ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea()) == null) {
                    inertList.add(pageMaterialPrice);
                    continue;
                }
                List<PageMaterialPrice> timeList = ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea());
                boolean flg = true;
                for (PageMaterialPrice t : timeList) {
                    String paDate = DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy-MM");
                    String tDate = DateFormatUtil.dateToString(t.getMdate(), "yyyy-MM");
                    if (paDate.equals(tDate)) {
                        pageMaterialPrice.setId(t.getId());
                        upList.add(pageMaterialPrice);
                        flg = false;
                    }
                }
                if (flg) {
                    inertList.add(pageMaterialPrice);
                }
            }
        }
        System.out.println("更新记录:"+upList.size());
        System.out.println("插入记录:"+inertList.size());
        if(inertList.size()>0){
            pageMaterialPriceInterface.insertBatch(inertList);
        }
        if(upList.size()>0) {
            boolean df =  pageMaterialPriceInterface.updateBatchById(upList);
        }
        return null;
    }

    //获取待处理数据列表（需要更新的价格材料）
    public List<PageMaterialUpdata>  getPendingDisposalList(){
        Map<String,Object> map = new HashMap<>();
        map.put("state",0);
        List<PageMaterialUpdata> list = baseMapper.selectByMap(map);
        return list;
    }

    /**
     * 计算指数同比环比
     */
    public List<PageMaterialPrice> dataComputer(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList ){

        List<PageMaterialPrice> list = new ArrayList<PageMaterialPrice>();
        Map<Integer, Map<String, List<PageMaterialPrice>>> ml =  baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getMid,
                Collectors.groupingBy(PageMaterialPrice::getArea
                )));
        Map<String,PageMaterialPrice> map = new HashMap<>();

        for(PageMaterialPrice pageMaterialPrice:pageMaterialPriceList){

            Map<String, List<PageMaterialPrice>> a = ml.get(pageMaterialPrice.getMid());
            if(ml.get(pageMaterialPrice.getMid())==null || ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea())==null){
                list.add(dataComputer(pageMaterialPrice,map));
                continue;
            }

            List<PageMaterialPrice> timeList=  ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea());
            for(PageMaterialPrice t:timeList){
                map.put("base",t);
                map.put("hb",t);
                //(日期比较，如果s>e 返回1, s=e 返回0,s<e 返回-1)
                if(DateFormatUtil.compareDate(map.get("base").getMdate(),t.getMdate())>0){
                    map.put("base",t);
                }else if(DateFormatUtil.compareDate(pageMaterialPrice.getMdate(),t.getMdate())<0){
                    map.put("hb",t);
                }
                if(pageMaterialPrice.getMdate().getYear() > t.getMdate().getYear() &&
                        pageMaterialPrice.getMdate().getMonth()==t.getMdate().getMonth()){
                    map.put("tb",t);
                }
            }
            list.add(dataComputer(pageMaterialPrice,map));
        }

        return list;
    }






    /**
     * 计算指数，环比，同比
     * @param pageMaterialPrice
     * @param map
     * @return
     */
    public PageMaterialPrice dataComputer(PageMaterialPrice pageMaterialPrice,Map<String,PageMaterialPrice> map){

        //获取月度基价
        PageMaterialPrice PageMaterialPriceBase = map.get("base");
        //获取月度环比价格
        PageMaterialPrice PageMaterialPriceHb = map.get("hb");
        //获取月度同比价格
        PageMaterialPrice PageMaterialPriceTb = map.get("tb");


        if(PageMaterialPriceBase!=null){
            //[1 + （当期价格 - 第一季度价格）/第一季度价格]* 1000 = 指数
            BigDecimal exponent = pageMaterialPrice.getPrice().subtract(PageMaterialPriceBase.getPrice())
                    .divide(PageMaterialPriceBase.getPrice(),BigDecimal.ROUND_DOWN)
                    .add(new BigDecimal("1"))
                    .multiply(new BigDecimal("1000"));
            pageMaterialPrice.setExponent(exponent);
        }

        if(PageMaterialPriceHb!=null) {
            //环比计算 = （当期 - 上期）/上期
            BigDecimal hb = pageMaterialPrice.getPrice()
                    .subtract(PageMaterialPriceHb.getPrice())
                    .divide(PageMaterialPriceHb.getPrice(), BigDecimal.ROUND_DOWN);
            pageMaterialPrice.setHuanbi(hb);
        }
        if(PageMaterialPriceTb!=null) {
            BigDecimal tb = pageMaterialPrice.getPrice()
                    .subtract(PageMaterialPriceTb.getPrice())
                    .divide(PageMaterialPriceTb.getPrice(),BigDecimal.ROUND_DOWN);
            pageMaterialPrice.setTongbi(tb);
        }
        //  pageMaterialPrice.setPrice(exponent);
        return pageMaterialPrice;
    }


//    //获取相关材料列表并统计计算各材料各区域平均价格(原始数据，月度)
//    public List<PageMaterialPrice>  getMaterialtPriceList(PageMaterialUpdata pageMaterialUpdata,String type,List<PageMunitUnifiedRule> ruleList) {
//        pageMaterialUpdata.setType(type);
//
//        List<PageMaterialUpdata> list = baseMapper.getMaterialtPriceList(pageMaterialUpdata);
//
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> areaMap = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getArea,Collectors.groupingBy(PageMaterialUpdata::getC3)));
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> cityMap = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getCity,Collectors.groupingBy(PageMaterialUpdata::getC3)));
//        Map<Integer, List<PageMaterialUpdata>> poMap = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getC3));
////      Map<String, List<PageMaterialUpdata>> c3AndArea = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getArea,Collectors.groupingBy(PageMaterialUpdata::getC3)));
//
//         List<PageMaterialPrice>  pageMaterialPriceList =  new ArrayList<>();
//
//        for(Map<Integer, List<PageMaterialUpdata>> mp:areaMap.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC3(),mlist.get(0).getC2(),"3",type,ruleList);
//
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+","+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }

//        for(Map<Integer, List<PageMaterialUpdata>> mp:cityMap.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC3(),mlist.get(0).getC2(),"3",type,ruleList);
//                pageMaterialPrice.setArea(pageMaterialPrice.getCity());
//                pageMaterialPrice.setCity("53");
//                pageMaterialPriceList.add(pageMaterialPrice);
//            //    map.put(pageMaterialPrice.getArea()+","+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }
//
//        for(List<PageMaterialUpdata> mlist:poMap.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC3(),mlist.get(0).getC2(),"3",type,ruleList);
//                pageMaterialPrice.setArea("53");
//                pageMaterialPrice.setCity("0");
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+","+pageMaterialPrice.getMid(),pageMaterialPrice);
//        }
//
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> areaMapC2 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getArea,Collectors.groupingBy(PageMaterialUpdata::getC2)));
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> cityMapC2 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getCity,Collectors.groupingBy(PageMaterialUpdata::getC2)));
//        Map<Integer, List<PageMaterialUpdata>> poMapC2 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getC2));
//
//        for(Map<Integer, List<PageMaterialUpdata>> mp:areaMapC2.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC2(),mlist.get(0).getC1(),"2",type,ruleList);
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+",c2,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }
//
//        for(Map<Integer, List<PageMaterialUpdata>> mp:cityMapC2.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC2(),mlist.get(0).getC1(),"2",type,ruleList);
//                pageMaterialPrice.setArea(pageMaterialPrice.getCity());
//                pageMaterialPrice.setCity("53");
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+",c2,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }
//
//        for(List<PageMaterialUpdata> mlist:poMapC2.values()){
//            PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC2(),mlist.get(0).getC1(),"3",type,ruleList);
//            pageMaterialPrice.setArea("53");
//            pageMaterialPrice.setCity("0");
//            pageMaterialPriceList.add(pageMaterialPrice);
////            map.put(pageMaterialPrice.getArea()+",c2,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//        }
//
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> areaMapC1 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getArea,Collectors.groupingBy(PageMaterialUpdata::getC1)));
//        Map<String, Map<Integer, List<PageMaterialUpdata>>> cityMapC1 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getCity,Collectors.groupingBy(PageMaterialUpdata::getC1)));
//        Map<Integer, List<PageMaterialUpdata>> poListC1 = list.stream().collect(Collectors.groupingBy(PageMaterialUpdata::getC1));
//
//        for(Map<Integer, List<PageMaterialUpdata>> mp:areaMap.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC1(),mlist.get(0).getC1(),"1",type,ruleList);
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+",c1,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }
//
//        for(Map<Integer, List<PageMaterialUpdata>> mp:areaMapC1.values()){
//            for( List<PageMaterialUpdata> mlist:mp.values()){
//                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC1(),mlist.get(0).getC1(),"1",type,ruleList);
//                pageMaterialPrice.setArea(pageMaterialPrice.getCity());
//                pageMaterialPrice.setCity("53");
//                pageMaterialPriceList.add(pageMaterialPrice);
////                map.put(pageMaterialPrice.getArea()+",c1,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//            }
//        }
//
//        for(List<PageMaterialUpdata> mlist:poListC1.values()){
//            PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(mlist,mlist.get(0).getC1(),mlist.get(0).getC1(),"1",type,ruleList);
//            pageMaterialPrice.setArea("53");
//            pageMaterialPrice.setCity("0");
//            pageMaterialPriceList.add(pageMaterialPrice);
////            map.put(pageMaterialPrice.getArea()+",c1,"+pageMaterialPrice.getMid(),pageMaterialPrice);
//        }
//        return pageMaterialPriceList;
//    }




  /*  public void timeer() throws IOException {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

         System.out.println("定时任务测试");
        System.out.println("启动时间："+ DateFormatUtil.dateToString(new Date()));

        InputStreamReader isr = new InputStreamReader(new FileInputStream("d:\\log\\log.txt"), "GBK");
        bufferedReader = new BufferedReader(isr);
        bufferedWriter = new BufferedWriter(new FileWriter("d:\\log\\log.txt"));
        String s = "启动时间："+ DateFormatUtil.dateToString(new Date());
        while ((s = bufferedReader.readLine()) != null) {
            bufferedWriter.write(s);
            bufferedWriter.newLine();//按行读取，写入一个分行符，否则所有内容都在一行显示
        }
    }*/








//    public void aaa(List<PageMaterialUpdata>  list){
//        Map<String,Object> matparMap = new HashMap<>();
//        Calendar cal=Calendar.getInstance();
//        PageMaterialUpdata a = list.get(0);
//        cal.setTime(a.getMdate());
//        int year = cal.get(Calendar.YEAR);
//        int month =cal.get( Calendar.MONTH)+1;
//        matparMap.put("year",year);
//        matparMap.put("month",month);
//        matparMap.put("mid",a.getMid());
//        matparMap.put("area",a.getArea());
//
//        //计算平均值
//        List<Map<String,Object>> listmap =  getMaterialtAvgPrice(matparMap);
//
//        System.out.println(listmap.get(0));
//        //获取到历史月度数据（更新）
//        PageMaterialPrice pageMaterialPrice = this.getMaterialtPrice(matparMap);
//
//        if(pageMaterialPrice!=null){
//            String priceSt =  listmap.get(0).get("price").toString();
//            BigDecimal price = new BigDecimal(priceSt);
//            //设置价格
//            pageMaterialPrice.setPrice(price);
//            matparMap.put("type",0);
//            System.out.println(pageMaterialPrice);
//            pageMaterialPrice = dataComputer(pageMaterialPrice,matparMap);
//            System.out.println(pageMaterialPrice);
////                pageMaterialPriceInterface.updateById(pageMaterialPrice);
//        }
//    }



// /**
//     * 计算指数，环比，同比
//     * @param pageMaterialPrice
//     * @param map
//     * @return
//     */
//    public PageMaterialPrice dataComputer(PageMaterialPrice pageMaterialPrice,Map<String,Object> map){
//        map.put("dateType",1);
//        //获取月度基价
//        PageMaterialPrice PageMaterialPriceBase = baseMapper.getMaterialtPriceByFilter(map);
//        //获取月度环比价格
//        map.put("dateType",2);
//       PageMaterialPrice PageMaterialPriceHb = baseMapper.getMaterialtPriceByFilter(map);
//        //获取月度同比价格
//        map.put("dateType",3);
//        PageMaterialPrice PageMaterialPriceTb = baseMapper.getMaterialtPriceByFilter(map);
//
//        if(PageMaterialPriceBase!=null){
//            //[1 + （当期价格 - 第一季度价格）/第一季度价格]* 1000 = 指数
//            BigDecimal exponent = pageMaterialPrice.getPrice().subtract(PageMaterialPriceBase.getPrice())
//                    .divide(PageMaterialPriceBase.getPrice(),BigDecimal.ROUND_DOWN)
//                    .add(new BigDecimal("1"))
//                    .multiply(new BigDecimal("1000"));
//            pageMaterialPrice.setExponent(exponent);
//        }
//
//        if(PageMaterialPriceHb!=null) {
//            //环比计算 = （当期 - 上期）/上期
//            BigDecimal hb = pageMaterialPrice.getPrice()
//                    .subtract(PageMaterialPriceHb.getPrice())
//                    .divide(PageMaterialPriceHb.getPrice(), BigDecimal.ROUND_DOWN);
//            pageMaterialPrice.setHuanbi(hb);
//        }
//        if(PageMaterialPriceTb!=null) {
//            BigDecimal tb = pageMaterialPrice.getPrice()
//                    .subtract(PageMaterialPriceTb.getPrice())
//                    .divide(PageMaterialPriceTb.getPrice(),BigDecimal.ROUND_DOWN);
//            pageMaterialPrice.setTongbi(tb);
//        }
//        //  pageMaterialPrice.setPrice(exponent);
//        return pageMaterialPrice;
//    }






    @Override
    public PageMaterialPrice getMaterialtPrice(Map<String, Object> map) {
        List<PageMaterialPrice> list =  baseMapper.getMaterialtPrice(map);
        return list.size()>0?list.get(0):null;
    }


    /**
     * 获取价格
     * @param map type 0:月,1:季度，2:年
     * @return
     */
    public PageMaterialPrice getMaterialtPriceByBasePrice(Map<String, Object> map) {
        map.put("dateType",1);
        return baseMapper.getMaterialtPriceByFilter(map);
    }







//    @Test
//    public void test(){
//        Map<String,Object> map = new HashMap<>();
//        String dataSt = "\"[{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2014\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"120\",\"mdate\":\"2019-04-30},{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2011\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"100\",\"mdate\":\"2019-04-30}]\"";
//        map.put("data",dataSt);
//        receiveMaterialtPrice(map);
//    }
//
//    @Test
//    public void test2(){
//        BigDecimal a = new BigDecimal("1");
//        BigDecimal b = new BigDecimal("2");
//        BigDecimal c = new BigDecimal("3");
//        BigDecimal d = new BigDecimal("4");
//        BigDecimal sum = a.add(b).multiply(c).subtract(d);
//        System.out.println(sum);
//
//    }





}
