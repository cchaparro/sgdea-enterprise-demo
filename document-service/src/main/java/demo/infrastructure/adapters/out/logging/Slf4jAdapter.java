package demo.infrastructure.adapters.out.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import demo.ports.out.LogPort;

@Component
public class Slf4jAdapter implements LogPort {

    private static final Logger log = LoggerFactory.getLogger(Slf4jAdapter.class);

    public void info(String msg) {
        log.info(msg);
    }

    public void error(String msg, Exception ex) {
        log.error(msg, ex);
    }

}
