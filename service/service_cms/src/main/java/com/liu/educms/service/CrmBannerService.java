package com.liu.educms.service;

import com.liu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-01-07
 */
public interface CrmBannerService extends IService<CrmBanner> {


    void removeBanner(String id);

    List<CrmBanner> getSomeBanner();
}
