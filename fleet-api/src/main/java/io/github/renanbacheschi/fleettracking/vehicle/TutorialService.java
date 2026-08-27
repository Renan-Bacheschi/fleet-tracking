package io.github.renanbacheschi.fleettracking.vehicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TutorialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialService.class);

    public void requestTutorial(String message) {
        LOGGER.info("Tutorial requested: {}", message);
    }
}
