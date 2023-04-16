package io.niceseason.gulimall.third.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import io.niceseason.common.utils.R;
import io.niceseason.gulimall.third.config.MinioTemplate;
import io.niceseason.gulimall.third.config.OssConfig;
import io.niceseason.gulimall.third.constant.OssType;
import io.niceseason.gulimall.third.vo.OssVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class OssController {

    @Autowired
    OSS ossClient;
    @Value ("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint ;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    String bucket ;

    @Value("${spring.cloud.alicloud.access-key}")
    String accessId ;
    @Value("${spring.cloud.alicloud.secret-key}")
    String accessKey ;
    private static final Logger log = LoggerFactory.getLogger(OssController.class);

    /**
     * 上传的文件夹(根据时间确定)
     */
    public static final String NORM_DAY_PATTERN = "yyyy/MM/dd";

    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private MinioTemplate minioTemplate;


    @GetMapping(value = "/oss/info")
    public R info(@RequestParam("fileNum") Integer fileNum) {
        OssVO ossVO = new OssVO();
        // minio文件上传
        if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            fillMinIoInfo(ossVO, fileNum);
        }
        return R.ok().setData(ossVO);
    }

    private void fillMinIoInfo(OssVO ossVo, Integer fileNum) {
        List<OssVO> ossVOList = new ArrayList<>();
        for (int i = 0; i<fileNum; i++) {
            OssVO oss = loadOssVO(new OssVO());
            String actionUrl = minioTemplate.getPresignedObjectUrl(oss.getDir() + oss.getFileName());
            oss.setActionUrl(actionUrl);
            ossVOList.add(oss);
        }
        ossVo.setOssList(ossVOList);
    }

    private OssVO loadOssVO(OssVO ossVo) {
        String dir = DateUtil.format(new Date(), NORM_DAY_PATTERN)+ "/";
        String fileName = IdUtil.simpleUUID();
        ossVo.setDir(dir);
        ossVo.setFileName(fileName);
        return ossVo;
    }


    @RequestMapping("/oss/policy")
    public R policy(){

        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint

        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format; // 用户上传文件时指定的前缀。

        Map<String, String> respMap=null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap= new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return R.ok().put("data",respMap);
    }
}
