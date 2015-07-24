package eu.freme.eservices.pipelines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Gerald Haesendonck
 */
@SpringBootApplication
@ComponentScan(basePackages = "eu.freme.eservices.pipelines.api")

public class Run {
	private ConfigurableApplicationContext appContext;
	private static final Logger logger = LoggerFactory.getLogger(Run.class);

	public void run() {
		appContext = SpringApplication.run(Run.class);
	}

	public void close() {
		if (appContext != null) {
			SpringApplication.exit(appContext);
		}
	}

	public static void main(String[] args) {
		logger.info("Starting e-Publishing service");
		Run run = new Run();
		run.run();
	}
}
