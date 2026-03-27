package net.bobdb.ai_doggos;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AiDoggosApplicationTests {

	@MockBean
	SimpleVectorStore simpleVectorStore;

	@Test
	void contextLoads() {
	}

}
