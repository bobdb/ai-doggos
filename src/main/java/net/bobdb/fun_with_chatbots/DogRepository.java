package net.bobdb.fun_with_chatbots;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface DogRepository extends JpaRepository<Dog, Integer>, QueryByExampleExecutor<Dog> {}
