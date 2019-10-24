package com.tfx0one.rabbitmqdemo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述
 *
 * @author 2fx0one
 * @version 1.0
 * @createDate 2019-10-24 14:22
 * @projectName sprinboot-rabbit-mq-demo
 */
@Data
@Accessors(chain = true)
public class DemoVo {

    private String a;
    private Integer b;
}
