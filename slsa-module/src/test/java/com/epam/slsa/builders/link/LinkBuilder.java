package com.epam.slsa.builders.link;

import com.epam.slsa.model.Link;
import com.epam.slsa.model.LinkType;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoach;
import static com.epam.slsa.builders.link.LinkInfo.*;

public class LinkBuilder {

    public static Link getLink() {
        return Link.builder()
                .type(LinkType.YOUTUBE)
                .url(url)
                .build();
    }

}
