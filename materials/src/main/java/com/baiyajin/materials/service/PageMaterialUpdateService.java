package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.*;
import com.baiyajin.materials.mapper.PageMaterialUpdateMapper;
import com.baiyajin.util.u.CustomException;
import com.baiyajin.util.u.DateFormatUtil;
import com.baiyajin.util.u.HashSalt;
import com.baiyajin.util.u.JsonUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PageMaterialUpdateService extends ServiceImpl<PageMaterialUpdateMapper,PageMaterialUpdata> implements PageMaterialUpdateInterface {

    private static final String tokenPrefix = "jiJia_";
    private static final String tokenSuffix = "_detectingSystem";
    @Autowired
    private PageMunitUnifiedRuleInterface pageMunitUnifiedRuleInterface;
    @Autowired
    private  PageMaterialPriceInterface pageMaterialPriceInterface;
    @Autowired
    PageAreaInterface pageAreaInterface;
    @Autowired
    PinfbPriceInterface pinfbPriceInterface;
    @Autowired
    MainPriceInterface mainPriceInterface;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int receiveMaterialtPrice(Map<String,Object> map) {
//        String data = map.get("data").toString();
//        String token = map.get("token").toString();
//        String matType = map.get("matType").toString();
//        //校验token
//        String serToken = tokenPrefix + data  +tokenSuffix;
//        String serTokenMD5 = HashSalt.getMD5(serToken);
//        System.out.println(serTokenMD5);
//        if(!serTokenMD5.equals(token.toLowerCase())){
//            throw new CustomException("token错误","-2");
//        }
//        Calendar calendar = Calendar.getInstance();
//        if(matType.equals("0")){
//            List<MainPrice> list = JsonUtil.jsonToList(data, MainPrice.class);
//            mainPriceInterface.insertBatch(list);
//            Set<String> yearAndMonthSet = new HashSet<>();
//            List<Map<String,Object>> dateList = new ArrayList<>();
//            //年月去重
//            for(MainPrice l:list){
//                calendar.setTime(l.getMdate());
//                yearAndMonthSet.add(calendar.get(Calendar.YEAR)+","+(calendar.get(Calendar.MONTH)+1));
//            }
//            //记录需要计算的年月，写到日志表
//            for(String st:yearAndMonthSet){
//                Map<String,Object> dateMap = new HashMap<>();
//                dateMap.put("year",st.split(",")[0]);
//                dateMap.put("month",st.split(",")[1]);
//                dateList.add(dateMap);
//            }
//            baseMapper.saveUpdatelog(dateList);
//        }
//        if(matType.equals("1")){
//            List<PinfbPrice> list = JsonUtil.jsonToList(data, PinfbPrice.class);
//            pinfbPriceInterface.insertBatch(list);
//            Set<String> yearAndMonthSet = new HashSet<>();
//            List<Map<String,Object>> dateList = new ArrayList<>();
//            //年月去重
//            for(PinfbPrice l:list){
//                calendar.setTime(l.getMtime());
//                yearAndMonthSet.add(calendar.get(Calendar.YEAR)+","+(calendar.get(Calendar.MONTH)+1));
//            }
//            //记录需要计算的年月，写到日志表
//            for(String st:yearAndMonthSet){
//                Map<String,Object> dateMap = new HashMap<>();
//                dateMap.put("year",st.split(",")[0]);
//                dateMap.put("month",st.split(",")[1]);
//                dateList.add(dateMap);
//            }
//            baseMapper.saveUpdatelog(dateList);
//        }
        updatePrice();

        return 0;
    }

    /**
     * 更新数据
     */
    public void updatePrice(){
        //查询未处理的月份
        List<Map<String,Object>> logList = baseMapper.selectUpdateLog();
        System.out.println(logList);
        //整理需要处理的年月


        //开始计算更新数据
    }


    /**
     * 计算更新数据
     * @param map
     */
    public void  updatePrice(Map<String,Object> map){
        //材料名称列表
        List<MaterialCategory> matInfoList = baseMapper.getMaterialCategory();
        //获取地址信息
        List<PageArea> areaInfoList = pageAreaInterface.selectByMap(new HashMap<>());
        //    System.out.println("结束查询");
        //获取单位转换规则
        List<PageMunitUnifiedRule> ruleList = pageMunitUnifiedRuleInterface.selectByMap(new HashMap<String,Object>());

        String yearSt = map.get("year").toString();
        String monthSt = map.get("month").toString();

        String[] years = yearSt.split(",");
        String[] months = monthSt.split(",");

        System.out.println(DateFormatUtil.dateToString(new Date()));
        for(String year:years){
            for(String month:months){
                updatePriceByMonth(Integer.parseInt(year),Integer.parseInt(month),matInfoList,areaInfoList,ruleList);
                updatePriceByQuarter(Integer.parseInt(year),Integer.parseInt(month));
               updatePriceByYear(Integer.parseInt(year));
            }
        }
        System.out.println("数据处理完成！");
        System.out.println(DateFormatUtil.dateToString(new Date()));
    }

    /**
     * 处理年度数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void  updatePriceByYear(Integer year){
        Map<String,Object> map = new HashMap<>();
        map.put("year",year);
        //获取指定季度月平均价格及指数
        List<PageMaterialPrice> materialPriceYearList = baseMapper.getPageMaterialPriceByYear(map);
        map.clear();
        map.put("type",2);
        //计算好的数据列表
        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(map);
        //计算同环比
        materialPriceYearList = dataComputerYear(materialPriceYearList,baseList);
        System.out.println("年度数据...");
        //保存或更新月度价格数据
        updatePriceData(materialPriceYearList,baseList,2);
    }

    /**
     * 处理季度数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void  updatePriceByQuarter(Integer year,Integer month){
        Map<String,Object> map = new HashMap<>();
        Integer quarter = 4;
        if(month>0 && month <4){
            quarter = 1;
        }
        if(month>=4 && month <7){
            quarter = 2;
        }
        if(month>=7 && month <10){
            quarter = 3;
        }
        map.put("year",year);
        map.put("quarter",quarter);
        //获取指定季度月平均价格及指数
        List<PageMaterialPrice> materialPriceQuarterList = baseMapper.getPageMaterialPriceByQuarter(map);

        map.clear();
        map.put("type",1);
        //计算好的数据列表
        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(map);

        //计算同环比
        materialPriceQuarterList = dataComputerQuarter(materialPriceQuarterList,baseList);
        System.out.println("季度数据...");
        //保存或更新月度价格数据
        updatePriceData(materialPriceQuarterList,baseList,1);
    }

    /**
     * 处理月度数据
     * @param year
     * @param month
     * @param matInfoList
     * @param areaInfoList
     * @param ruleList
     */
    @Transactional(rollbackFor = Exception.class)
    public void  updatePriceByMonth(Integer year,Integer month,List<MaterialCategory> matInfoList, List<PageArea> areaInfoList,
                                    List<PageMunitUnifiedRule> ruleList){
        System.out.println("开始更新" + year + "年" + month + "月数据");
        Map<String,Object> map = new HashMap<>();
        map.put("year",year);
        map.put("month",month);
        //获取条件中年月的材料价格信息（统计到各区域c3）
    //    System.out.println("开始查询");
        List<BasePrice> c4BasePriceList = baseMapper.getPinfbPrice(map);
//munitUnified
        if(c4BasePriceList.size()==0){
            System.out.println("没有需要处理的数据");
            return;
        }
        //按C3分类准备过滤掉单位不符合的材料
       Map<Integer,List<BasePrice>>  c4BasePriceListMap = c4BasePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC3));
        List<BasePrice>  c3BasePriceList = new ArrayList<>();
        for(List<BasePrice> c4BasePrice:c4BasePriceListMap.values()){
            c3BasePriceList.addAll(munitUnified(c4BasePrice,ruleList));
        }

        map.clear();
        map.put("type",0);
        //计算好的数据列表
        List<PageMaterialPrice> baseList = pageMaterialPriceInterface.selectByMap(map);

      //   System.out.println("开始计算");
        //处理并统计各材料各区域平均价格
        List<PageMaterialPrice> basePriceList = monthAvgPrice(c3BasePriceList,ruleList);
         //System.out.println("结束计算");
         //System.out.println("更新开始");
        //保存或更新月度价格数据
        updatePrice(basePriceList,matInfoList,areaInfoList,baseList);
       // System.out.println( year + "年" + month + "月数据更新结束");
    }

