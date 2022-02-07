package com.liu.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.commonutils.R;
import com.liu.educms.client.OssClient;
import com.liu.educms.entity.CrmBanner;
import com.liu.educms.mapper.CrmBannerMapper;
import com.liu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-01-07
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    private OssClient ossClient;


    @Override
    public void removeBanner(String id) {
        CrmBanner crmBanner = baseMapper.selectById(id);
        String url = crmBanner.getImageUrl();
        System.out.println(url);
        if(url!=null)
        {
            ossClient.deleteOssFile(url);
        }
        baseMapper.deleteById(id);
    }

    @Cacheable(key = "'selectIndexList'",value = "banner")
    @Override
    public List<CrmBanner> getSomeBanner() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //last方法，拼接sql语句
        wrapper.last("limit 3");
        List<CrmBanner> crmBanners = baseMapper.selectList(wrapper);
        return crmBanners;
    }
}
