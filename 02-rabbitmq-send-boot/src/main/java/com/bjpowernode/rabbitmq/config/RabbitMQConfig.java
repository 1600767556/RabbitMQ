package com.bjpowernode.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //配置一direct类型的交换机
    @Bean
    public DirectExchange directExdchange(){
        return new DirectExchange("bootDirectExchange");
    }
    //配置一个队列
    @Bean
    public Queue directQueue(){
        return new Queue("bootQueue");
    }
    //配置队列和交换机的绑定
    @Bean
    public Binding directBinding(Queue directQueue,DirectExchange directExchange){
        // 完成绑定 参数一 为需要绑定的对垒 参数二 绑定的交换机 参数三 绑定的Routingkey
        return BindingBuilder.bind(directQueue).to(directExchange).with("bootDirectRouting");

    }
    //配置一个fanout类型的交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    //配置一个topic类型的交换机
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

}
