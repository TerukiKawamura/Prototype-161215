/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    private String[] week_name = {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};
    private String[] weather_name = {"晴れ", "晴れのち雨", "雨", "くもり", "くもりのち晴れ", "雪", "雨のち晴れ"};

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        
        String responseMessage = "";
        
        if (event.getMessage() != null &&  event.getMessage().getText() != null) {
        	
        	String message = event.getMessage().getText();
        	responseMessage = message;
        	
			Calendar calendar = Calendar.getInstance(Locale.JAPAN);
			//int year = calendar.get(Calendar.YEAR);
			//int month = calendar.get(Calendar.MONTH) + 1;
			//int day = calendar.get(Calendar.DATE);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			//int minute = calendar.get(Calendar.MINUTE);
			//int second = calendar.get(Calendar.SECOND);
		    int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        	if (message.contains("今日")) {
        		if (message.contains("何曜")) {
        			responseMessage = String.format("%sです", week_name[week]);
        		} else if (message.contains("何日")) {
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        			responseMessage = String.format("%sです", sdf.format(calendar.getTime()));
        		}
        	} else if (message.contains("明日")) {

    		} else if (message.contains("次")) {
	    		if (message.contains("予約")) {
	    			calendar.add(Calendar.DATE, 10);
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    			responseMessage = String.format("%sの%d時です", sdf.format(calendar.getTime()), hour - 2 % 24);
	    		}
    		} else {
        		if (message.contains("天気")) {
        			responseMessage = String.format("%sです", weather_name[week]);
        		} else if (message.contains("ここはどこ")) {
        			responseMessage = "地球です";
        		}
    		}
        }
        
        return new TextMessage(responseMessage);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
