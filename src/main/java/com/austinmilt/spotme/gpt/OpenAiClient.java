package com.austinmilt.spotme.gpt;

import java.util.Set;

import com.austinmilt.spotme.Env;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class OpenAiClient {
    private final OpenAiService openAiService;

    private OpenAiClient(final OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public static OpenAiClient make() {
        final OpenAiService openAiService = new OpenAiService(Env.OPEN_AI_API_KEY);
        return new OpenAiClient(openAiService);
    }

    public Set<String> getProductRecommendations(Set<String> musicGenres) {
        final String prompt = String.format(
                "In 200 words or less, convince me buy tickets for concert featuring " +
                        "4 bands playing the following musical genres: %s. Include the names " +
                        "of all 4 bands and sound extra excited about the headliner.",
                String.join(", ", musicGenres));
        final CompletionRequest request = CompletionRequest.builder()
                .prompt(prompt)
                .model("text-davinci-003")
                .echo(false)
                .n(1)
                .maxTokens(2048)
                .build();
        return Set.of(openAiService.createCompletion(request).getChoices().get(0).getText());
    }
}
