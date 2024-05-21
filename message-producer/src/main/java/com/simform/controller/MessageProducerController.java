package com.simform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.simform.dto.Message;
import com.simform.util.ApiResponse;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class MessageProducerController {

    @Autowired
    private AmqpTemplate template;

    @Value("${aws.mq.exchange}")
    private String exchangeName;

    @Value("${aws.mq.routing-key}")
    private String routingKey;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody String xmlData) {
        String message = convertXmlToJson(xmlData);
        template.convertAndSend(exchangeName, routingKey, message);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Data sent to the consumer.");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getStatusCode()));
    }

    private String convertXmlToJson(String xmlData) {
        ObjectMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String jsonData = "";
        try {
            Message message = xmlMapper.readValue(xmlData, Message.class);
            jsonData = jsonMapper.writeValueAsString(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonData;
    }
}
