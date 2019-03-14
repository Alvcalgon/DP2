
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query("select distinct h from History h where h.brotherhood.id=?1")
	History findHistoryByBrotherhood(int brotherhoodId);

	@Query("select distinct h from History h where h.inceptionRecord.id=?1")
	History findHistoryByInceptionRecord(int inceptionRecordId);

	@Query("select h from History h join h.periodRecords pr where pr.id=?1")
	History findHistoryByPeriodRecord(int periodRecordId);

	@Query("select h from History h join h.miscellaneousRecords mr where mr.id=?1")
	History findHistoryByMiscellaneousRecord(int miscellaneousRecordId);

	@Query("select h from History h join h.legalRecords lr where lr.id=?1")
	History findHistoryByLegalRecord(int legalRecordId);

	@Query("select h from History h join h.linkRecords lir where lir.id=?1")
	History findHistoryByLinkRecord(int linkRecordId);

}
