package com.example.YNN.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityQuestion {
    BIRTH_CITY("당신이 태어난 도시의 이름은 무엇입니까?"),FIRST_SCHOOL_NAME("당신의 첫 번째 학교 이름은 무엇입니까?"),
    FAVORITE_FOOD("당신이 가장 좋아하는 음식의 이름은 무엇입니까?"),FIRST_PET_NAME("당신의 첫 번째 애완동물의 이름은 무엇입니까?"),
    MOTHER_NAME("당신의 어머니의 이름은 무엇입니까?");

    private final String question;


    public static SecurityQuestion fromString(String question) {
        for (SecurityQuestion sq : SecurityQuestion.values()) {
            if (sq.getQuestion().equalsIgnoreCase(question)) {
                return sq;
            }
        }
        throw new IllegalArgumentException("Unknown question: " + question);
    }
}