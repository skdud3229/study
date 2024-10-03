package com.example.web_ide;

import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.repository.BoardRepository;
import com.example.web_ide.service.ViewLikesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

@Import(ApplicationConfig.class)
@SpringBootTest
@Transactional
class WebIdeApplicationTests {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ViewLikesService viewLikesService;

	@PersistenceContext
	private EntityManager entityManager;


	@Test
	void contextLoads() {
	}



}
