package io.niceseason.gulimall.product.vo;

import io.niceseason.gulimall.product.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttrVo extends AttrEntity {
    private Long attrGroupId;
}
