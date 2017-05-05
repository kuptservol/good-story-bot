package ru.skuptsov.telegram.bot.goodstory.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "story_subscription")
public class Subscription {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    private String time;

    private String query;

    private String timezone;
}
