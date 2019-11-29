package com.zaaach.transformerslayoutdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zaaach
 * @Date: 2019/11/25
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class DataFactory {

    public static List<Nav> loadData() {
        List<Nav> navs = new ArrayList<>();
        navs.add(new Nav(R.mipmap.ic_nav_charge, "流量", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_TK5jX.png"));
        navs.add(new Nav(R.mipmap.ic_nav_bill, "分账", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_AhKVK.png"));
        navs.add(new Nav(R.mipmap.ic_nav_device_binding, "绑定", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_kE4jY.png"));
        navs.add(new Nav(R.mipmap.ic_nav_member_manage, "会员", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_2yXk8.png"));
        navs.add(new Nav(R.mipmap.ic_nav_msg, "消息", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_irvud.png"));
        navs.add(new Nav(R.mipmap.ic_nav_download, "物料", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_PcmPU.png"));
        navs.add(new Nav(R.mipmap.ic_nav_excel, "营收", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_NeFKy.png"));
        navs.add(new Nav(R.mipmap.ic_nav_favor, "收藏", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_yUFai.png"));
        navs.add(new Nav(R.mipmap.ic_nav_goods, "商品", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_KahtV.png"));
        navs.add(new Nav(R.mipmap.ic_nav_group, "分组", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_vuZsr.png"));
        navs.add(new Nav(R.mipmap.ic_nav_devices, "设备统计", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_t4sRT.png"));
        navs.add(new Nav(R.mipmap.ic_nav_vip_card, "会员卡", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172730_j2td3.png"));
        navs.add(new Nav(R.mipmap.ic_nav_vip_integral, "积分", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172730_rArtS.png"));
        navs.add(new Nav(R.mipmap.ic_nav_members, "用户", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_LsMFQ.png"));
        navs.add(new Nav(R.mipmap.ic_nav_device_magage, "设备管理", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172629_UcuEH.png"));
        navs.add(new Nav(R.mipmap.ic_nav_shop_decorate, "装修", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_fleMs.png"));
        navs.add(new Nav(R.mipmap.ic_nav_order, "订单", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_zzyP2.png"));
        navs.add(new Nav(R.mipmap.ic_nav_service, "客服", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172705_Wuun5.png"));
        navs.add(new Nav(R.mipmap.ic_nav_shopping, "商城", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172730_VnmYC.png"));
        navs.add(new Nav(R.mipmap.ic_nav_stock, "库存", "https://c-ssl.duitang.com/uploads/item/201911/25/20191125172730_iSTcz.png"));
        return navs;
    }
}
