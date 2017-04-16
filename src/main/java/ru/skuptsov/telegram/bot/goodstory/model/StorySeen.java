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
@Builder
@Entity
@Table(name = "story_seen")
public class StorySeen {

    @Id
    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "story_id")
    public Long storyId;
}
