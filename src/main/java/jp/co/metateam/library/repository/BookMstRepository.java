package jp.co.metateam.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.metateam.library.model.BookMst;
import java.util.List;
import java.util.Optional;

public interface BookMstRepository extends JpaRepository<BookMst, Long> {

	@Query(value = "SELECT * FROM book_mst LIMIT 1000", nativeQuery = true)
	List<BookMst> findLimitedBook();

	@Query(value = "SELECT * FROM book_mst WHERE id = ?1", nativeQuery = true)
	Optional<BookMst> selectById(Long id);


	@Query(value = "SELECT COUNT(*) FROM book_mst WHERE isbn = :isbn", nativeQuery = true)
long countByIsbn(@Param("isbn") String isbn);

	//何件あるのかをカウント　0件だったらOK　1件だったら重複チェック
	//SQLでかぶっているISBNが何件あるかカウント　カウントをどうやって行うかを調べる

}
