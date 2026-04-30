package com.ccq.pojo.response;

import java.util.List;

public class ResListDTO {
    private Integer id;
    private String name;
    private Integer position;
    private List<ResCardDTO> cards;

    public ResListDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public List<ResCardDTO> getCards() { return cards; }
    public void setCards(List<ResCardDTO> cards) { this.cards = cards; }
}
