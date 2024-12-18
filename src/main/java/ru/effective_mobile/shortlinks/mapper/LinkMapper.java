package ru.effective_mobile.shortlinks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;
import ru.effective_mobile.shortlinks.entity.Link;

@Mapper
public interface LinkMapper {

    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    LinkResponse linkToResponse(Link link);

    Link linkFromRequest(LinkRequest linkRequest);
}