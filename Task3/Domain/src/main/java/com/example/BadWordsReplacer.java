package com.example;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BadWordsReplacer {

    public String Replace(String currentMessage, List<String> prohibitedWords)
    {
        if (currentMessage == null || prohibitedWords == null) {
            return currentMessage;
        }

        // Создаем regex pattern для всех запрещенных слов
        String regex = "\\b(" + String.join("|", prohibitedWords) + ")\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        // Используем Matcher для поиска и замены
        Matcher matcher = pattern.matcher(currentMessage);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // Заменяем найденное слово на звездочки той же длины
            matcher.appendReplacement(result, "*".repeat(matcher.group().length()));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
