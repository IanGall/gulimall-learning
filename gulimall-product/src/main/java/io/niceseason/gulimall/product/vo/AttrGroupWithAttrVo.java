package io.niceseason.gulimall.product.vo;

import io.niceseason.gulimall.product.entity.AttrEntity;
import io.niceseason.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttrGroupWithAttrVo extends AttrGroupEntity {
    private List<AttrEntity> attrs;
}
