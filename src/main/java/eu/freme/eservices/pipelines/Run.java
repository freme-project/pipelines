/**
 * Copyright (C) 2015 Felix Sasaki (Felix.Sasaki@dfki.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