//    /**
//     * 处理并统计各材料各区域平均价格
//     * @param basePriceList
//     * @return
//     */
//    public  List<PageMaterialPrice> monthAvgPrice(List<BasePrice> basePriceList,List<PageMunitUnifiedRule> ruleList){
//
//        List<PageMaterialPrice> pageMaterialPriceList = new ArrayList<>();
//        //统计到各区域c3并统一单位
//        List<PageMaterialPrice> c3a3List = computePrice(basePriceList,ruleList,3,3);
//        pageMaterialPriceList.addAll(c3a3List);
//
//        //统计到各区域c2并统一单位
//        List<BasePrice> c3a3BList = pageMaterialPriceToBasePrice(c3a3List);
//        List<PageMaterialPrice> c2a3List =computePrice(c3a3BList,ruleList,2,3);
//        pageMaterialPriceList.addAll(c2a3List);
//
//        //统计到各区域c1并统一单位
//        List<BasePrice> c2a3BList = pageMaterialPriceToBasePrice(c2a3List);
//        List<PageMaterialPrice> c1a3List =computePrice(c2a3BList,ruleList,1,3);
//        pageMaterialPriceList.addAll(c1a3List);
//
//        //统计到各市级c3并统一单位
//        List<PageMaterialPrice> c3a2List = computePrice(basePriceList,ruleList,3,2);
//        pageMaterialPriceList.addAll(c3a2List);
//
//        //统计到各市级c2并统一单位
//        List<BasePrice> c3a2BList = pageMaterialPriceToBasePrice(c3a2List);
//        List<PageMaterialPrice> c2a2List =computePrice(c3a2BList,ruleList,2,2);
//        pageMaterialPriceList.addAll(c2a2List);
//
//        //统计到各市级c1并统一单位
//        List<BasePrice> c2a2BList = pageMaterialPriceToBasePrice(c2a2List);
//        List<PageMaterialPrice> c1a2List =computePrice(c2a2BList,ruleList,1,2);
//        pageMaterialPriceList.addAll(c1a2List);
//
//        //统计省级c3并统一单位
//        List<PageMaterialPrice> c3a1List = computePrice(basePriceList,ruleList,3,1);
//        pageMaterialPriceList.addAll(c3a1List);
//
//        //统计省级c2并统一单位
//        List<BasePrice> c3a1BList = pageMaterialPriceToBasePrice(c3a1List);
//        List<PageMaterialPrice> c2a1List =computePrice(c3a1BList,ruleList,2,1);
//        pageMaterialPriceList.addAll(c2a1List);
//        //统计省级c1并统一单位
//        List<BasePrice> c2a1BList = pageMaterialPriceToBasePrice(c2a1List);
//        List<PageMaterialPrice> c1a1List =computePrice(c2a1BList,ruleList,1,1);
//        pageMaterialPriceList.addAll(c1a1List);
//
//        return pageMaterialPriceList;
//    }
//
//
//    public List<BasePrice> pageMaterialPriceToBasePrice(List<PageMaterialPrice> pageMaterialPriceList){
//
//        BasePrice basePrice = new BasePrice();
//
//
//
//        return null;
//    }



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
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,2,3));
        //统计到各区域c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,3));
        //统计到各市级c3并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,3,2));
        //统计到各市级c2并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,2,2));
        //统计到各市级c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,2));
        //统计省级c3并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,3,1));
        //统计省级c2并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,2,1));
        //统计省级c1并统一单位
        pageMaterialPriceList.addAll(computePrice(basePriceList,ruleList,1,1));

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


        if(matlevel==3 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC3));
            for(Integer key:priceMap.keySet()){
                Map<String,List<BasePrice>> tempMap =  new HashMap<>();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }
        }
        if(matlevel==2 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC2));

            for(Integer key:priceMap.keySet()){
                Map<String,List<BasePrice>> tempMap =  new HashMap<>();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }


        }
        if(matlevel==1 && areaLevel==1){
            priceMap = basePriceList.stream().collect(Collectors.groupingBy(BasePrice::getC1));
            for(Integer key:priceMap.keySet()){
                Map<String,List<BasePrice>> tempMap =  new HashMap<>();
                tempMap.put("53",priceMap.get(key));
                priceListMap.put(key,tempMap);
            }
        }

        for(Integer key1:priceListMap.keySet()){
            for(String key2:priceListMap.get(key1).keySet()){
                List<BasePrice> list = priceListMap.get(key1).get(key2);
                Integer pid = 0;
                String city = list.get(0).getCity();
                if(areaLevel==1){
                    city = "0";
                }
                if(areaLevel==2){
                    city = "53";
                }
                if(matlevel==3){
                    pid = list.get(0).getC2();
                }
                if(matlevel==2){
                    pid = list.get(0).getC1();
                }
                PageMaterialPrice pageMaterialPrice = getMaterialtAvgPrice(list,key1,key2,city,pid,matlevel.toString(),"0",ruleList);
               if(pageMaterialPrice!=null){
                   pageMaterialPriceList.add(pageMaterialPrice);
               }
            }
        }
        return pageMaterialPriceList;
    }




    //计算平均价格
    public PageMaterialPrice getMaterialtAvgPrice(List<BasePrice> basePriceList,Integer id,String areaId,String city,Integer pid,String level,String type,List<PageMunitUnifiedRule> ruleList){
        //统一单位
        basePriceList =  munitUnified(basePriceList,ruleList);
        if(basePriceList.size()==0){
            return null;
        }

        BigDecimal avg =  basePriceList.stream().map(BasePrice::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add).divide(BigDecimal.valueOf(basePriceList.size()),BigDecimal.ROUND_DOWN);

        PageMaterialPrice pageMaterialPrice = new PageMaterialPrice(basePriceList.get(0));
        pageMaterialPrice.setMid(id);
        pageMaterialPrice.setMat_pid(pid.toString());
        pageMaterialPrice.setLevel(level);
        pageMaterialPrice.setType(type);
        pageMaterialPrice.setPrice(avg);
        pageMaterialPrice.setCity(city);
        pageMaterialPrice.setArea(areaId);
        return pageMaterialPrice;
    }






    /**
     * 单位统一
     * @param list
     * @return
     */
    private List<BasePrice>  munitUnified(List<BasePrice> list,List<PageMunitUnifiedRule> ruleList ){
        //统一单位
        List<BasePrice> reList = new ArrayList<>();
        Map<String,List<BasePrice>>  munitTypeMap=  list.stream().collect(Collectors.groupingBy(BasePrice::getMunit));
        Set<Integer> munitSet = new HashSet<>();

        for(String munit: munitTypeMap.keySet()){
            for(PageMunitUnifiedRule pr:ruleList){
                if(("," + pr.getMatchedNames() + ",").indexOf(","+munit+",")!=-1){
                    munitSet.add(pr.getType());
                }
            }
        }

        if(munitSet.size()>1) {
            return reList;
        }

        for(BasePrice l:list){
            for(PageMunitUnifiedRule rl:ruleList){
                if( ("," + rl.getMatchedNames() + ",").indexOf(","+l.getMunit()+",")!=-1){
                    l.setPrice(l.getPrice().multiply(rl.getCoefficient()));
                    l.setMunit(rl.getMunitName());
                    reList.add(l);
                }
            }
        }
        return reList;
    }



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
            Date date = DateFormatUtil.setDate(mp.getMdate(),5,2);
            mp.setMdate(new Timestamp(date.getTime()));
            if(ll.size()>0) {
                mp.setMat_name( ll.get(0).getName());
            }
            if(al.size()>0){
                mp.setArea_name(al.get(0).getName());
            }

            materialtPriceListUpdateName.add(mp);
        }
        //更新变动的数据
        updatePriceData(materialtPriceListUpdateName,baseList,0);
    }




    /**
     * 更新价格数据
     * type 0月度，1季度，2年度
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePriceData(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList,Integer type){
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
                    String paYear = DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy");
                    String tYear = DateFormatUtil.dateToString(t.getMdate(), "yyyy");
                    if(type==1){
                        paDate = paYear + DateFormatUtil.getQuarter(pageMaterialPrice.getMdate());
                        tDate = tYear + DateFormatUtil.getQuarter(t.getMdate());
                    }
                    if(type==2){
                        paDate = paYear;
                        tDate = tYear;
                    }

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

        System.out.println("原始记录:"+pageMaterialPriceList.size());
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

//    /**
//     * 计算指数同比环比
//     */
//    public List<PageMaterialPrice> dataComputerYear(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList ){
//
//        List<PageMaterialPrice> list = new ArrayList<PageMaterialPrice>();
//        Map<Integer, Map<String, List<PageMaterialPrice>>> ml =  baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getMid,
//                Collectors.groupingBy(PageMaterialPrice::getArea
//                )));
//        Map<String,PageMaterialPrice> map = new HashMap<>();
//        for(PageMaterialPrice pageMaterialPrice:pageMaterialPriceList){
//            if(ml.get(pageMaterialPrice.getMid())==null || ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea())==null){
//                list.add(dataComputer(pageMaterialPrice,map));
//                continue;
//            }
//            List<PageMaterialPrice> timeList=  ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea());
//            for(PageMaterialPrice t:timeList){
//                Integer pageMaterialPriceYear =Integer.parseInt(DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy"));
//                Integer tYear = Integer.parseInt(DateFormatUtil.dateToString(t.getMdate(), "yyyy"));
//                if(pageMaterialPriceYear > tYear){
//                    map.put("base",t);
//                    map.put("hb",t);
//                    map.put("tb",t);
//                }
//            }
//            list.add(dataComputer(pageMaterialPrice,map));
//        }
//        return list;
//    }

    /**
     * 计算指数同比环比
     */
    public List<PageMaterialPrice> dataComputerYear(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList ){

        List<PageMaterialPrice> list = new ArrayList<PageMaterialPrice>();
        Map<Integer, Map<String, List<PageMaterialPrice>>> ml =  baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getMid,
                Collectors.groupingBy(PageMaterialPrice::getArea
                )));
        Map<String,PageMaterialPrice> map = new HashMap<>();
        for(PageMaterialPrice pageMaterialPrice:pageMaterialPriceList){
            if(ml.get(pageMaterialPrice.getMid())==null || ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea())==null){
                list.add(dataComputer(pageMaterialPrice,map));
                continue;
            }
            List<PageMaterialPrice> timeList=  ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea());
            map.clear();
            for(PageMaterialPrice t:timeList){
                Integer pageMaterialPriceYear =Integer.parseInt(DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy"));
                Integer tYear = Integer.parseInt(DateFormatUtil.dateToString(t.getMdate(), "yyyy"));
                Integer basePageMaterialPriceYear = pageMaterialPriceYear;
                if(map.get("base")!=null){
                    basePageMaterialPriceYear = DateFormatUtil.getQuarter(map.get("base").getMdate());
                }
                if(map.get("base")!=null){
                    pageMaterialPriceYear = DateFormatUtil.getQuarter(map.get("base").getMdate());
                }
                if(basePageMaterialPriceYear > tYear ){
                    map.put("base",t);
                }
                if(pageMaterialPriceYear  >tYear ){
                    map.put("hb",t);
                    map.put("tb",t);
                }
                //不是同一季度取最小的
//                if(pageMaterialPriceYear * 10 + pageMaterialPriceQuarter > tYear * 10 + tQuarter){
//                    map.put("base",t);
//                    map.put("hb",t);
//                }
//                //同季度不同年
//                if(pageMaterialPriceYear > tYear && pageMaterialPriceQuarter==tQuarter){
//                    map.put("tb",t);
//                }
            }
            list.add(dataComputer(pageMaterialPrice,map));
        }
        return list;
    }

    /**
     * 计算指数同比环比
     */
    public List<PageMaterialPrice> dataComputerQuarter(List<PageMaterialPrice> pageMaterialPriceList, List<PageMaterialPrice> baseList ){

        List<PageMaterialPrice> list = new ArrayList<PageMaterialPrice>();
        Map<Integer, Map<String, List<PageMaterialPrice>>> ml =  baseList.stream().collect(Collectors.groupingBy(PageMaterialPrice::getMid,
                Collectors.groupingBy(PageMaterialPrice::getArea
                )));
        Map<String,PageMaterialPrice> map = new HashMap<>();

        for(PageMaterialPrice pageMaterialPrice:pageMaterialPriceList){

            if(ml.get(pageMaterialPrice.getMid())==null || ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea())==null){
                list.add(dataComputer(pageMaterialPrice,map));
                continue;
            }

            List<PageMaterialPrice> timeList=  ml.get(pageMaterialPrice.getMid()).get(pageMaterialPrice.getArea());
            map.clear();
            for(PageMaterialPrice t:timeList){
                Integer pageMaterialPriceYear =Integer.parseInt(DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy"));
                Integer tYear = Integer.parseInt(DateFormatUtil.dateToString(t.getMdate(), "yyyy"));
                Integer pageMaterialPriceQuarter = DateFormatUtil.getQuarter(pageMaterialPrice.getMdate());
                Integer tQuarter = DateFormatUtil.getQuarter(t.getMdate());
                String pageMaterialPriceMonthSt =DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy-MM");
                String tMonthSt = DateFormatUtil.dateToString(t.getMdate(), "yyyy-MM");
                Integer pageMaterialPriceMonth = Integer.parseInt(pageMaterialPriceMonthSt.split("-")[1]);
                Integer tMonth = Integer.parseInt(tMonthSt.split("-")[1]);
                Integer baseQuarter = 0;
                Integer baseYear = 0;
                Integer basePageMaterialPriceYear = pageMaterialPriceYear;
                Integer basePageMaterialPriceQuarter = pageMaterialPriceQuarter;
                if(map.get("base")!=null){
                    basePageMaterialPriceYear = DateFormatUtil.getQuarter(map.get("base").getMdate());
                    basePageMaterialPriceQuarter = Integer.parseInt(DateFormatUtil.dateToString(map.get("base").getMdate(), "yyyy"));
                }
                if(basePageMaterialPriceYear * 10 +basePageMaterialPriceQuarter > tYear * 10 +tQuarter){
                    if(pageMaterialPrice.getMid()==128 && pageMaterialPrice.getArea().equals("53"))   {
                        System.out.println("tbase"+t);
                        System.out.println(baseYear);
                        System.out.println(baseQuarter );
                        System.out.println(tYear );
                        System.out.println(tQuarter );
                    }
                    map.put("base",t);
                }
                if(pageMaterialPriceYear * 10 +pageMaterialPriceQuarter >tYear * 10 + tQuarter){
                    if(pageMaterialPrice.getMid()==128 && pageMaterialPrice.getArea().equals("53"))   {
                        System.out.println("t"+t);
                    }
                    map.put("hb",t);
                }
                if(pageMaterialPriceYear > tYear &&
                        pageMaterialPrice.getMdate().getMonth()==t.getMdate().getMonth()){
                    map.put("tb",t);
                }
                //不是同一季度取最小的
//                if(pageMaterialPriceYear * 10 + pageMaterialPriceQuarter > tYear * 10 + tQuarter){
//                    map.put("base",t);
//                    map.put("hb",t);
//                }
//                //同季度不同年
//                if(pageMaterialPriceYear > tYear && pageMaterialPriceQuarter==tQuarter){
//                    map.put("tb",t);
//                }
            }
            list.add(dataComputer(pageMaterialPrice,map));
        }
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
            map.clear();
            for(PageMaterialPrice t:timeList){

                Integer pageMaterialPriceYear =Integer.parseInt(DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy"));
                Integer tYear = Integer.parseInt(DateFormatUtil.dateToString(t.getMdate(), "yyyy"));
                String pageMaterialPriceMonthSt =DateFormatUtil.dateToString(pageMaterialPrice.getMdate(), "yyyy-MM");
                String tMonthSt = DateFormatUtil.dateToString(t.getMdate(), "yyyy-MM");
                Integer pageMaterialPriceMonth = Integer.parseInt(pageMaterialPriceMonthSt.split("-")[1]);
                Integer tMonth = Integer.parseInt(tMonthSt.split("-")[1]);

//                map.put("base",t);
//                map.put("hb",t);
                //(日期比较，如果s>e 返回1, s=e 返回0,s<e 返回-1)
                if(map.get("base")==null || DateFormatUtil.compareDate(map.get("base").getMdate(),t.getMdate())>0){
                    map.put("base",t);
                }

                if(pageMaterialPriceYear * 10 +pageMaterialPriceMonth >tYear * 10 + tMonth){
                    if(pageMaterialPrice.getMid()==128 && pageMaterialPrice.getArea().equals("53"))   {
                        System.out.println("t"+t);
                    }
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

            //（当期价格/基准期价格）×定基指数(1000)
            BigDecimal exponent = pageMaterialPrice.getPrice()
                    .divide(PageMaterialPriceBase.getPrice(),15,BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal("1000"));
            pageMaterialPrice.setExponent(exponent);
        }else{
            pageMaterialPrice.setExponent(new BigDecimal("1000"));
        }

        if(PageMaterialPriceHb!=null) {
            //环比=(当期指数-上期指数)/上期指数×100%
            BigDecimal hb = pageMaterialPrice.getExponent()
                    .subtract(PageMaterialPriceHb.getExponent())
                    .divide(PageMaterialPriceHb.getExponent(), 15,BigDecimal.ROUND_DOWN);
            pageMaterialPrice.setHuanbi(hb);
        }else{
            pageMaterialPrice.setHuanbi(null);
        }


        if(PageMaterialPriceTb!=null) {
            //同比=(当期指数-去年同期指数)/去年同期指数×100%
            BigDecimal tb = pageMaterialPrice.getExponent()
                    .subtract(PageMaterialPriceTb.getExponent())
                    .divide(PageMaterialPriceTb.getExponent(),15,BigDecimal.ROUND_DOWN);
            pageMaterialPrice.setTongbi(tb);
        }else{
            pageMaterialPrice.setTongbi(null);
        }

        //  pageMaterialPrice.setPrice(exponent);
        return pageMaterialPrice;
    }

//
//    /**
//     * 计算指数，环比，同比
//     * @param pageMaterialPrice
//     * @param map
//     * @return
//     */
//    public PageMaterialPrice dataComputer(PageMaterialPrice pageMaterialPrice,Map<String,PageMaterialPrice> map){
//
//        //获取月度基价
//        PageMaterialPrice PageMaterialPriceBase = map.get("base");
//        //获取月度环比价格
//        PageMaterialPrice PageMaterialPriceHb = map.get("hb");
//        //获取月度同比价格
//        PageMaterialPrice PageMaterialPriceTb = map.get("tb");
//
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
//
//





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
