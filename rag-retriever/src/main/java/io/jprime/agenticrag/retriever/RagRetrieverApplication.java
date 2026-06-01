package io.jprime.agenticrag.retriever;

import io.jprime.agenticrag.retriever.domain.observability.OtelContextAccessor;
import io.micrometer.context.ContextRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RagRetrieverApplication {

	public static void main(String[] args) {
		ContextRegistry.getInstance().registerThreadLocalAccessor(new OtelContextAccessor());

		SpringApplication.run(RagRetrieverApplication.class, args);
	}

}
