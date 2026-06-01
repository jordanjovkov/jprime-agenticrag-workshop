package io.jprime.agenticrag.retriever.domain.model.llmresponse;

import java.util.ArrayList;
import java.util.List;

public class VideoEditingCardList {

    private List<VideoEditingCard> cards;

    public VideoEditingCardList() {
        this.cards = new ArrayList<>();
    }

    public List<VideoEditingCard> getCards() {
        return cards;
    }

    public void setCards(List<VideoEditingCard> cards) {
        this.cards = cards;
    }
}
