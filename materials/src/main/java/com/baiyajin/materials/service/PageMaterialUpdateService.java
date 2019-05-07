package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baiyajin.entity.bean.PageMunitUnifiedRule;
import com.baiyajin.materials.mapper.PageMaterialUpdateMapper;
import com.baiyajin.util.u.JsonUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PageMaterialUpdateService extends ServiceImpl<PageMaterialUpdateMapper,PageMaterialUpdata> implements PageMaterialUpdateInterface {

    @Autowired
    private PageMunitUnifiedRuleInterface pageMunitUnifiedRuleInterface;
    @Autowired
    private  PageMaterialPriceInterface pageMaterialPriceInterface;

    @Override
    public int receiveMaterialtPrice(Map<String,Object> map) {
            String dataSt = map.get("data").toString();
            //  String dataSt = "\"[{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2014\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"120\",\"mdate\":\"2019-04-30},{c1\":\"1\",\"c2\":\"12\",\"c3\":\"51\",\"mname\":\"钢绞线（钢丝束）\",\"mspec\":\"1×7-12.7-1860-GB/T5224-2011\",\"munit\":\"kg\",\"remark\":\"\",\"city\",\"area\":\"53\",\"price\":\"100\",\"mdate\":\"2019-04-30}]\"";

            //解析json内容
            List<PageMaterialUpdata> list = JsonUtil.jsonToList(dataSt, PageMaterialUpdata.class);
            //统一单位
            list = MunitUnified(list);

//            //保存
            for (PageMaterialUpdata l : list) {
                baseMapper.insert(l);
            }

            Map<String,Object> matparMap = new HashMap<>();
            Calendar cal=Calendar.getInstance();
            PageMaterialUpdata a = list.get(0);
            cal.setTime(a.getMdate());
            int year = cal.get(Calendar.YEAR);
            int month =cal.get( Calendar.MONTH);
            matparMap.put("year",year);
            matparMap.put("month",month);
            matparMap.put("mid",a.getMid());
            matparMap.put("area",a.getArea());
            //计算平均值
            List<Map<String,Object>> listmap =  getMaterialtAvgPrice(matparMap);
            System.out.println(listmap.get(0));
            //获取到历史月度数据（更新）
        PageMaterialPrice pageMaterialPrice = this.getMaterialtPrice(matparMap);

            if(pageMaterialPrice!=null){
               String priceSt =  listmap.get(0).get("price").toString();
               BigDecimal price = new BigDecimal(priceSt);
               //设置价格
                pageMaterialPrice.setPrice(price);
                matparMap.put("type",0);
                pageMaterialPrice = dataComputer(pageMaterialPrice,matparMap);
                System.out.println(pageMaterialPrice);
//                pageMaterialPriceInterface.updateById(pageMaterialPrice);

            }
        return 0;
    }

    /**
     * 计算指数，环比，同比
     * @param pageMaterialPrice
     * @param map
     * @return
     */
    public PageMaterialPrice dataComputer(PageMaterialPrice pageMaterialPrice,Map<String,Object> map){
        map.put("dateType",1);
        //获取月度基价
        PageMaterialPrice PageMaterialPriceBase = baseMapper.getMaterialtPriceByFilter(map);
        //获取月度环比价格
        map.put("dateType",2);
       PageMaterialPrice PageMaterialPriceHb = baseMapper.getMaterialtPriceByFilter(map);
        //获取月度同比价格
        map.put("dateType",3);
        PageMaterialPrice PageMaterialPriceTb = baseMapper.getMaterialtPriceByFilter(map);


        //[1 + （当期价格 - 第一季度价格）/第一季度价格]* 1000 = 指数
        BigDecimal exponent = pageMaterialPrice.getPrice().subtract(PageMaterialPriceBase.getPrice())
                .divide(PageMaterialPriceBase.getPrice(),BigDecimal.ROUND_DOWN)
                .add(new BigDecimal("1"))
                .multiply(new BigDecimal("1000"));
        //环比计算 = （当期 - 上期）/上期
        BigDecimal hb = pageMaterialPrice.getPrice()
                .subtract(PageMaterialPriceHb.getPrice())
                .divide(PageMaterialPriceHb.getPrice(),BigDecimal.ROUND_DOWN);
        BigDecimal tb = pageMaterialPrice.getPrice()
                .subtract(PageMaterialPriceTb.getPrice())
                .divide(PageMaterialPriceTb.getPrice(),BigDecimal.ROUND_DOWN);
        //  pageMaterialPrice.setPrice(exponent);
        pageMaterialPrice.setExponent(exponent);
        pageMaterialPrice.setHuanbi(hb);
        pageMaterialPrice.setTongbi(tb);
        return pageMaterialPrice;
    }


    @Override
    public List<Map<String, Object>> getMaterialtAvgPrice(Map<String, Object> map) {
        return baseMapper.getMaterialtAvgPrice(map);
    }

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




    /**
     * 单位统一
     * @param list
     * @return
     */
    private List<PageMaterialUpdata>  MunitUnified(List<PageMaterialUpdata> list){
            List<PageMunitUnifiedRule> ruleList = pageMunitUnifiedRuleInterface.selectByMap(new HashMap<String,Object>());
            //统一单位
            for(PageMaterialUpdata l:list){
                for(PageMunitUnifiedRule rl:ruleList){
                    if(rl.getMatchedNames().indexOf(l.getMunit())!=-1){
                        l.setMunit(rl.getMunitName());
                        l.setPrice(l.getPrice().multiply(rl.getCoefficient()));
                    }
                }
            }
            return list;
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
