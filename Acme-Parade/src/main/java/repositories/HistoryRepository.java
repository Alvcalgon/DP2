
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
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

	@Query("select h.brotherhood from History h where (1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size) = (select max(1 + hi.periodRecords.size + hi.miscellaneousRecords.size + hi.legalRecords.size + hi.linkRecords.size) from History hi)")
	Brotherhood findBrotherhoohLargestHistory();

	@Query("select avg(1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size), min(1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size), max(1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size), stddev(1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size) from History h")
	Double[] findDataNumberRecordsPerHistory();

	@Query("select h.brotherhood from History h where (1 + h.periodRecords.size + h.miscellaneousRecords.size + h.legalRecords.size + h.linkRecords.size) > (select avg(1 + hi.periodRecords.size + hi.miscellaneousRecords.size + hi.legalRecords.size + hi.linkRecords.size) from History hi)")
	Collection<Brotherhood> findBrotherhoohsLargestHistoryAvg();
}
