package hanghae.application.dto.common;

import hanghae.domain.entity.Screen;

public record ScreenResponse(
        String screenName
) {

    public static ScreenResponse from(Screen screen) {
        return new ScreenResponse(
                screen.getScreenName()
        );
    }
}
