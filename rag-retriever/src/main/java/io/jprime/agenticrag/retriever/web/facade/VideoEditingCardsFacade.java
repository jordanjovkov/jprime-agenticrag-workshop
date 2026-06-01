package io.jprime.agenticrag.retriever.web.facade;

import io.jprime.agenticrag.retriever.domain.model.llmresponse.VideoEditingCardList;
import io.jprime.agenticrag.retriever.domain.service.VideoEditingCardsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VideoEditingCardsFacade {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardsFacade.class);

    private final VideoEditingCardsService videoEditingCardsService;

    public VideoEditingCardsFacade(VideoEditingCardsService videoEditingCardsService) {
        this.videoEditingCardsService = videoEditingCardsService;
    }

    public VideoEditingCardList getVideoEditingCardList() {
        log.info("[VideoEditingCardsFacade] getVideoEditingCardList called");
        return videoEditingCardsService.getVideoEditingCardList();
    }

    public VideoEditingCardList getVideoEditingCardListMultiDocument() {
        log.info("[VideoEditingCardsFacade] getVideoEditingCardListMultiDocument called");
        return videoEditingCardsService.getVideoEditingCardListMultiDocument();
    }
}
